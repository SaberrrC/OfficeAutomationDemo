package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.j256.ormlite.stmt.query.In;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.MeetingDetailsAdapter;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.meeting_details_list)
    RecyclerView mMeetingDetailsList;
    private MeetingDetailsAdapter ad;
    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);
        ButterKnife.bind(this);
        mTopView.getRightView().setOnClickListener(this);
        for (int i = 0; i < 10; i++) {
            data.add("");
        }
        mMeetingDetailsList.setLayoutManager(new LinearLayoutManager(this));
        ad = new MeetingDetailsAdapter(this,data);
        mMeetingDetailsList.setAdapter(ad);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MeetingReservationRecordActivity.class);
        startActivity(intent);
    }
}
