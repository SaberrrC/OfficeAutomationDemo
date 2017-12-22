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
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.PushMsg;
import com.shanlinjinrong.oa.ui.activity.home.approval.ApprovalListActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.MyLaunchWorkReportActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportCheckActivity;
import com.shanlinjinrong.oa.ui.activity.notice.NoticeListActivity;
import com.shanlinjinrong.oa.ui.activity.push.contract.PushListContract;
import com.shanlinjinrong.oa.ui.activity.push.presenter.PushListPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.ui.fragment.adapter.TabMsgListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/18 16:45.
 * Description:推送消息列表
 */
public class PushListActivity extends HttpBaseActivity<PushListPresenter> implements SwipeRefreshLayout.OnRefreshListener, PushListContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notice_detail_recyclerView)
    RecyclerView mRecyclerView;
    private ArrayList<PushMsg> list;
    private TabMsgListAdapter mAdapter;
    @BindView(R.id.layout_content)
    RelativeLayout mRootView;
    @BindView(R.id.swipeRefreshLayout)
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

    @Override
    protected void initInject() {

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
                                MyLaunchWorkReportActivity.class);
                        break;
                    case 6:
                        intent = new Intent(PushListActivity.this,
                                ApprovalListActivity.class);
                        break;
                    case 7:
                        intent = new Intent(PushListActivity.this,
                                ApprovalListActivity.class);
                        break;

                    case 10://工作汇报：发送我的
                        intent = new Intent(PushListActivity.this,
                                WorkReportCheckActivity.class);
                        break;
                    default:
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
                                handler.sendEmptyMessageDelayed(LOAD_MORE_CONTENT, 1000);
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
        mPresenter.readPush(AppConfig.getAppConfig(this).getDepartmentId(), pid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
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
        mPresenter.loadPushMsg(AppConfig.getAppConfig(this).getDepartmentId(), currentPage, limit, loadMore);
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
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public void loadPushMsgSuccess(ArrayList<PushMsg> pushMessages, boolean loadMore) {
        if (loadMore) {
            //如果是加载更多的话，需要将最后一个view移除了
            mAdapter.removeAllFooterView();
        }
        isLoading = false;

        list.addAll(pushMessages);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadPushMsgEmpty() {
        showEmptyView(mRootView, "抱歉，当前推送消息为空", 0, false);
    }

    @Override
    public void loadPushMsgLimitContentEmpty() {
        mAdapter.removeAllFooterView();
        hasMore = false;
        showToast("没有更多了");
    }

    @Override
    public void loadPushMsgFailed(int errCode, String errMsg) {
        catchWarningByCode(errCode);
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void readPushFailed(int errCode, String errMsg) {
        catchWarningByCode(errCode);
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }
}
