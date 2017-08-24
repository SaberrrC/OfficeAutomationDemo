package com.shanlinjinrong.oa.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.approval.ApprovalListActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.ScheduleActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportListActivity;
import com.shanlinjinrong.oa.ui.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 首页综合页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabGroupFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_group_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.btn_schedule, R.id.btn_work_report, R.id.btn_Approval})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Approval:
                startActivity(new Intent(getActivity(), ApprovalListActivity.class));
                break;
            case R.id.btn_work_report:
                startActivity(new Intent(getActivity(), WorkReportListActivity.class));
                break;
            case R.id.btn_schedule:
                startActivity(new Intent(getActivity(), ScheduleActivity.class));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}