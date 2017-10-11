package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 */
public class MeetingReservationRecordActivity extends HttpBaseActivity<MeetingReservationRecordActivityPresenter> implements MeetingReservationRecordActivityContract.View {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.rv_meeting_Reservation_Record)
    RecyclerView mRvMeetingReservationRecord;

    private MeetingReservationRecordAdapter mRecordAdapter;
    private List<ReservationRecordBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_reservation_record);
        ButterKnife.bind(this);


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
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(int code) {

    }
}
