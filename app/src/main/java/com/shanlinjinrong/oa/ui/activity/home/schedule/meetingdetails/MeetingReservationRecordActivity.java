package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.MeetingReservationRecordAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingReservationRecordActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingReservationRecordActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.views.AllRecyclerView;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 会议室 预订记录
 */
public class MeetingReservationRecordActivity extends HttpBaseActivity<MeetingReservationRecordActivityPresenter> implements MeetingReservationRecordActivityContract.View {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.rv_meeting_Reservation_Record)
    RecyclerView mRvMeetingReservationRecord;
    private MeetingReservationRecordAdapter mRecordAdapter;
    private List<ReservationRecordBean.DataBean> data = new ArrayList<>();

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
        mTopView.getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mPresenter.getMeetingRecord();
        mRecordAdapter = new MeetingReservationRecordAdapter(this, data);
        mRvMeetingReservationRecord.setLayoutManager(new LinearLayoutManager(this));
        mRvMeetingReservationRecord.setAdapter(mRecordAdapter);
        mRecordAdapter.notifyDataSetChanged();
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
        mRecordAdapter.setNewData(bean);
        mRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void getMeetingRecordFailed(String msgStr) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MeetingDeleteSuccess(String str) {
        if (str.equals("meetingDeleteSuccess")) {
            mPresenter.getMeetingRecord();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
