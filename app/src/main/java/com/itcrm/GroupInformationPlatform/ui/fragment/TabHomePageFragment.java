package com.itcrm.GroupInformationPlatform.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.ui.activity.ApprovalListActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.CreateMeetingActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.CreateNoteActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.LaunchApprovalActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.MyMailActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.ScheduleActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.SelectMeetingRoomActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.WorkReportLaunchActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.WorkReportListActivity;
import com.itcrm.GroupInformationPlatform.ui.base.BaseFragment;

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
            .rl_schedule_meeting_plan, R.id.rl_schedule_me_launch, R.id.rl_schedule_note, R.id
            .rl_schedule_create_common_meeting, R.id.rl_schedule_create_video_meeting, R.id
            .rl_schedule_create_note, R.id.rl_schedule_my_mail})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_schedule_my_mail:
                intent = new Intent(mContext, MyMailActivity.class
                );
                break;
            case R.id.rl_work_report_launch:
                intent = new Intent(mContext, WorkReportListActivity.class
                );
                intent.putExtra("whichList", 1);
                break;
            case R.id.rl_work_report_send_to_me:
                intent = new Intent(mContext, WorkReportListActivity.class
                );
                intent.putExtra("whichList", 2);
                break;
            case R.id.rl_work_report_copy_to_me:
                intent = new Intent(mContext, WorkReportListActivity.class
                );
                intent.putExtra("whichList", 3);
                break;
            case R.id.rl_work_report_launch_report:
                intent = new Intent(mContext, WorkReportLaunchActivity.class
                );
                break;
            case R.id.rl_approval_me_launch:
                intent = new Intent(mContext, ApprovalListActivity.class);
                intent.putExtra("whichList",1);
                break;
            case R.id.rl_approval_wait_me_approval:
                intent = new Intent(mContext, ApprovalListActivity.class);
                intent.putExtra("whichList",2);
                break;
            case R.id.rl_approval_me_approvaled:
                intent = new Intent(mContext, ApprovalListActivity.class);
                intent.putExtra("whichList",3);
                break;
            case R.id.rl_approval_launch_approval:
                intent=new Intent(mContext, LaunchApprovalActivity.class);
                break;
            case R.id.rl_schedule_meeting_plan:
                intent=new Intent(mContext, ScheduleActivity.class);
                intent.putExtra("whichId",1);
                break;
            case R.id.rl_schedule_me_launch:
                intent=new Intent(mContext, ScheduleActivity.class);
                intent.putExtra("whichId",2);
                break;
            case R.id.rl_schedule_note:
                intent=new Intent(mContext, ScheduleActivity.class);
                intent.putExtra("whichId",3);
                break;
            case R.id.rl_schedule_create_common_meeting:
                intent=new Intent(mContext, SelectMeetingRoomActivity.class);
                intent.putExtra("meetingType","1");
                break;
            case R.id.rl_schedule_create_video_meeting:
                intent=new Intent(mContext, SelectMeetingRoomActivity.class);
                intent.putExtra("meetingType","2");
                break;
            case R.id.rl_schedule_create_note:
                intent=new Intent(mContext, CreateNoteActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}