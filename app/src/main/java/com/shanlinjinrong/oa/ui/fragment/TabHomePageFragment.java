package com.shanlinjinrong.oa.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.BuildConfig;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.home.approval.LaunchApprovalActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.MyMailActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingDetailsActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.HolidaySearchActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.MyAttendenceActivity;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.WriteWeeklyNewspaperActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.MyLaunchWorkReportActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportCheckActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.MyUpcomingTasksActivity;
import com.shanlinjinrong.oa.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 首页消息页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabHomePageFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView mTvTitle;

    @BindView(R.id.iv_send_to_me_dot)
    ImageView mSendToMeDot;

    @BindView(R.id.iv_wait_me_approval_dot)
    ImageView mWaitMeApprovalDot;

    private RelativeLayout mRootView;

    private static int    TYPE_SEND_TO_ME       = 0;//发送我的
    private static int    TYPE_WAIT_ME_APPROVAL = 1;//待我审批
    private static String DOT_STATUS            = "DOT_STATUS";
    public static  String DOT_SEND              = "DOT_SEND";
    public static  String DOT_APPORVAL          = "DOT_APPORVAL";

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
    protected void lazyLoadData() {

    }


    /**
     * 初始化控件
     */
    private void initWidget() {
        mTvTitle.setText(AppConfig.getAppConfig(mContext).get(AppConfig.PREF_KEY_COMPANY_NAME));
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
            mSendToMeDot.setVisibility(View.VISIBLE);
        } else {
            mSendToMeDot.setVisibility(View.GONE);
        }
        if (sp.getBoolean(DOT_APPORVAL, false)) {
            mWaitMeApprovalDot.setVisibility(View.VISIBLE);
        } else {
            mWaitMeApprovalDot.setVisibility(View.GONE);
        }
    }

    public void clearDot(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(AppConfig.getAppConfig(mContext).getPrivateUid() + DOT_STATUS, Context.MODE_PRIVATE);
        sp.edit().remove(name).apply();
        refreshDot();
    }


    @OnClick({R.id.rl_test, R.id.rl_work_report_launch, R.id.rl_work_report_send_to_me, R.id.rl_work_report_copy_to_me, R.id.rl_work_report_launch_report, R.id.rl_approval_me_launch, R.id.rl_approval_wait_me_approval, R.id.rl_approval_me_approvaled, R.id.rl_approval_launch_approval, R.id.rl_schedule_my_mail, R.id.rl_schedule_book_meeting, R.id.rl_my_attandance, R.id.rl_holiday_search, R.id.rl_pay_search})
    public void onClick(View view) {
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
                    intent = new Intent(mContext, EaseChatMessageActivity.class);
                    intent.putExtra("cmy", "cmy");
                }
                break;
            case R.id.rl_holiday_search:
                //假期查询
                intent = new Intent(mContext, HolidaySearchActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}