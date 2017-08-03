package com.itcrm.GroupInformationPlatform.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Api;
import com.itcrm.GroupInformationPlatform.common.Constants;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.model.PushMsg;
import com.itcrm.GroupInformationPlatform.ui.activity.ApprovalListActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.MainController;
import com.itcrm.GroupInformationPlatform.ui.activity.MeetingInfoActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.NoticeListActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.WorkReportListActivity;
import com.itcrm.GroupInformationPlatform.ui.adapter.TabMsgListAdapter;
import com.itcrm.GroupInformationPlatform.ui.base.BaseFragment;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.fragment
 * Author:Created by Tsui on Date:2016/11/9 11:02
 * Description:消息列表fragment
 */
public class TabMsgListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.notice_detail_recyclerView)
    RecyclerView mRecyclerView;

    RadioButton rbDateAll;
    RadioButton rbDateCurrentMonth;
    RadioButton rbDateLastMonth;
    RadioButton rbDateLastTwoMonth;
    RadioButton rbDateLastThreeMonth;
    RadioButton rbDateLastSixMonth;
    RadioButton rbTypeAll;
    RadioButton rbTypeWorkReport;
    RadioButton rbTypeApproval;
    RadioButton rbTypeNotice;
    RadioButton rbTypeSchedule;
    RadioButton rbTypeAppoint;

    private ArrayList<PushMsg> list;
    private TabMsgListAdapter mAdapter;
    @Bind(R.id.layout_root)
    RelativeLayout mRootView;
    @Bind(R.id.layout_content)
    RelativeLayout mContentView;
    @Bind(R.id.rl_condition)
    RelativeLayout mRlCondition;
    @Bind(R.id.tab_homepage_top)
    RelativeLayout mTabHomePageTop;

    private ArrayList<RadioButton> listDate;
    private ArrayList<RadioButton> listType;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private int limit = 10;//当前条目数量
    private int currentPage = 1;//当前界面起始页
    private boolean hasMore = false;//是否有更多
    private Integer NO_MORE_CONTENT = 0;//没有更多内容的标识
    private Integer LOAD_MORE_CONTENT = 1;//jia zai更多内容的标识
    private boolean isLoading = false;
    private boolean isFirst = true;
    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == NO_MORE_CONTENT) {
                showToast("没有更多了");
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (msg.what == LOAD_MORE_CONTENT) {
                loadData(false, true, "", "");
            }
            return false;
        }
    });
    private PopupWindow popupWindow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, inflater.inflate(R.layout.tab_msg_list, container,
                false));
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        loadData(true, false, "", "");
    }

    private void initWidget() {

        mRlCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0EA7ED"),
                Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(mContext);
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
                        intent = new Intent(mContext,
                                NoticeListActivity.class);
                        break;
                    case 5://工作汇报：我发起的
                        intent = new Intent(mContext,
                                WorkReportListActivity.class);
                        intent.putExtra("whichList", 1);
                        break;
                    case 6:
                        intent = new Intent(mContext,
                                ApprovalListActivity.class);
                        intent.putExtra("whichList", 2);
                        break;
                    case 7:
                        intent = new Intent(mContext,
                                ApprovalListActivity.class);
                        intent.putExtra("whichList", 1);
                        break;
                    case 8:
                    case 9:
                        intent = new Intent(mContext,
                                MeetingInfoActivity.class);
                        intent.putExtra("meeting_mode", Constants.PUSH_CONFORM_MEETING);
                        intent.putExtra("cf_id", list.get(i).getPid());
                        intent.putExtra("isShowJoin", true);
                        break;
                    case 10://工作汇报：发送我的
                        intent = new Intent(mContext,
                                WorkReportListActivity.class);
                        intent.putExtra("whichList", 2);
                        break;
                    case 11://工作汇报：抄送我的
                        intent = new Intent(mContext,
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
                    hasMore=true;
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition == list.size() - 1) {
                        //看到了最后一条，显示加载更多的状态
                        View view = View.inflate(mContext, R.layout.load_more_layout, null);

                        if (hasMore) {
                            try {
                                //如果没有在加载，才去加载
                                if (!isLoading) {
                                    isLoading = true;
                                    if (!list.isEmpty()) {
                                        mAdapter.addFooterView(view, list.size());
                                    }
                                    handler.sendEmptyMessageDelayed(LOAD_MORE_CONTENT, 1000);
                                }
                            } catch (Exception e) {
                            }

                        } else {
                            handler.sendEmptyMessage(NO_MORE_CONTENT);
                        }
                    }
                }
            }
        });
    }

    private void showPopupWindow() {
        View view = View.inflate(mContext, R.layout.tab_msg_list_popwindow, null);
        bind(view);
        addRadioButtonToList();
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer dateValue = checkCheckedListDateRb();
                Integer typeValue = checkCheckedListTypeRb();
                loadData(false, false, String.valueOf(dateValue), String.valueOf(typeValue));
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.setAnimationStyle(R.style.top_filter_pop_anim_style);
            popupWindow.showAsDropDown(mTabHomePageTop, 0, 0, Gravity.CENTER);
        }
    }

    private void bind(View view) {
        rbDateAll = (RadioButton) view.findViewById(R.id.rb_date_all);
        rbDateCurrentMonth = (RadioButton) view.findViewById(R.id.rb_date_current_month);
        rbDateLastMonth = (RadioButton) view.findViewById(R.id.rb_date_last_month);
        rbDateLastTwoMonth = (RadioButton) view.findViewById(R.id.rb_date_last_two_month);
        rbDateLastThreeMonth = (RadioButton) view.findViewById(R.id.rb_date_last_three_month);
        rbDateLastSixMonth = (RadioButton) view.findViewById(R.id.rb_date_last_six_month);
        rbTypeAll = (RadioButton) view.findViewById(R.id.rb_type_all);
        rbTypeWorkReport = (RadioButton) view.findViewById(R.id.rb_type_work_report);
        rbTypeApproval = (RadioButton) view.findViewById(R.id.rb_type_approval);
        rbTypeNotice = (RadioButton) view.findViewById(R.id.rb_type_notice);
        rbTypeSchedule = (RadioButton) view.findViewById(R.id.rb_type_schedule);
        rbTypeAppoint = (RadioButton) view.findViewById(R.id.rb_type_appoint);
    }

    private Integer checkCheckedListTypeRb() {
        for (int i = 0; i < listType.size(); i++) {
            if (listType.get(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }

    private Integer checkCheckedListDateRb() {
        for (int i = 0; i < listDate.size(); i++) {
            if (listDate.get(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }

    private void addRadioButtonToList() {

        listDate = new ArrayList<>();
        listDate.add(rbDateAll);
        listDate.add(rbDateCurrentMonth);
        listDate.add(rbDateLastMonth);
        listDate.add(rbDateLastTwoMonth);
        listDate.add(rbDateLastThreeMonth);
        listDate.add(rbDateLastSixMonth);
        setListenerForEachRb(listDate);
        listType = new ArrayList<>();
        //1日程2工作汇报3审批4通知公告
        listType.add(rbTypeAll);
        listType.add(rbTypeSchedule);
        listType.add(rbTypeWorkReport);
        listType.add(rbTypeApproval);
        listType.add(rbTypeNotice);
        listType.add(rbTypeAppoint);
        setListenerForEachRb(listType);

        //默认选中所有的
        rbDateAll.setChecked(true);
        rbTypeAll.setChecked(true);

    }

    /**
     * @param
     */
    private void setListenerForEachRb(final ArrayList<RadioButton> listRb) {
        for (int i = 0; i < listRb.size(); i++) {
            final RadioButton rb = listRb.get(i);
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        clearListCheckedRb(listRb);
                        rb.setChecked(true);
                    }
                }
            });
        }
    }

    /**
     * @param list 清除所有选中的radiobutton
     */
    private void clearListCheckedRb(List<RadioButton> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
        }
    }


    private void readPush(String pid) {

        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(mContext).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(mContext).getPrivateToken());

        params.put("department_id", AppConfig.getAppConfig(mContext).getDepartmentId());
        params.put("pid", pid);

        initKjHttp().post(Api.MESSAGE_READPUSH, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                showLoadingView();
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
    public void onResume() {
        currentPage = 1;
        reFresUnRedCount();
        if (!isFirst) {
            onRefresh();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        isFirst = false;
        super.onPause();
    }

    /**
     * 刷新消息未读的红点数量
     */
    private void reFresUnRedCount() {
        MainController activity = (MainController) getActivity();
        activity.refreshUnReadMsgCount();
    }

    @Override
    public void onRefresh() {
        try {
            mAdapter.removeAllFooterView();
        } catch (Exception e) {
        }
        loadData(true, false, "", "");
        reFresUnRedCount();
    }


    /**
     * @param isPull   是否是下拉刷新
     * @param loadMore 是否是加载更多
     */
    private void loadData(boolean isPull, final boolean loadMore, String time, String where) {
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
        params.put("time", time);
        params.put("where", where);
        params.put("page", currentPage);
        params.put("uid", AppConfig.getAppConfig(mContext).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(mContext).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(mContext).getDepartmentId());

        LogUtils.e("limit"+ limit);
        LogUtils.e("time"+time);
        LogUtils.e("where"+ where);
        LogUtils.e("page"+ currentPage);
        LogUtils.e("uid"+AppConfig.getAppConfig(mContext).getPrivateUid());
        LogUtils.e("department_id"+AppConfig.getAppConfig(mContext).getDepartmentId());

        initKjHttp().post(Api.MESSAGE_PUSHS, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                removeEmptyView(mContentView);
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
                            showEmptyView(mContentView, "当前没有收到通知", 0, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                        case Api.LIMIT_CONTENT_EMPTY:
                            mAdapter.removeAllFooterView();
                            hasMore = false;
                            showToast("没有更多了");
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
                if (null != mSwipeRefreshLayout) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e("onFailure->"+errorNo+strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }

    private void changeLoadState() {
        currentPage = 1;
        list.clear();
        mAdapter.notifyDataSetChanged();
        if (list.size() > 9) {
            hasMore = true;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        handler.removeCallbacksAndMessages(null);
    }
}