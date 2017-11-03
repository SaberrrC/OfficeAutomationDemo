package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.iflytek.cloud.thirdparty.V;
import com.jakewharton.rxbinding2.view.RxView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.MeetingDetailsAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingDetailsActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingDetailsActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

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
    @Bind(R.id.tv_network_error)
    TextView mTvNetworkError;
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
            case com.shanlinjinrong.uilibrary.R.id.topview_right_view:
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
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void onRefresh() {
        initData();
    }
}
