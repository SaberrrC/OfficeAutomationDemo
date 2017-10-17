package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.MeetingDetailsAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingDetailsActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingDetailsActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择会议室
 */
public class MeetingDetailsActivity extends HttpBaseActivity<MeetingDetailsActivityPresenter> implements MeetingDetailsActivityContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.meeting_details_list)
    RecyclerView mMeetingDetailsList;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private MeetingDetailsAdapter mMeetingRoomAdapter;
    private List<MeetingRoomsBean.DataBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mRefreshLayout.setRefreshing(true);
        mPresenter.getMeetingRooms();
    }

    private void initView() {
        mTopView.getRightView().setOnClickListener(this);
        mMeetingDetailsList.setLayoutManager(new LinearLayoutManager(this));
        mMeetingRoomAdapter = new MeetingDetailsAdapter(this, data);
        mMeetingDetailsList.setAdapter(mMeetingRoomAdapter);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MeetingReservationRecordActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void requestFinish() {
    }

    @Override
    public void getMeetingRoomsSuccess(List<MeetingRoomsBean.DataBean> data) {
        mMeetingRoomAdapter.setNewData(data);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getMeetingRoomsFailed(String data) {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
