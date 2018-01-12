package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.MeetingDetailsAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingDetailsActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingDetailsActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择会议室
 */
public class MeetingDetailsActivity extends HttpBaseActivity<MeetingDetailsActivityPresenter> implements MeetingDetailsActivityContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.top_view)
    CommonTopView      mTopView;
    @BindView(R.id.meeting_details_list)
    RecyclerView       mMeetingDetailsList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_network_error)
    TextView           mTvNetworkError;
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
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark);
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
        switch (view.getId()) {
            case R.id.topview_right_view:
                Intent intent = new Intent(this, MeetingReservationRecordActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void getMeetingRoomsEmpty() {
        mTvNetworkError.setVisibility(View.VISIBLE);
        mTvNetworkError.setText(R.string.string_meeting_rooms_not_available);
    }

    @Override
    public void getMeetingRoomsSuccess(List<MeetingRoomsBean.DataBean> data) {
        mTvNetworkError.setVisibility(View.GONE);
        mMeetingRoomAdapter.setNewData(data);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getMeetingRoomsFailed(int errorCode, String data) {
        if ("auth error".equals(data)) {
            catchWarningByCode(data);
            return;
        }
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.net_no_connection));
                mTvNetworkError.setText(R.string.string_not_network);
                mTvNetworkError.setVisibility(View.VISIBLE);
                break;
            default:
                mTvNetworkError.setVisibility(View.GONE);
                break;
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
