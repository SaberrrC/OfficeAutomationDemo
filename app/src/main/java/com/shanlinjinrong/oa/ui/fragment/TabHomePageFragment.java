package com.shanlinjinrong.oa.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.home.approval.ApprovalListActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.LaunchApprovalActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.CreateNoteActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.MyMailActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.ScheduleActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.SelectOrdinaryMeetingRoomActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.SelectVedioMeetingRoomActivity;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.WriteWeeklyNewspaperActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.MyLaunchWorkReportActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportCheckActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity;
import com.shanlinjinrong.oa.ui.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 首页消息页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabHomePageFragment extends BaseFragment {

    @Bind(R.id.title)
    TextView mTvTitle;
    private RelativeLayout mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = (RelativeLayout) inflater.inflate(R.layout.tab_homepage_fragment, container,
                false);
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    @OnClick({R.id.rl_work_report_launch, R.id.rl_work_report_send_to_me, R.id
            .rl_work_report_copy_to_me, R.id.rl_work_report_launch_report, R.id
            .rl_approval_me_launch, R.id.rl_approval_wait_me_approval, R.id
            .rl_approval_me_approvaled, R.id.rl_approval_launch_approval, R.id
            .rl_schedule_meeting_plan, R.id
            .rl_schedule_create_common_meeting, R.id.rl_schedule_create_video_meeting, R.id
            .rl_schedule_create_note, R.id.rl_schedule_my_mail})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_schedule_my_mail:
                intent = new Intent(mContext, MyMailActivity.class);
                break;
            case R.id.rl_work_report_launch:
//                Toast.makeText(mContext, "新功能程序员正在加紧开发中哦～", Toast.LENGTH_SHORT).show();
                intent = new Intent(mContext, MyLaunchWorkReportActivity.class);
                break;
            case R.id.rl_work_report_send_to_me:
//                Toast.makeText(mContext, "新功能程序员正在加紧开发中哦～", Toast.LENGTH_SHORT).show();
                intent = new Intent(mContext, WorkReportCheckActivity.class);
                break;
            case R.id.rl_work_report_copy_to_me:
//                Toast.makeText(mContext, "新功能程序员正在加紧开发中哦～", Toast.LENGTH_SHORT).show();
//                intent = new Intent(mContext, WorkReportListActivity.class);
                intent = new Intent(mContext, WriteWeeklyNewspaperActivity.class);
                break;
            case R.id.rl_work_report_launch_report:
                intent = new Intent(mContext, WorkReportLaunchActivity.class);
                break;
            case R.id.rl_approval_me_launch:
                intent = new Intent(mContext, ApprovalListActivity.class);
                intent.putExtra("whichList", 1);
                break;
            case R.id.rl_approval_wait_me_approval:
                intent = new Intent(mContext, ApprovalListActivity.class);
                intent.putExtra("whichList", 2);
                break;
            case R.id.rl_approval_me_approvaled:
                intent = new Intent(mContext, ApprovalListActivity.class);
                intent.putExtra("whichList", 3);
                break;
            case R.id.rl_approval_launch_approval:
                intent = new Intent(mContext, LaunchApprovalActivity.class);
                break;
            case R.id.rl_schedule_meeting_plan:
                intent = new Intent(mContext, ScheduleActivity.class);
                intent.putExtra("whichId", 1);
                break;
            case R.id.rl_schedule_create_common_meeting:
                //创建普通会议-旧版
//                intent=new Intent(mContext, SelectMeetingRoomActivity.class);
//                intent.putExtra("meetingType","1");
                //创建普通会议-新版
                intent = new Intent(mContext, SelectOrdinaryMeetingRoomActivity.class);
                intent.putExtra("meetingType", "1");
                //今日安排
//                intent=new Intent(mContext, ViewTheMeetingScheduleActivity.class);
//                intent.putExtra("meetingType","1");
                break;
            case R.id.rl_schedule_create_video_meeting:
                intent = new Intent(mContext, SelectVedioMeetingRoomActivity.class);
                intent.putExtra("meetingType", "2");
                break;

            case R.id.rl_schedule_create_note:
                intent = new Intent(mContext, CreateNoteActivity.class);
                break;

        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}