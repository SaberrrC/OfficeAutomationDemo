package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.ui.activity.home.schedule.SelectJoinPeopleActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRecordInfo;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingInfoFillOutActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingInfoFillOutActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 预订会议室 确认
 */
public class MeetingInfoFillOutActivity extends HttpBaseActivity<MeetingInfoFillOutActivityPresenter> implements MeetingInfoFillOutActivityContract.View, CompoundButton.OnCheckedChangeListener {
    private static final int ADD_JOIN_PEOPLE = 100;

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_meeting_date)
    TextView mTvMeetingDate;
    @BindView(R.id.tv_meeting_name)
    TextView mTvMeetingName;
    @BindView(R.id.tv_meeting_person_number)
    TextView mTvMeetingPersonNumber;
    @BindView(R.id.tv_meeting_receive_person)
    TextView mTvMeetingReceivePerson;
    @BindView(R.id.tv_meeting_device)
    TextView mTvMeetingDevice;
    @BindView(R.id.rb_is_meeting_invite)
    RadioButton mRbIsMeetingInvite;
    @BindView(R.id.ed_meeting_content)
    EditText mEdMeetingContent;
    @BindView(R.id.ed_meeting_theme)
    EditText mEdMeetingTheme;
    @BindView(R.id.ed_meeting_person)
    EditText mEdMeetingPerson;
    @BindView(R.id.cb_email)
    CheckBox mCbEmail;
    @BindView(R.id.cb_messages)
    CheckBox mCbMessages;
    @BindView(R.id.btn_meeting_info_complete)
    TextView mBtnMeetingInfoComplete;
    @BindView(R.id.tv_receive_person)
    TextView mTvReceivePerson;
    @BindView(R.id.tv_meeting_invite)
    TextView mTvMeetingInvite;
    @BindView(R.id.tv_is_meeting_invite)
    TextView mTvIsMeetingInvite;
    @BindView(R.id.tv_meeting_theme)
    TextView mTvMeetingTheme;
    @BindView(R.id.tv_meeting_person)
    TextView mTvMeetingPerson;
    @BindView(R.id.ll_meeting_person)
    LinearLayout mLlMeetingPerson;
    @BindView(R.id.ll_meeting_theme)
    LinearLayout mLlMeetingTheme;
    @BindView(R.id.tv_red_dot1)
    TextView mTvRedDot1;
    @BindView(R.id.tv_red_dot2)
    TextView mTvRedDot2;
    @BindView(R.id.tv_red_dot3)
    TextView mTvRedDot3;
    @BindView(R.id.iv_add_contacts)
    ImageView mAddContacts;
    @BindView(R.id.ll_meeting_title)
    LinearLayout mLlMeetingTitle;
    @BindView(R.id.ll_invite_mode)
    LinearLayout mLlInviteMode;
    @BindView(R.id.ll_meeting_personnel)
    LinearLayout mLlMeetingPersonnel;
    @BindView(R.id.ll_is_launch_meeting)
    LinearLayout mLlIsLaunchMeeting;
    @BindView(R.id.tv_rb_is_meeting_invite)
    TextView mTvRbIsMeetingInvite;

    private String mEndDate;
    private String mBeginDate;
    private String mMeetingName;
    private String mSendType;
    private int mRoomId;
    private String mHoursOfUse;
    private boolean isWriteMeetingInfo;
    private ArrayList<Child> contactsList = new ArrayList<>(); //邀请人参会人
    private String mStartTime;
    private String mEndTime;
    private int mId;
    private String mUid = "";
    private boolean mModifyMeeting;

    private String currentStartTime;
    private String currentEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_info_fill_out);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initListener();
        initData();
        initView();
    }

    private void initListener() {
        //下单重复点击设置
        RxView.clicks(mBtnMeetingInfoComplete).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (mModifyMeeting) {
//                        modifyMeetingState();
                    return;
                } else if (!getIntent().getBooleanExtra("isMeetingRecord", false)) {
                    addMeetingParams();
                } else {
                    Intent intent = new Intent(MeetingInfoFillOutActivity.this, MeetingPredetermineRecordActivity.class);
                    intent.putExtra("id", mId);
                    intent.putExtra("modifyMeeting", true);
                    intent.putExtra("isWriteMeetingInfo", false);
                    intent.putExtra("isMeetingPast", getIntent().getBooleanExtra("isMeetingPast", false));
                    intent.putExtra("roomId", getIntent().getIntExtra("roomId", -1));
                    intent.putExtra("startTime", currentStartTime);
                    intent.putExtra("endTime", currentEndTime);
                    intent.putExtra("start_time", mStartTime);
                    intent.putExtra("meeting_name", mTvMeetingName.getText().toString());
                    intent.putExtra("end_time", mEndTime);
                    startActivity(intent);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
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
        backTip();
    }

    private void initReadView() {
        mTopView.setAppTitle("预定详情");
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

        if (!getIntent().getBooleanExtra("isMeetingPast", false)) {
            mBtnMeetingInfoComplete.setEnabled(false);
        } else {
            mBtnMeetingInfoComplete.setEnabled(true);
        }
        mBtnMeetingInfoComplete.setText("调期");
        mCbEmail.setEnabled(false);
        mCbMessages.setEnabled(false);
        mAddContacts.setClickable(false);
        mEdMeetingContent.setEnabled(false);

        mId = getIntent().getIntExtra("id", -1);
        if (mId != -1) {
            mPresenter.lookMeetingRooms(mId);
        }

        if (getIntent().getBooleanExtra("isMeetingPast", false)) {
            mTopView.setRightText("取消");
            mTopView.getRightView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBackTip("您确定要取消会议", "确定", "取消", true);
                }
            });
        }
    }

    private void initWriteView() {
        mTopView.setRightText("");
        mTopView.setAppTitle("选择会议室");
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
        mEdMeetingTheme.setEnabled(false);
        mEdMeetingPerson.setEnabled(false);
        mCbEmail.setEnabled(false);
        mCbMessages.setEnabled(false);
        mAddContacts.setClickable(false);
        mRbIsMeetingInvite.setOnCheckedChangeListener(this);

    }

    private void initData() {
        mBeginDate = getIntent().getStringExtra("beginDate");
        mEndDate = getIntent().getStringExtra("endDate");
        mHoursOfUse = getIntent().getStringExtra("hoursOfUse");
        mMeetingName = getIntent().getStringExtra("meetingName");
        mRoomId = getIntent().getIntExtra("roomId", 0);
        mModifyMeeting = getIntent().getBooleanExtra("modifyMeeting", false);
        String meetingPeopleNumber = getIntent().getStringExtra("meetingPeopleNumber");
        String meetingDevice = getIntent().getStringExtra("meetingDevice");
        mStartTime = getIntent().getStringExtra("start_time");
        mEndTime = getIntent().getStringExtra("end_time");
        if (mBeginDate != null && mEndDate != null) {
            mTvMeetingDate.setText(mBeginDate + " -- " + mEndDate);
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


    @OnClick({R.id.iv_add_contacts, R.id.tv_rb_is_meeting_invite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_contacts:
                Intent intent = new Intent(this, SelectJoinPeopleActivity.class);
                intent.putParcelableArrayListExtra("selectedContacts", contactsList);
                startActivityForResult(intent, ADD_JOIN_PEOPLE);
                break;
            case R.id.tv_rb_is_meeting_invite:
                mRbIsMeetingInvite.setChecked(false);
                break;
        }
    }

    //会议调期
    private void modifyMeetingState() {
        HttpParams httpParams = new HttpParams();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("start_time", mStartTime);
            jsonObject.put("end_time", mEndTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpParams.putJsonParams(jsonObject.toString());
//        mPresenter.modifyMeetingRooms(mId, httpParams);
    }

    //添加会议参数
    private void addMeetingParams() {
        if (mRbIsMeetingInvite.isChecked()) {
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
        }
        HttpParams httpParams = new HttpParams();
        httpParams.put("room_id", mRoomId);
        httpParams.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        if (mEdMeetingContent.getText().toString().trim().equals("")) {
            httpParams.put("content", "暂无");
        } else {
            httpParams.put("content", mEdMeetingContent.getText().toString());
        }
        httpParams.put("start_time", mStartTime);
        httpParams.put("end_time", mEndTime);

        if (mRbIsMeetingInvite.isChecked()) {
            httpParams.put("part_uid", mUid);
            httpParams.put("title", mEdMeetingTheme.getText().toString());
            if (mCbEmail.isChecked() && mCbMessages.isChecked()) {
                mSendType = "邮件,消息";
            } else if (mCbMessages.isChecked()) {
                mSendType = "消息";
            } else if (mCbEmail.isChecked()) {
                mSendType = "邮件";
            }
        } else {
            httpParams.put("part_uid", "");
            httpParams.put("title", "");
        }

        httpParams.put("send_type", mSendType + "");
        mPresenter.addMeetingRooms(httpParams);
    }

    //判断是否禁用输入框
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rb_is_meeting_invite:
                if (mRbIsMeetingInvite.isChecked()) {
                    mEdMeetingTheme.setEnabled(true);
                    mEdMeetingPerson.setEnabled(false);
                    mCbEmail.setEnabled(true);
                    mCbMessages.setEnabled(true);
                    mAddContacts.setClickable(true);
                    mTvRbIsMeetingInvite.setVisibility(View.VISIBLE);
                } else {
                    mEdMeetingTheme.setEnabled(false);
                    mEdMeetingPerson.setEnabled(false);
                    mCbEmail.setEnabled(false);
                    mCbMessages.setEnabled(false);
                    mAddContacts.setClickable(false);
                    mTvRbIsMeetingInvite.setVisibility(View.GONE);
                }
                break;
        }
    }

    //添加参会人
    private void addCopyPersonOperate(Intent data) {
        this.contactsList.clear();
        ArrayList<Child> contacts = data.getParcelableArrayListExtra("contacts");
        contactsList.addAll(contacts);
        String person = "";
        for (int i = 0; i < contactsList.size(); i++) {
            if (i == contactsList.size() - 1) {
                person += contactsList.get(i).getUsername();
                mUid += contactsList.get(i).getUid();
            } else {
                person += contactsList.get(i).getUsername() + ",";
                mUid += contactsList.get(i).getUid() + ",";
            }

        }
        mEdMeetingPerson.setText(person);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ADD_JOIN_PEOPLE) {
            mUid = "";
            mEdMeetingPerson.setText("");
            addCopyPersonOperate(data);
        }
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
    }

    @Override
    public void requestNetworkError() {

    }

    //添加会议成功跳转
    @Override
    public void addMeetingRoomsSuccess() {
        Intent intent = new Intent(this, MeetingReservationSucceedActivity.class);
        intent.putExtra("mMeetingDate", mStartTime.replace(" ", "  ") + " - " + mEndDate);
        intent.putExtra("mMeetingName", mMeetingName);
        intent.putExtra("mReservation", "您已经成功预定");
        startActivity(intent);
//        MeetingPredetermineRecordActivity.mRecordActivity.finish();
        EventBus.getDefault().post("finish");
        finish();
    }

    @Override
    public void addMeetingRoomsFailed(int errorCode, String strMsg) {
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.net_no_connection));
                break;
            default:
                if (!strMsg.equals(""))
                    Toast.makeText(this, strMsg, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //会议记录查看
    @Override
    public void lookMeetingRoomsSuccess(MeetingRecordInfo info) {
        try {
            mTvMeetingName.setText(info.getData().getRoomname());
            mTvMeetingPersonNumber.setText(info.getData().getNop());
            if (!mModifyMeeting) {
                currentStartTime = DateUtils.stringToDateTransform(info.getData().getStart_time(), "yyyy年MM月dd日  HH:mm");
                currentEndTime = DateUtils.stringToDateTransform(info.getData().getEnd_time(), "yyyy年MM月dd日  HH:mm");
                String startTime = DateUtils.stringToDateTransform(info.getData().getStart_time(), "MM月dd日  HH:mm");
                String endTime = DateUtils.stringToDateTransform(info.getData().getEnd_time(), "HH:mm");
                mTvMeetingDate.setText(startTime + " -- " + endTime);
            }
            mTvMeetingDevice.setText(info.getData().getDevice());
            mTvMeetingReceivePerson.setText(info.getData().getSend_user());
            mTvMeetingTheme.setText(info.getData().getTitle());
            List<MeetingRecordInfo.DataBean.PartNameBean> part_name = info.getData().getPart_name();
            String userName = "";
            for (int i = 0; i < part_name.size(); i++) {
                if (part_name.get(i).getUsername() != null)
                    userName += part_name.get(i).getUsername() + " ";
            }
            if (!userName.trim().equals("")) {
                mTvMeetingPerson.setText(userName);
            }
            switch (info.getData().getSend_type()) {
                case "邮件,消息":
                    mCbEmail.setChecked(true);
                    mCbMessages.setChecked(true);
                    break;
                case "邮件":
                    mCbEmail.setChecked(true);
                    break;
                case "消息":
                    mCbMessages.setChecked(true);
                    break;
            }
            mEdMeetingContent.setText(info.getData().getContent());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lookMeetingRoomsFailed(int errorCoe, String strMsg) {
        switch (errorCoe) {
            case -1:
                showToast(getString(R.string.net_no_connection));
                break;
            default:
                if (!strMsg.equals(""))
                    Toast.makeText(this, strMsg, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //删除会议
    @Override
    public void deleteMeetingRoomsSuccess() {
        Toast.makeText(this, "会议室取消成功!", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post("finish");
//        MeetingReservationRecordActivity.mRecordActivity.finish();
        Intent intent = new Intent(this, MeetingReservationRecordActivity.class);
        startActivity(intent);
        EventBus.getDefault().post("meetingDeleteSuccess");
        finish();
    }

    @Override
    public void deleteMeetingRoomsFailed(int errorCode, String strMsg) {
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.net_no_connection));
                break;
            default:
                if (!strMsg.equals(""))
                    Toast.makeText(this, strMsg, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isWriteMeetingInfo && (!mEdMeetingContent.getText().toString().trim().equals("") || !mEdMeetingPerson.getText().toString().trim().equals("")
                || !mEdMeetingTheme.getText().toString().trim().equals("") || mCbMessages.isChecked() || mCbEmail.isChecked())) {
            showBackTip("是否放弃选择会议室", "确定", "取消", false);
        } else {
            finish();
        }
    }

    /**
     * 验证内容为空的返回提示
     */
    public void showBackTip(String msg, final String posiStr, String negaStr, final boolean isCancelMeeting) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_editor, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(this,
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, posiStr,
                (dialog, which) -> {
                    if (isCancelMeeting) {
                        if (mId != -1) {
                            mPresenter.deleteMeetingRooms(mId);
                        }
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr,
                (dialog, which) -> {
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
    }

    //返回提示
    private void backTip() {
        mTopView.getLeftView().setOnClickListener(view -> {
            if (isWriteMeetingInfo && (!TextUtils.isEmpty(mEdMeetingContent.getText().toString().trim()) || !mEdMeetingPerson.getText().toString().trim().equals("")
                    || !mEdMeetingTheme.getText().toString().trim().equals("") || mCbMessages.isChecked() || mCbEmail.isChecked())) {
                showBackTip("是否放弃选择会议室", "确定", "取消", false);
            } else {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(MeetingInfoFillOutActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinish(String str) {
        if (str.equals("finish")) {
            finish();
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
