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

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.EventMessage;
import com.shanlinjinrong.oa.ui.activity.home.approval.ApprovalListActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.LaunchApprovalActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.MyMailActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingDetailsActivity;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.WriteWeeklyNewspaperActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.MyLaunchWorkReportActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportCheckActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity;
import com.shanlinjinrong.oa.ui.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @Bind(R.id.iv_send_to_me_dot)
    ImageView mSendToMeDot;

    @Bind(R.id.iv_wait_me_approval_dot)
    ImageView mWaitMeApprovalDot;

    private RelativeLayout mRootView;

    private static String DOT_STATUS = "DOT_STATUS";
    public static String DOT_SEND = "DOT_SEND";
    public static String DOT_APPORVAL = "DOT_APPORVAL";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = (RelativeLayout) inflater.inflate(R.layout.tab_homepage_fragment, container,
                false);
        ButterKnife.bind(this, mRootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    /**
     * 判断去哪个列表
     * 1:系统消息 2：公司公告 3：部门通知 4：集团公告 5：工作汇报（我发起的） 6：审批申请
     * 7：审批回复 8：会议 9：视频会议 10：工作汇报(发送我的) 11：工作汇报（抄送我的）
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void showDot(EventMessage eventMessage) {
        if (eventMessage.getStr().equals("reFreash")) {
            if (eventMessage.getType() == 10) {
                saveDot(DOT_SEND);
            }

            if (eventMessage.getType() == 6) {
                saveDot(DOT_APPORVAL);
            }
            refreshDot();
        }
    }

    private void saveDot(String name) {
        SharedPreferences sp = getActivity().getSharedPreferences(DOT_STATUS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(name, true).apply();
    }

    public void refreshDot() {
        SharedPreferences sp = getActivity().getSharedPreferences(DOT_STATUS, Context.MODE_PRIVATE);
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

    public static void clearDot(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(DOT_STATUS, Context.MODE_PRIVATE);
        sp.edit().remove(name).apply();
    }


    @OnClick({R.id.rl_work_report_launch, R.id.rl_work_report_send_to_me,
            R.id.rl_work_report_copy_to_me, R.id.rl_work_report_launch_report,
            R.id.rl_approval_me_launch, R.id.rl_approval_wait_me_approval,
            R.id.rl_approval_me_approvaled, R.id.rl_approval_launch_approval,
            R.id.rl_schedule_my_mail, R.id.rl_schedule_book_meeting})

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
            case R.id.rl_schedule_book_meeting:
                intent = new Intent(mContext, MeetingDetailsActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}