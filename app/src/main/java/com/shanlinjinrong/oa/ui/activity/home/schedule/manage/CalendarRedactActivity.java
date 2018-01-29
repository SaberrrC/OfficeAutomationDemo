package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract.CalendarRedactActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter.CalendarRedactActivityPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingInfoFillOutActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.SelectedTimeFragment;
import com.shanlinjinrong.pickerview.OptionsPickerView;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 编辑日历
 */
public class CalendarRedactActivity extends HttpBaseActivity<CalendarRedactActivityPresenter> implements CalendarRedactActivityContract.View, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_date)
    TextView      mTvDate;
    @BindView(R.id.ed_task_theme)
    EditText      mEdTaskTheme;
    @BindView(R.id.ed_task_details)
    EditText      mEdTaskDetails;
    @BindView(R.id.btn_common_calendar)
    TextView      mBtnCommonCalendar;
    @BindView(R.id.cb_completes)
    CheckBox      mCbCompletes;
    @BindView(R.id.view_completes)
    View          mViewCompletes;
    @BindView(R.id.tv_task_address)
    EditText      mTvTaskAddress;
    @BindView(R.id.tv_task_end_date)
    TextView      mTvTaskEndDate;
    @BindView(R.id.tv_task_start_date)
    TextView      mTvTaskStartDate;

    private String               mDate;
    private int                  mStartTime;
    private int                  mEndTime;
    private String               mTitle;
    private String               mContent;
    private String               mYear;
    private String               mMonth;
    private int                  mItemType;
    private OptionsPickerView    beginTimeView;
    private SelectedTimeFragment mSelectedTimeFragment;
    private String               mTaskStartTime;
    private String               mTaskEndTime;
    private int                  mId;
    private int                  mStatus;
    private String               mOldTheme;
    private String               mOldDate;
    private String               mOldDetails;
    private boolean              mIsFirst, mIsCheckBox;
    private int    mTaskId;
    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_redact);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initView() {
        mTopView.getRightView().setOnClickListener(view -> {
            switch (mItemType) {
                //删除 返回
                case Constants.WRITECALENDAR:
                    finish();
                    break;
                //删除 日程
                case Constants.LOOKCALENDAR:
                    mPresenter.deleteCalendarSchedule(mId);
                    break;
                case Constants.MEETINGCALENDAR:
                    break;
                default:
                    break;
            }
        });

        if (mStatus == 1) {
            mCbCompletes.setChecked(true);
            mCbCompletes.setEnabled(false);
            mBtnCommonCalendar.setVisibility(View.GONE);
        }

        mCbCompletes.setOnCheckedChangeListener(this);

        SimpleDateFormat from = new SimpleDateFormat("yyyyMMdd");
        String date = from.format(new Date());

        switch (mItemType) {

            case Constants.WRITECALENDAR:
                mCbCompletes.setVisibility(View.GONE);
                mViewCompletes.setVisibility(View.GONE);
                if (date.equals(mYear + mMonth + mDate + "")) {
                    mTvDate.setText(mMonth + "月" + mDate + "日  今天");
                } else {
                    mTvDate.setText(mMonth + "月" + mDate + "日");
                }
                mTvTaskStartDate.setText(mStartTime + ":00");
                mTvTaskEndDate.setText(mEndTime + ":00");
                break;
            case Constants.LOOKCALENDAR:
//                if (mAddress == null) {
//                    mAddress = " ";
//                }
//                if (mContent == null) {
//                    mContent = "暂无";
//                }

                mTvDate.setText(mDate);
                mTopView.setRightText("删除");
                mBtnCommonCalendar.setText("编辑");
                mCbCompletes.setVisibility(View.VISIBLE);
                mViewCompletes.setVisibility(View.VISIBLE);

                mEdTaskTheme.setEnabled(false);
                mTvTaskStartDate.setEnabled(false);
                mTvTaskEndDate.setEnabled(false);
                mEdTaskDetails.setEnabled(false);
                mTvTaskAddress.setEnabled(false);

                mTvTaskAddress.setText(mAddress);
                mEdTaskTheme.setText(mTitle);
                mTvTaskStartDate.setText(mStartTime + ":00");
                mTvTaskEndDate.setText(mEndTime + ":00");
                mEdTaskDetails.setText(mContent);
                break;
            case Constants.MEETINGCALENDAR:
//                if (mAddress == null) {
//                    mAddress = " ";
//                }
//                if (mContent == null) {
//                    mContent = "暂无";
//                }

                mTvDate.setText(mDate);
                mBtnCommonCalendar.setText("查看详情");
                mTopView.setAppTitle("普通会议");
                mBtnCommonCalendar.setBackgroundColor(getResources().getColor(R.color.blue_69B0F2));
                mCbCompletes.setVisibility(View.GONE);
                mTopView.setRightText("");
                mViewCompletes.setVisibility(View.GONE);

                mEdTaskTheme.setEnabled(false);
                mTvTaskStartDate.setEnabled(false);
                mTvTaskEndDate.setEnabled(false);
                mEdTaskDetails.setEnabled(false);
                mTvTaskAddress.setEnabled(false);


                mTvTaskAddress.setBackground(null);
                mEdTaskTheme.setBackground(null);
                mTvTaskStartDate.setBackground(null);
                mTvTaskEndDate.setBackground(null);
                mEdTaskDetails.setBackground(null);

                mTvTaskAddress.setText(mAddress);
                mEdTaskTheme.setText(mTitle);
                mTvTaskStartDate.setText(mStartTime + ":00");
                mTvTaskEndDate.setText(mEndTime + ":00");
                mEdTaskDetails.setText(mContent);
                break;
            default:
                break;
        }
    }

    private void initData() {
        mIsFirst = true;
        mItemType = getIntent().getIntExtra(Constants.CALENDARTYPE, -1);
        switch (mItemType) {
            //编辑周历
            case Constants.WRITECALENDAR:
                mYear = getIntent().getStringExtra(Constants.CALENDARYEAR);
                mMonth = getIntent().getStringExtra(Constants.CALENDARMONTH);
                mDate = getIntent().getStringExtra(Constants.CALENDARDATE);
                mStartTime = getIntent().getIntExtra(Constants.CALENDARSTARTTIME, 9);
                mEndTime = getIntent().getIntExtra(Constants.CALENDARENDTIME, 10);

                if (mStartTime == 9) {
                    mTaskStartTime = "0" + mStartTime;
                    mTaskEndTime = mEndTime + "";
                } else {
                    mTaskStartTime = mStartTime + "";
                    mTaskEndTime = mEndTime + "";
                }
                break;
            //查看周历
            case Constants.LOOKCALENDAR:
                mTitle = getIntent().getStringExtra(Constants.CALENDARTITLE);
                mContent = getIntent().getStringExtra(Constants.CALENDARCONTENT);
                mStartTime = getIntent().getIntExtra(Constants.CALENDARSTARTTIME, 9);
                mEndTime = getIntent().getIntExtra(Constants.CALENDARENDTIME, 10);
                mDate = getIntent().getStringExtra(Constants.CALENDARDATE);
                mAddress = getIntent().getStringExtra(Constants.CALENDARADDRESS);
                mId = getIntent().getIntExtra(Constants.CALENDARID, -1);
                mStatus = getIntent().getIntExtra(Constants.CALENDARSTATUS, 0);
                if (mStartTime == 9) {
                    mTaskStartTime = "0" + mStartTime;
                    mTaskEndTime = mEndTime + "";
                } else {
                    mTaskStartTime = mStartTime + "";
                    mTaskEndTime = mEndTime + "";
                }
                break;
            //查看会议室
            case Constants.MEETINGCALENDAR:
                mTitle = getIntent().getStringExtra(Constants.CALENDARTITLE);
                mContent = getIntent().getStringExtra(Constants.CALENDARCONTENT);
                mDate = getIntent().getStringExtra(Constants.CALENDARDATE);
                mAddress = getIntent().getStringExtra(Constants.CALENDARADDRESS);
                mStartTime = getIntent().getIntExtra(Constants.CALENDARSTARTTIME, 9);
                mEndTime = getIntent().getIntExtra(Constants.CALENDARENDTIME, 10);

                mTaskId = getIntent().getIntExtra(Constants.SELECTEDTASTID, -1);
                if (mStartTime == 9) {
                    mTaskStartTime = "0" + mStartTime;
                    mTaskEndTime = mEndTime + "";
                } else {
                    mTaskStartTime = mStartTime + "";
                    mTaskEndTime = mEndTime + "";
                }
                break;
            default:
                break;
        }

    }

    @OnClick({R.id.tv_task_end_date, R.id.btn_common_calendar, R.id.tv_task_start_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_common_calendar:
                switch (mItemType) {
                    //编辑周历
                    case Constants.WRITECALENDAR:
                        try {
                            if ("".equals(mEdTaskTheme.getText().toString())) {
                                Toast.makeText(this, "任务主题不能为空！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONObject jsonObject = new JSONObject();
                            String taskDate = mYear + "-" + mMonth + "-" + mDate;
                            jsonObject.put("taskDate", taskDate);
                            jsonObject.put("taskTheme", mEdTaskTheme.getText().toString());
                            jsonObject.put("startTime", taskDate + " " + mTaskStartTime + ":00:00");
                            jsonObject.put("endTime", taskDate + " " + mTaskEndTime + ":00:00");
                            jsonObject.put("taskType", 2);

                            if (!TextUtils.isEmpty(mEdTaskDetails.getText().toString().trim())) {
                                jsonObject.put("taskDetail", mEdTaskDetails.getText().toString());
                            }
                            if (!TextUtils.isEmpty(mTvTaskAddress.getText().toString().trim())) {
                                jsonObject.put("address", mTvTaskAddress.getText().toString());
                            }

                            mPresenter.addCalendarSchedule(jsonObject.toString());
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        break;
                    //查看周历
                    case Constants.LOOKCALENDAR:
//                        if ("编辑".equals(mBtnCommonCalendar.getText().toString())) {
//                            mEdTaskTheme.setEnabled(true);
//                            mTvTaskDate.setEnabled(true);
//                            mEdTaskDetails.setEnabled(true);
//                            mTvTaskAddress.setEnabled(true);
//
//                            mBtnCommonCalendar.setText("更新");
//
//                            if (mIsFirst) {
//                                mIsFirst = false;
//                                mOldTheme = mEdTaskTheme.getText().toString();
//                                mOldDate = mTvTaskDate.getText().toString();
//                                mOldDetails = mEdTaskDetails.getText().toString();
//                            }
//                        } else {
//                            try {
//                                JSONObject jsonObject = new JSONObject();
//
//
//                                if (!mOldTheme.equals(mEdTaskTheme.getText().toString().trim())) {
//                                    jsonObject.put("taskTheme", mEdTaskTheme.getText().toString().trim());
//                                }
//
//                                if (!mOldDate.equals(mTvTaskDate.getText().toString().trim())) {
//                                    if ("9".equals(mTaskStartTime)) {
//                                        mTaskStartTime = "09";
//                                    }
//                                    jsonObject.put("startTime", mTaskStartTime + ":00:00");
//                                    jsonObject.put("endTime", mTaskEndTime + ":00:00");
//                                }
//
//                                if (!mOldDetails.equals(mEdTaskDetails.getText().toString().trim())) {
//                                    jsonObject.put("taskDetail", mEdTaskDetails.getText().toString().trim());
//                                }
//
//                                String json = jsonObject.toString();
//
//                                if ("{}".equals(json)) {
//                                    Toast.makeText(this, "日程无变更，更新失败", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    jsonObject.put("id", mId);
//                                    String calendar = jsonObject.toString();
//                                    mPresenter.updateCalendarSchedule(calendar);
//                                }
//                            } catch (Throwable e) {
//                                e.printStackTrace();
//                            }
//
//                            mEdTaskTheme.setEnabled(false);
//                            mTvTaskDate.setEnabled(false);
//                            mEdTaskDetails.setEnabled(false);
//                            mTvTaskAddress.setEnabled(false);
//                            mBtnCommonCalendar.setText("编辑");
//                        }
                        break;
                    //查看会议室
                    case Constants.MEETINGCALENDAR:

                        Intent intent = new Intent(this, MeetingInfoFillOutActivity.class);
                        intent.putExtra("isWriteMeetingInfo", false);
                        intent.putExtra("id", mTaskId);
                        startActivity(intent);
                        break;
                    default:
                        break;

                }
                break;
            //TODO 弹出时间选择框
            case R.id.tv_task_start_date:
                try {
                    showDate(true);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_task_end_date:
                try {
                    showDate(false);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void showDate(boolean start) {
        if (mSelectedTimeFragment == null) {
            mSelectedTimeFragment = new SelectedTimeFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.SELECTEDPOSITION, getIntent().getIntExtra(Constants.SELECTEDPOSITION, 0));
        bundle.putInt(Constants.CALENDARSTARTTIME, mStartTime);
        bundle.putInt(Constants.CALENDARENDTIME, mEndTime);
        bundle.putBoolean("isStart", start);
        mSelectedTimeFragment.setArguments(bundle);
        mSelectedTimeFragment.show(getSupportFragmentManager(), "0");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SelectedWeekCalendarEvent event) {
        switch (event.getEvent()) {
            case Constants.SELECTEDTIME:
                boolean isStart = event.isStart();
                if (isStart) {
                    int startHour = event.getStartHour();
                    int startMin = event.getStartMin();
                    StringBuffer startTime = new StringBuffer();

                    if (startHour == 9) {
                        startTime.append("0").append(startHour).append(":");
                    } else {
                        startTime.append(startHour).append(":");
                    }

                    if (startMin < 10) {
                        startTime.append("0").append(startMin);
                    } else {
                        startTime.append(startMin);
                    }

                    mTvTaskStartDate.setText(startTime);
                } else {
                    int endHour = event.getEndHour();
                    int endMin = event.getEndMin();
                    StringBuffer endTime = new StringBuffer();

                    endTime.append(endHour).append(":");

                    if (endMin < 10) {
                        endTime.append("0").append(endMin);
                    } else {
                        endTime.append(endMin);
                    }

                    mTvTaskEndDate.setText(endTime);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void hideLoading() {
        hideLoadingView();
    }

    @Override
    public void addCalendarScheduleSuccess() {
        showToast("新增日程成功");
        setResult(-1);
        finish();
    }

    @Override
    public void addCalendarScheduleFailure(int errorCode, String errorMsg) {
        showToast("新增日程失败!");
    }

    @Override
    public void deleteCalendarScheduleSuccess() {
        showToast("删除日程成功");
        setResult(-1);
        finish();
    }

    @Override
    public void deleteCalendarScheduleFailure(int errorCode, String errorMsg) {
        showToast("删除日程失败，请稍后重试");
    }

    @Override
    public void updateCalendarScheduleSuccess() {
        setResult(-1);
        if (mIsCheckBox) {
            finish();
        }
        showToast("更新日程成功");

    }

    @Override
    public void updateCalendarScheduleFailure(int errorCode, String errorMsg) {
        showToast("更新日程失败，请稍后重试");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            try {
                mIsCheckBox = true;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", mId);
                jsonObject.put("status", 1);
                String calendar = jsonObject.toString();
                mPresenter.updateCalendarSchedule(calendar);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
