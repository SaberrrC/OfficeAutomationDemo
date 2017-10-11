package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预订成功
 */
public class MeetingReservationSucceedActivity extends AppCompatActivity {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.tv_tips_Message)
    TextView mTvTipsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_reservation_succeed);
        ButterKnife.bind(this);

        String mMeetingDate = getIntent().getStringExtra("mMeetingDate");
        String mMeetingName = getIntent().getStringExtra("mMeetingName");

        mTvTipsMessage.setText(mMeetingDate +"  "+ mMeetingName);
    }

    @OnClick(R.id.btn_meeting_info_complete)
    public void onViewClicked() {
        Intent intent = new Intent(this, MeetingReservationRecordActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MeetingReservationRecordActivity.class);
        startActivity(intent);
        finish();
    }

}
