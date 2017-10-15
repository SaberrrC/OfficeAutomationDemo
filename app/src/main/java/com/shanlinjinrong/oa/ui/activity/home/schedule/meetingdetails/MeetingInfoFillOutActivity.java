package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingInfoFillOutActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingInfoFillOutActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.kymjs.kjframe.http.HttpParams;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预订会议室 确认
 */
public class MeetingInfoFillOutActivity extends HttpBaseActivity<MeetingInfoFillOutActivityPresenter> implements MeetingInfoFillOutActivityContract.View, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.tv_meeting_date)
    TextView mTvMeetingDate;
    @Bind(R.id.tv_meeting_name)
    TextView mTvMeetingName;
    @Bind(R.id.tv_meeting_person_number)
    TextView mTvMeetingPersonNumber;
    @Bind(R.id.tv_meeting_receive_person)
    TextView mTvMeetingReceivePerson;
    @Bind(R.id.tv_meeting_device)
    TextView mTvMeetingDevice;
    @Bind(R.id.rb_is_meeting_invite)
    RadioButton mRbIsMeetingInvite;
    @Bind(R.id.ed_meeting_content)
    EditText mEdMeetingContent;
    @Bind(R.id.ed_meeting_theme)
    EditText mEdMeetingTheme;
    @Bind(R.id.ed_meeting_person)
    EditText mEdMeetingPerson;
    @Bind(R.id.cb_email)
    CheckBox mCbEmail;
    @Bind(R.id.cb_messages)
    CheckBox mCbMessages;
    //@Bind(R.id.cb_sms)
    //CheckBox mCbSms;
    @Bind(R.id.btn_meeting_info_complete)
    TextView mBtnMeetingInfoComplete;
    @Bind(R.id.tv_receive_person)
    TextView mTvReceivePerson;
    @Bind(R.id.tv_meeting_invite)
    TextView mTvMeetingInvite;
    @Bind(R.id.tv_is_meeting_invite)
    TextView mTvIsMeetingInvite;
    @Bind(R.id.tv_meeting_theme)
    TextView mTvMeetingTheme;
    @Bind(R.id.tv_meeting_person)
    TextView mTvMeetingPerson;
    @Bind(R.id.ll_meeting_person)
    LinearLayout mLlMeetingPerson;
    @Bind(R.id.ll_meeting_theme)
    LinearLayout mLlMeetingTheme;
    @Bind(R.id.tv_red_dot1)
    TextView mTvRedDot1;
    @Bind(R.id.tv_red_dot2)
    TextView mTvRedDot2;
    @Bind(R.id.tv_red_dot3)
    TextView mTvRedDot3;
    @Bind(R.id.iv_add_contacts)
    ImageView mAddContacts;
    private String mEndDate;
    private String mBeginDate;
    private String mMeetingName;
    private String mSendType;
    private int mRoomId;
    private String mHoursOfUse;
    private boolean isWriteMeetingInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_info_fill_out);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initView() {
        isWriteMeetingInfo = getIntent().getBooleanExtra("isWriteMeetingInfo", true);
        if (isWriteMeetingInfo) {
            initWriteView();
        } else {
            initReadView();
        }
        mTopView.getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initReadView() {
        mTopView.setRightText("取消");
        mTvReceivePerson.setVisibility(View.GONE);
        mTvMeetingReceivePerson.setVisibility(View.GONE);
        mRbIsMeetingInvite.setVisibility(View.GONE);
        mTvIsMeetingInvite.setVisibility(View.GONE);
        mEdMeetingTheme.setVisibility(View.GONE);
        mLlMeetingPerson.setVisibility(View.GONE);
        mLlMeetingTheme.setVisibility(View.GONE);
        mEdMeetingPerson.setVisibility(View.GONE);
        mTvMeetingInvite.setVisibility(View.VISIBLE);
        mTvMeetingTheme.setVisibility(View.VISIBLE);
        mTvMeetingPerson.setVisibility(View.VISIBLE);
        mTvRedDot1.setVisibility(View.INVISIBLE);
        mTvRedDot2.setVisibility(View.INVISIBLE);
        mTvRedDot3.setVisibility(View.INVISIBLE);
        mBtnMeetingInfoComplete.setText("调期");
        mCbEmail.setEnabled(false);
        mCbMessages.setEnabled(false);
        mEdMeetingContent.setEnabled(false);
    }

    private void initWriteView() {
        mTopView.setRightText("");
        mTvReceivePerson.setVisibility(View.VISIBLE);
        mTvMeetingReceivePerson.setVisibility(View.VISIBLE);
        mRbIsMeetingInvite.setVisibility(View.VISIBLE);
        mTvIsMeetingInvite.setVisibility(View.VISIBLE);
        mEdMeetingTheme.setVisibility(View.VISIBLE);
        mEdMeetingPerson.setVisibility(View.VISIBLE);
        mLlMeetingPerson.setVisibility(View.VISIBLE);
        mLlMeetingTheme.setVisibility(View.VISIBLE);
        mTvRedDot1.setVisibility(View.VISIBLE);
        mTvRedDot2.setVisibility(View.VISIBLE);
        mTvRedDot3.setVisibility(View.VISIBLE);
        mTvMeetingInvite.setVisibility(View.GONE);
        mTvMeetingTheme.setVisibility(View.GONE);
        mTvMeetingPerson.setVisibility(View.GONE);
        mBtnMeetingInfoComplete.setText("完成");
        mBtnMeetingInfoComplete.setEnabled(false);
        mEdMeetingContent.setEnabled(false);
        mEdMeetingTheme.setEnabled(false);
        mEdMeetingPerson.setEnabled(false);
        mCbEmail.setEnabled(false);
        mCbMessages.setEnabled(false);
        mRbIsMeetingInvite.setOnCheckedChangeListener(this);
    }

    private void initData() {
        mBeginDate = getIntent().getStringExtra("beginDate");
        mEndDate = getIntent().getStringExtra("endDate");
        mHoursOfUse = getIntent().getStringExtra("hoursOfUse");
        mMeetingName = getIntent().getStringExtra("meetingName");
        mRoomId = getIntent().getIntExtra("roomId", 0);
        String meetingPeopleNumber = getIntent().getStringExtra("meetingPeopleNumber");
        String meetingDevice = getIntent().getStringExtra("meetingDevice");

        if (mHoursOfUse != null) {
            mTvMeetingDate.setText(mHoursOfUse);
        }

        if (mMeetingName != null) {
            mTvMeetingName.setText(mMeetingName);
        }

        if (meetingPeopleNumber != null) {
            mTvMeetingPersonNumber.setText(meetingPeopleNumber);
        }

        if (meetingDevice != null) {
            mTvMeetingDevice.setText(meetingDevice);
        }

        mTvMeetingReceivePerson.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME));
    }


    @OnClick({R.id.btn_meeting_info_complete, R.id.iv_add_contacts})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_meeting_info_complete:
                if (mEdMeetingTheme.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "会议主题不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mEdMeetingPerson.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "与议人员不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!mCbEmail.isChecked() && !mCbMessages.isChecked()) {
                    Toast.makeText(this, "请选择邀请方式", Toast.LENGTH_SHORT).show();
                    return;
                }

                HttpParams httpParams = new HttpParams();
                httpParams.put("room_id", mRoomId);
                httpParams.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
                httpParams.put("title", mTvMeetingName.getText().toString());
                httpParams.put("content", mEdMeetingContent.getText().toString());
                //TODO参会人待处理
                httpParams.put("part_uid", mEdMeetingPerson.getText().toString());
                //TODO 会议室开始时间跟结束时间
                httpParams.put("start_time", "2017-10-16 12:00");
                httpParams.put("end_time", "2017-10-16 13:00");

                if (mCbEmail.isChecked() && mCbMessages.isChecked()) {
                    mSendType = "邮件，消息";
                } else if (mCbMessages.isChecked()) {
                    mSendType = "消息";
                } else if (mCbEmail.isChecked()) {
                    mSendType = "邮件";
                }
                httpParams.put("send_type", mSendType);
                mPresenter.addMeetingRooms(httpParams);

                break;

            case R.id.iv_add_contacts:
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rb_is_meeting_invite:
                if (mRbIsMeetingInvite.isChecked()) {
                    mBtnMeetingInfoComplete.setEnabled(true);
                    mEdMeetingContent.setEnabled(true);
                    mEdMeetingTheme.setEnabled(true);
                    mEdMeetingPerson.setEnabled(true);
                    mCbEmail.setEnabled(true);
                    mCbMessages.setEnabled(true);
                    //mCbSms.setEnabled(true);
                } else {
                    mBtnMeetingInfoComplete.setEnabled(false);
                    mEdMeetingContent.setEnabled(false);
                    mEdMeetingTheme.setEnabled(false);
                    mEdMeetingPerson.setEnabled(false);
                    mCbEmail.setEnabled(false);
                    mCbMessages.setEnabled(false);
                    //mCbSms.setEnabled(false);
                }
                break;
        }
    }

    @Override
    public void uidNull(int code) {

    }

    @Override
    public void addMeetingRoomsSuccess() {

        Intent intent = new Intent(this, MeetingReservationSucceedActivity.class);
        intent.putExtra("mMeetingDate", DateUtils.getDateFormat("yyyy-MM-dd").format(new Date()) + "  " + mBeginDate + "-" + mEndDate);
        intent.putExtra("mMeetingName", mMeetingName);
        startActivity(intent);
        MeetingPredetermineRecordActivity.mRecordActivity.finish();
        finish();
    }

    @Override
    public void addMeetingRoomsFailed() {

    }
}
