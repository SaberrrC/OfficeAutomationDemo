package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.MeetingReservationRecordAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingReservationRecordActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingReservationRecordActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 会议室 预订记录
 */
public class MeetingReservationRecordActivity extends HttpBaseActivity<MeetingReservationRecordActivityPresenter> implements MeetingReservationRecordActivityContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.rv_meeting_Reservation_Record)
    RecyclerView mRvMeetingReservationRecord;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    private int mPage;
    private int mNum;
    private MeetingReservationRecordAdapter mRecordAdapter;
    private List<ReservationRecordBean.DataBean> data = new ArrayList<>();
    public static MeetingReservationRecordActivity mRecordActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_reservation_record);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
    }

    private void initData() {
        mRecordActivity = this;
        refreshData();
        mTopView.getLeftView().setOnClickListener(this);
        mRecordAdapter = new MeetingReservationRecordAdapter(this, data);
        mRvMeetingReservationRecord.setLayoutManager(new LinearLayoutManager(this));
        mRvMeetingReservationRecord.setAdapter(mRecordAdapter);
        mRecordAdapter.notifyDataSetChanged();
    }

    private void refreshData() {
        mRefresh.setOnRefreshListener(this);
        mRefresh.setRefreshing(true);
        HttpParams httpParams = new HttpParams();
        mPage = 0;
        mNum = 15;
        httpParams.put("page", mPage);
        httpParams.put("num", mNum);
        mPresenter.getMeetingRecord(httpParams);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(int code) {

    }

    @Override
    public void getMeetingRecordSuccess(List<ReservationRecordBean.DataBean> bean) {
        mRefresh.setRefreshing(false);
        mRecordAdapter.setNewData(bean);
        mRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void getMeetingRecordFailed(String msgStr) {
        mRefresh.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MeetingDeleteSuccess(String str) {
        if (str.equals("meetingDeleteSuccess")) {
            mRefresh.setRefreshing(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
