package com.shanlinjinrong.oa.ui.activity.push;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.PushMsg;
import com.shanlinjinrong.oa.ui.activity.home.schedule.MeetingInfoActivity;
import com.shanlinjinrong.oa.ui.activity.notice.NoticeListActivity;
import com.shanlinjinrong.oa.ui.adapter.TabMsgListAdapter;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.ApprovalListActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportListActivity;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/18 16:45.
 * Description:推送消息列表
 */
public class PushListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.notice_detail_recyclerView)
    RecyclerView mRecyclerView;
    private ArrayList<PushMsg> list;
    private TabMsgListAdapter mAdapter;
    @Bind(R.id.layout_content)
    RelativeLayout mRootView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private int limit = 10;//当前条目数量
    private int currentPage = 1;//当前界面起始页
    private boolean hasMore = true;//是否有更多
    private Integer NO_MORE_CONTENT = 0;//没有更多内容的标识
    private Integer LOAD_MORE_CONTENT = 1;//jia zai更多内容的标识
    private boolean isLoading = false;

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == NO_MORE_CONTENT) {
                showToast("没有更多了");
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (msg.what == LOAD_MORE_CONTENT) {
                loadData(false, true);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_msg_list);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWidget();
        loadData(false, false);
    }

    private void initWidget() {


        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#468bff"),
                Color.parseColor("#468bff"), Color.parseColor("#468bff"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        mAdapter = new TabMsgListAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int i) {
                //类型 5工作汇报，6审批申请，7审批回复	string
                PushMsg pushMsg = list.get(i);
                pushMsg.setStatus("2");
                mAdapter.notifyDataSetChanged();

                //发送推送已读
                readPush(pushMsg.getPid());

                String type = pushMsg.getType();
                Intent intent = null;
                switch (Integer.parseInt(type)) {
                    case 2:
                    case 3:
                        intent = new Intent(PushListActivity.this,
                                NoticeListActivity.class);
                        break;
                    case 5://工作汇报：我发起的
                        intent = new Intent(PushListActivity.this,
                                WorkReportListActivity.class);
                        intent.putExtra("whichList", 1);
                        break;
                    case 6:
                        intent = new Intent(PushListActivity.this,
                                ApprovalListActivity.class);
                        break;
                    case 7:
                        intent = new Intent(PushListActivity.this,
                                ApprovalListActivity.class);
                        break;
                    case 8:
                        intent = new Intent(PushListActivity.this,
                                MeetingInfoActivity.class);
                        intent.putExtra("meeting_mode", Constants.PUSH_CONFORM_MEETING);
                        intent.putExtra("cf_id", list.get(i).getPid());
                        break;
                    case 10://工作汇报：发送我的
                        intent = new Intent(PushListActivity.this,
                                WorkReportListActivity.class);
                        intent.putExtra("whichList", 2);
                        break;
                    case 11://工作汇报：抄送我的
                        intent = new Intent(PushListActivity.this,
                                WorkReportListActivity.class);
                        intent.putExtra("whichList", 3);
                        break;
                }

                if (intent != null) {
                    startActivity(intent);
                }


            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 0屏幕停止滚动；1:滚动且用户仍在触碰或手指还在屏幕上2：随用户的操作，屏幕上产生的惯性滑动；
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == 0 && list.size() > 9) {
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition == list.size() - 1) {
                        //看到了最后一条，显示加载更多的状态
                        View view = View.inflate(PushListActivity.this, R.layout.load_more_layout, null);

                        if (hasMore) {
                            //如果没有在加载，才去加载
                            if (!isLoading) {
                                isLoading = true;
                                mAdapter.addFooterView(view, list.size());
                                handler.sendEmptyMessageDelayed(LOAD_MORE_CONTENT,1000);
                            }
                        } else {
                            handler.sendEmptyMessage(NO_MORE_CONTENT);
                        }
                    }
                }
            }
        });
    }

    private void readPush(String pid) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());

        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("pid", pid);

        initKjHttp().post(Api.MESSAGE_READPUSH, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            break;
                        case Api.RESPONSES_CONTENT_EMPTY:
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage=1;
    }

    @Override
    public void onRefresh() {
        loadData(true, false);
    }



    /**
     * @param isPull   是否是下拉刷新
     * @param loadMore 是否是加载更多
     */
    private void loadData(boolean isPull, final boolean loadMore) {
        if (isPull) {
            mSwipeRefreshLayout.setRefreshing(true);
            changeLoadState();
        }
        if (!isPull && !loadMore) {
            showLoadingView();
            changeLoadState();
        }
        if (loadMore) {
            currentPage++;
        }



        HttpParams params = new HttpParams();
        params.put("limit", limit);
        params.put("page", currentPage);
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        initKjHttp().post(Api.MESSAGE_PUSHS, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            ArrayList<PushMsg> listPushMsgs = new ArrayList<>();
                            JSONArray notices = Api.getDataToJSONArray(jo);
                            for (int i = 0; i < notices.length(); i++) {
                                JSONObject jsonObject = notices.getJSONObject(i);
                                PushMsg pushMsg =
                                        new PushMsg(jsonObject);
                                listPushMsgs.add(pushMsg);
                            }
                            if (loadMore) {
                                //如果是加载更多的话，需要将最后一个view移除了
                                mAdapter.removeAllFooterView();
                            }
                            isLoading = false;

                            list.addAll(listPushMsgs);
                            mAdapter.notifyDataSetChanged();
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            showEmptyView(mRootView, "抱歉，当前推送消息为空", 0, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                        case Api.LIMIT_CONTENT_EMPTY:
                            mAdapter.removeAllFooterView();
                            hasMore = false;
                            showToast("没有更多了");
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }

    private void changeLoadState() {
        currentPage = 1;
        list.clear();
        mAdapter.notifyDataSetChanged();
        hasMore = true;
    }


    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("推送消息");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        handler.removeCallbacksAndMessages(null);
    }


}