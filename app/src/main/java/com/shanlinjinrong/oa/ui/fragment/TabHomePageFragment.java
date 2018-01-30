package com.shanlinjinrong.oa.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.retrofit.model.responsebody.LimitResponseBody;
import com.google.gson.Gson;
import com.shanlinjinrong.oa.BuildConfig;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.home.approval.LaunchApprovalActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.MyMailActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.ScheduleWeekCalendarActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingDetailsActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.HolidaySearchActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.MyAttendenceActivity;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.WriteWeeklyNewspaperActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.MyLaunchWorkReportActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportCheckActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity;
import com.shanlinjinrong.oa.ui.activity.main.event.IsNetConnectEvent;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.MyUpcomingTasksActivity;
import com.shanlinjinrong.oa.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ren.yale.android.cachewebviewlib.utils.NetworkUtils;

/**
 * <h3>Description: 首页消息页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabHomePageFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView       mTvTitle;
    @BindView(R.id.rl_work_report_launch)
    RelativeLayout mRlWorkReportLaunch;
    @BindView(R.id.stork11)
    View           mStork11;
    @BindView(R.id.rl_work_report_launch_report)
    RelativeLayout mRlWorkReportLaunchReport;
    @BindView(R.id.stork12)
    View           mStork12;
    @BindView(R.id.stork13)
    View           mStork13;
    @BindView(R.id.iv_send_to_me_dot)
    ImageView      mIvSendToMeDot;
    @BindView(R.id.stork_blank11)
    View           mStorkBlank11;
    @BindView(R.id.fl_blank11)
    FrameLayout    mFlBlank11;
    @BindView(R.id.stork_blank12)
    View           mStorkBlank12;
    @BindView(R.id.fl_blank12)
    FrameLayout    mFlBlank12;
    @BindView(R.id.stork_blank13)
    View           mStorkBlank13;
    @BindView(R.id.fl_blank13)
    FrameLayout    mFlBlank13;
    @BindView(R.id.stork_blank14)
    View           mStorkBlank14;
    @BindView(R.id.fl_blank14)
    FrameLayout    mFlBlank14;
    @BindView(R.id.rl_approval_me_launch)
    RelativeLayout mRlApprovalMeLaunch;
    @BindView(R.id.stork21)
    View           mStork21;
    @BindView(R.id.iv_wait_me_approval_dot)
    ImageView      mIvWaitMeApprovalDot;
    @BindView(R.id.stork22)
    View           mStork22;
    @BindView(R.id.iv_homepage_approval_me_approval)
    ImageView      mIvHomepageApprovalMeApproval;
    @BindView(R.id.rl_approval_me_approvaled)
    RelativeLayout mRlApprovalMeApprovaled;
    @BindView(R.id.stork23)
    View           mStork23;
    @BindView(R.id.rl_approval_launch_approval)
    RelativeLayout mRlApprovalLaunchApproval;
    @BindView(R.id.stork_blank21)
    View           mStorkBlank21;
    @BindView(R.id.fl_blank21)
    FrameLayout    mFlBlank21;
    @BindView(R.id.stork_blank22)
    View           mStorkBlank22;
    @BindView(R.id.fl_blank22)
    FrameLayout    mFlBlank22;
    @BindView(R.id.stork_blank23)
    View           mStorkBlank23;
    @BindView(R.id.fl_blank23)
    FrameLayout    mFlBlank23;
    @BindView(R.id.stork_blank24)
    View           mStorkBlank24;
    @BindView(R.id.fl_blank24)
    FrameLayout    mFlBlank24;
    @BindView(R.id.rl_my_attandance)
    RelativeLayout mRlMyAttandance;
    @BindView(R.id.stork31)
    View           mStork31;
    @BindView(R.id.rl_holiday_search)
    RelativeLayout mRlHolidaySearch;
    @BindView(R.id.stork32)
    View           mStork32;
    @BindView(R.id.stork_blank31)
    View           mStorkBlank31;
    @BindView(R.id.fl_blank31)
    FrameLayout    mFlBlank31;
    @BindView(R.id.stork_blank32)
    View           mStorkBlank32;
    @BindView(R.id.fl_blank32)
    FrameLayout    mFlBlank32;
    @BindView(R.id.stork41)
    View           mStork41;
    @BindView(R.id.rl_schedule_book_meeting)
    RelativeLayout mRlScheduleBookMeeting;
    @BindView(R.id.stork42)
    View           mStork42;
    @BindView(R.id.stork43)
    View           mStork43;
    @BindView(R.id.stork_blank41)
    View           mStorkBlank41;
    @BindView(R.id.fl_blank41)
    FrameLayout    mFlBlank41;
    @BindView(R.id.stork_blank42)
    View           mStorkBlank42;
    @BindView(R.id.fl_blank42)
    FrameLayout    mFlBlank42;
    @BindView(R.id.stork_blank43)
    View           mStorkBlank43;
    @BindView(R.id.fl_blank43)
    FrameLayout    mFlBlank43;
    @BindView(R.id.stork_blank44)
    View           mStorkBlank44;
    @BindView(R.id.fl_blank44)
    FrameLayout    mFlBlank44;
    @BindView(R.id.fl_approval_wait_me_approval)
    FrameLayout    mFlApprovalWaitMeApproval;
    @BindView(R.id.ll_work_report_container)
    LinearLayout   mLlWorkReportContainer;
    @BindView(R.id.ll_approval_container)
    LinearLayout   mLlApprovalContainer;
    @BindView(R.id.ll_approval_checking)
    LinearLayout   mLlApprovalChecking;
    @BindView(R.id.ll_approval_date)
    LinearLayout   mLlApprovalDate;
    @BindView(R.id.rl_work_report_copy_to_me)
    RelativeLayout mRlWorkReportCopyToMe;
    @BindView(R.id.rl_work_report_send_to_me)
    RelativeLayout mRlWorkReportSendToMe;
    @BindView(R.id.rl_schedule_manage)
    RelativeLayout mRlScheduleManage;
    @BindView(R.id.rl_schedule_my_mail)
    RelativeLayout mRlScheduleMyMail;
    @BindView(R.id.toolbarshadow)
    LinearLayout   mToolbarShadow;
    @BindView(R.id.fl_work_report_send_to_me)
    FrameLayout    mFlWorkReportSendToMe;
    @BindView(R.id.tv_empty_view)
    TextView       mTvEmptyView;
    private RelativeLayout mRootView;

    private static int    TYPE_SEND_TO_ME       = 0;//发送我的
    private static int    TYPE_WAIT_ME_APPROVAL = 1;//待我审批
    private static String DOT_STATUS            = "DOT_STATUS";
    public static  String DOT_SEND              = "DOT_SEND";
    public static  String DOT_APPORVAL          = "DOT_APPORVAL";
    private        long   lastClickTime         = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = (RelativeLayout) inflater.inflate(R.layout.tab_homepage_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean connected = NetworkUtils.isConnected(getActivity());
        if (connected) {
            mTvEmptyView.setVisibility(View.GONE);
            checkUserLimit();
        } else {
            showToast("网络不通，请检查网络连接！");
            mLlWorkReportContainer.setVisibility(View.GONE);
            mLlApprovalContainer.setVisibility(View.GONE);
            mLlApprovalChecking.setVisibility(View.GONE);
            mLlApprovalDate.setVisibility(View.GONE);
            mToolbarShadow.setVisibility(View.GONE);
            mTvEmptyView.setVisibility(View.VISIBLE);
            mTvEmptyView.setText("网络不通，请检查网络连接！");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserLimit(IsNetConnectEvent event) {
        LimitResponseBody userLimitBean = AppConfig.getAppConfig(getActivity()).getUserLimitBean();
        if (userLimitBean == null) {
            checkUserLimit();
        }
    }

    @Override
    protected void lazyLoadData() {
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        String title = AppConfig.getAppConfig(mContext).get(AppConfig.PREF_KEY_COMPANY_NAME);
        if (title.equals("null") || title.trim().equals("") || title.equals("false")) {
            title = "上海善林金融";
        }
        mTvTitle.setText(title);
    }

    private void checkUserLimit() {
        showLoadingView();
        LimitResponseBody userLimitBean = AppConfig.getAppConfig(getActivity()).getUserLimitBean();
        if (userLimitBean == null) {
            getUserLimitFromNet();
            return;
        }
        mTvEmptyView.setVisibility(View.GONE);
        setUserLimit(userLimitBean);
    }

    private void setUserLimit(LimitResponseBody userLimitBean) {
        hideLoadingView();
        if (userLimitBean == null) {
            showToast("没有任何权限");
            mLlWorkReportContainer.setVisibility(View.GONE);
            mLlApprovalContainer.setVisibility(View.GONE);
            mLlApprovalChecking.setVisibility(View.GONE);
            mLlApprovalDate.setVisibility(View.GONE);
            mToolbarShadow.setVisibility(View.GONE);
            mTvEmptyView.setVisibility(View.VISIBLE);
            mTvEmptyView.setText("没有任何权限");
            return;
        }
        List<LimitResponseBody.DataBean> dataList = userLimitBean.getData();
        if (dataList == null || dataList.size() == 0) {
            mLlWorkReportContainer.setVisibility(View.GONE);
            mLlApprovalContainer.setVisibility(View.GONE);
            mLlApprovalChecking.setVisibility(View.GONE);
            mLlApprovalDate.setVisibility(View.GONE);
            mToolbarShadow.setVisibility(View.GONE);
            mTvEmptyView.setVisibility(View.VISIBLE);
            mTvEmptyView.setText("没有任何权限");
            return;
        }
        mTvEmptyView.setVisibility(View.GONE);
        for (LimitResponseBody.DataBean data : dataList) {
            if (TextUtils.equals("203", data.getId())) {//我发起的
                mRlWorkReportLaunch.setVisibility(View.VISIBLE);
                mStorkBlank11.setVisibility(View.GONE);
                mFlBlank11.setVisibility(View.GONE);
            }
            if (TextUtils.equals("201", data.getId())) {//发起日报
                mStork11.setVisibility(View.VISIBLE);
                mRlWorkReportLaunchReport.setVisibility(View.VISIBLE);
                mStorkBlank12.setVisibility(View.GONE);
                mFlBlank12.setVisibility(View.GONE);
            }
            if (TextUtils.equals("202", data.getId())) {//发起周报
                mStork12.setVisibility(View.VISIBLE);
                mRlWorkReportCopyToMe.setVisibility(View.VISIBLE);
                mStorkBlank13.setVisibility(View.GONE);
                mFlBlank13.setVisibility(View.GONE);
            }
            if (TextUtils.equals("204", data.getId())) {//发给我的
                mStork13.setVisibility(View.VISIBLE);
                mFlWorkReportSendToMe.setVisibility(View.VISIBLE);
                mStorkBlank14.setVisibility(View.GONE);
                mFlBlank14.setVisibility(View.GONE);
            }
            if (TextUtils.equals("302", data.getId())) {//我的申请
                mRlApprovalMeLaunch.setVisibility(View.VISIBLE);
                mStorkBlank21.setVisibility(View.GONE);
                mFlBlank21.setVisibility(View.GONE);
            }
            if (TextUtils.equals("303", data.getId())) {//待办事宜
                mFlApprovalWaitMeApproval.setVisibility(View.VISIBLE);
                mStork21.setVisibility(View.VISIBLE);
                mStorkBlank22.setVisibility(View.GONE);
                mFlBlank22.setVisibility(View.GONE);
            }
            if (TextUtils.equals("304", data.getId())) {//已办事宜
                mStork22.setVisibility(View.VISIBLE);
                mRlApprovalMeApprovaled.setVisibility(View.VISIBLE);
                mStorkBlank23.setVisibility(View.GONE);
                mFlBlank23.setVisibility(View.GONE);
            }
            if (TextUtils.equals("301", data.getId())) {//发起申请
                mStork23.setVisibility(View.VISIBLE);
                mRlApprovalLaunchApproval.setVisibility(View.VISIBLE);
                mStorkBlank24.setVisibility(View.GONE);
                mFlBlank24.setVisibility(View.GONE);
            }
            if (TextUtils.equals("111", data.getId())) {//我的考勤
                mRlMyAttandance.setVisibility(View.VISIBLE);
                mStorkBlank31.setVisibility(View.GONE);
                mFlBlank31.setVisibility(View.GONE);
            }
            if (TextUtils.equals("112", data.getId())) {//假期查询
                mStork31.setVisibility(View.VISIBLE);
                mRlHolidaySearch.setVisibility(View.VISIBLE);
                mStorkBlank32.setVisibility(View.GONE);
                mFlBlank32.setVisibility(View.GONE);
            }
            if (TextUtils.equals("106", data.getId())) {//日程管理
                mRlScheduleManage.setVisibility(View.VISIBLE);
                mStorkBlank41.setVisibility(View.GONE);
                mFlBlank41.setVisibility(View.GONE);
            }
            if (TextUtils.equals("101", data.getId())) {//会议室预定
                mRlScheduleBookMeeting.setVisibility(View.VISIBLE);
                mStork41.setVisibility(View.VISIBLE);
                mStorkBlank42.setVisibility(View.GONE);
                mFlBlank42.setVisibility(View.GONE);
            }
            if (TextUtils.equals("105", data.getId())) {//我的邮箱
                mRlScheduleMyMail.setVisibility(View.VISIBLE);
                mStork42.setVisibility(View.VISIBLE);
                mStorkBlank43.setVisibility(View.GONE);
                mFlBlank43.setVisibility(View.GONE);
            }
        }
        if (!checkLineLimit(dataList, "2")) {//工作汇报
            mLlWorkReportContainer.setVisibility(View.GONE);
        } else {
            mLlWorkReportContainer.setVisibility(View.VISIBLE);
        }
        if (!checkLineLimit(dataList, "3")) {//流程审批
            mLlApprovalContainer.setVisibility(View.GONE);
        } else {
            mLlApprovalContainer.setVisibility(View.VISIBLE);
        }
        if (!checkLineLimit(dataList, "11")) {//考勤管理
            mLlApprovalChecking.setVisibility(View.GONE);
        } else {
            mLlApprovalChecking.setVisibility(View.VISIBLE);
        }
        if (!checkLineLimit(dataList, "10")) {//日程管理
            mLlApprovalDate.setVisibility(View.GONE);
        } else {
            mLlApprovalDate.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkLineLimit(List<LimitResponseBody.DataBean> dataList, String limits) {
        for (LimitResponseBody.DataBean dataBean : dataList) {
            String id = dataBean.getId();
            if (TextUtils.equals(id, limits)) {
                return true;
            }
        }
        return false;
    }

    private void getUserLimitFromNet() {
        KJHttp kjHttp = initKjHttp();
        kjHttp.cleanCache();
        kjHttp.get(ApiJava.USER_LIMIT, new HttpParams(), new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    LimitResponseBody bean = new Gson().fromJson(t, LimitResponseBody.class);
                    if (TextUtils.equals(bean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                        AppConfig.getAppConfig(getActivity()).setUserLimit(bean);
                        setUserLimit(bean);
                        return;
                    }
                    setUserLimit(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("获取用户权限失败");
                    setUserLimit(null);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                showToast("获取用户权限失败");
                hideLoadingView();
                mTvEmptyView.setVisibility(View.VISIBLE);
                mTvEmptyView.setText("获取用户权限失败");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void refreshDot() {
        SharedPreferences sp = getActivity().getSharedPreferences(AppConfig.getAppConfig(mContext).getPrivateUid() + DOT_STATUS, Context.MODE_PRIVATE);
        if (sp.getBoolean(DOT_SEND, false)) {
            mIvSendToMeDot.setVisibility(View.VISIBLE);
        } else {
            mIvSendToMeDot.setVisibility(View.GONE);
        }
        if (sp.getBoolean(DOT_APPORVAL, false)) {
            mIvWaitMeApprovalDot.setVisibility(View.VISIBLE);
        } else {
            mIvWaitMeApprovalDot.setVisibility(View.GONE);
        }
    }

    public void clearDot(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(AppConfig.getAppConfig(mContext).getPrivateUid() + DOT_STATUS, Context.MODE_PRIVATE);
        sp.edit().remove(name).apply();
        refreshDot();
    }


    @OnClick({R.id.rl_test, R.id.rl_work_report_launch, R.id.rl_work_report_send_to_me, R.id.rl_work_report_copy_to_me, R.id.rl_work_report_launch_report, R.id.rl_approval_me_launch, R.id.rl_approval_wait_me_approval, R.id.rl_approval_me_approvaled, R.id.rl_approval_launch_approval, R.id.rl_schedule_my_mail, R.id.rl_schedule_book_meeting, R.id.rl_my_attandance, R.id.rl_holiday_search, R.id.rl_pay_search, R.id.rl_schedule_manage})
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime < 1000) {
            lastClickTime = currentTime;
            return;
        }
        lastClickTime = currentTime;
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_schedule_my_mail:
                //我的邮箱
                intent = new Intent(mContext, MyMailActivity.class);
                break;
            case R.id.rl_work_report_launch:
                //我发起的
                intent = new Intent(mContext, MyLaunchWorkReportActivity.class);
                break;
            case R.id.rl_work_report_send_to_me:
                //发送给的
                intent = new Intent(mContext, WorkReportCheckActivity.class);
                clearDot(getContext(), DOT_SEND);
                break;
            case R.id.rl_work_report_copy_to_me:
                //发起周报
                intent = new Intent(mContext, WriteWeeklyNewspaperActivity.class);
                break;
            case R.id.rl_work_report_launch_report:
                //发起日报
                intent = new Intent(mContext, WorkReportLaunchActivity.class);
                break;
            case R.id.rl_approval_me_launch:
                //我的申请
                intent = new Intent(mContext, MyUpcomingTasksActivity.class);
                intent.putExtra("whichList", "1");
                break;
            case R.id.rl_approval_wait_me_approval:
                //待我审批
                intent = new Intent(mContext, MyUpcomingTasksActivity.class);
                intent.putExtra("whichList", "2");
                clearDot(getContext(), DOT_APPORVAL);
                break;
            case R.id.rl_approval_me_approvaled:
                //我审批的
                intent = new Intent(mContext, MyUpcomingTasksActivity.class);
                intent.putExtra("whichList", "3");
                break;
            case R.id.rl_approval_launch_approval:
                //发起审批
                intent = new Intent(mContext, LaunchApprovalActivity.class);
                break;
            case R.id.rl_schedule_book_meeting:
                //会议室预定
                intent = new Intent(mContext, MeetingDetailsActivity.class);
                break;
            case R.id.rl_my_attandance:
                //我的考勤
                intent = new Intent(mContext, MyAttendenceActivity.class);
                break;
            case R.id.rl_pay_search:
                //薪资查询
                //intent = new Intent(mContext, PayQueryActivity.class);
                break;
            case R.id.rl_test:
                if (BuildConfig.DEBUG) {
                    //intent = new Intent(mContext, UpcomingTasksActivity.class);
                }
                break;
            case R.id.rl_holiday_search:
                //假期查询
                intent = new Intent(mContext, HolidaySearchActivity.class);
                break;
            case R.id.rl_schedule_manage:
                //日程管理
                intent = new Intent(mContext, ScheduleWeekCalendarActivity.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}