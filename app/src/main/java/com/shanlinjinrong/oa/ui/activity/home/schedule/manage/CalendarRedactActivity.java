package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.SelectedTimeFragment;
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
    TextView mTvDate;
    @BindView(R.id.ed_task_theme)
    EditText mEdTaskTheme;
    @BindView(R.id.ed_task_details)
    EditText mEdTaskDetails;
    @BindView(R.id.btn_common_calendar)
    TextView mBtnCommonCalendar;
    @BindView(R.id.cb_completes)
    CheckBox mCbCompletes;
    @BindView(R.id.view_completes)
    View mViewCompletes;
    @BindView(R.id.tv_task_address)
    EditText mTvTaskAddress;
    @BindView(R.id.tv_task_end_date)
    TextView mTvTaskEndDate;
    @BindView(R.id.tv_task_start_date)
    TextView mTvTaskStartDate;
    @BindView(R.id.tv_task_theme)
    TextView tvTaskTheme;
    @BindView(R.id.tv_task_addresss)
    TextView tvTaskAddresss;
    @BindView(R.id.tv_task_details)
    TextView tvTaskDetails;

    private String mDate;
    private String mTitle;
    private String mContent;
    private String mYear;
    private String mMonth;
    private int mItemType;
    private SelectedTimeFragment mSelectedTimeFragment;
    private int mId;
    private int mStatus;
    private String mOldTheme;
    private String mOldStartTime;
    private String mOldEndTime;
    private String mOldAddress;
    private String mOldDetails;
    private boolean mIsFirst, mIsCheckBox;
    private int mTaskId;
    private String mAddress;
    private StringBuffer mTaskStartTime;
    private StringBuffer mTaskEndTime;
    private int mStartHour;
    private int mStartMin;
    private int mEndHour;
    private int mEndMin;
    InputMethodManager inputMethodManager;

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
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mTopView.getRightView().setOnClickListener(view -> {
            switch (mItemType) {
                //删除 返回
                case Constants.WRITECALENDAR:
                    hideSoftKeyboard();
                    break;
                //删除 日程
                case Constants.LOOKCALENDAR:
                    showDeleteTip("是否删除当前日程", "确认", "取消");
                    break;
                case Constants.MEETINGCALENDAR:
                    break;
                default:
                    break;
            }
        });
        mTopView.getLeftView().setOnClickListener(view -> {
            hideSoftKeyboard();
        });

        if (mStatus == 1) {
            mCbCompletes.setChecked(true);
            mCbCompletes.setEnabled(false);
            mBtnCommonCalendar.setEnabled(false);
            mBtnCommonCalendar.setBackgroundColor(getResources().getColor(R.color.FFDBDBDB));
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
                mTvTaskStartDate.setText(mTaskStartTime.toString());
                mTvTaskEndDate.setText(mTaskEndTime.toString());

                break;
            case Constants.LOOKCALENDAR:
                long time = DateUtils.getTimestampFromString(mDate, "yyyy-MM-dd");
                String lookDate = DateUtils.getDisplayMonthDay(time);
                mTvDate.setText(lookDate);
                mTopView.setRightText("删除");
                mTopView.setAppTitle("任务详情");
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
                mTvTaskStartDate.setText(mTaskStartTime.toString());
                mTvTaskEndDate.setText(mTaskEndTime.toString());
                mEdTaskDetails.setText(mContent);
                break;
            case Constants.MEETINGCALENDAR:
                long time1 = DateUtils.getTimestampFromString(mDate, "yyyy-MM-dd");
                String meetingDate = DateUtils.getDisplayMonthDay(time1);
                mTvDate.setText(meetingDate);
                mBtnCommonCalendar.setText("查看详情");
                mTopView.setAppTitle("普通会议");
                tvTaskTheme.setText("会议主题");
                tvTaskAddresss.setText("会议地点");
                tvTaskDetails.setText("会议详情");

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
                mTvTaskStartDate.setText(mTaskStartTime);
                mTvTaskEndDate.setText(mTaskEndTime);
                mEdTaskDetails.setText(mContent);
                break;
            default:
                break;
        }


    }


    private void initData() {
        mIsFirst = true;
        mTaskStartTime = new StringBuffer();

        mTaskEndTime = new StringBuffer();
        mItemType = getIntent().getIntExtra(Constants.CALENDARTYPE, -1);
        switch (mItemType) {
            //编辑周历
            case Constants.WRITECALENDAR:
                mYear = getIntent().getStringExtra(Constants.CALENDARYEAR);
                mMonth = getIntent().getStringExtra(Constants.CALENDARMONTH);
                mDate = getIntent().getStringExtra(Constants.CALENDARDATE);
                mStartHour = getIntent().getIntExtra(Constants.CALENDARSTARTHOUR, 9);
                mStartMin = getIntent().getIntExtra(Constants.CALENDARSTARTMIN, 0);

                mEndHour = getIntent().getIntExtra(Constants.CALENDARENDHOUR, 10);
                mEndMin = getIntent().getIntExtra(Constants.CALENDARENDMIN, 0);


                if (mStartHour == 9) {
                    mTaskStartTime.append("0").append(mStartHour).append(":");
                } else {
                    mTaskStartTime.append(mStartHour).append(":");
                }

                if (mStartMin < 10) {
                    mTaskStartTime.append("0").append(mStartMin);
                } else {
                    mTaskStartTime.append(mStartMin);
                }


                mTaskEndTime.append(mEndHour).append(":");

                if (mEndMin < 10) {
                    mTaskEndTime.append("0").append(mEndMin);
                } else {
                    mTaskEndTime.append(mEndMin);
                }
                break;
            //查看周历
            case Constants.LOOKCALENDAR:
                mTitle = getIntent().getStringExtra(Constants.CALENDARTITLE);
                mContent = getIntent().getStringExtra(Constants.CALENDARCONTENT);
                mDate = getIntent().getStringExtra(Constants.CALENDARDATE);
                mAddress = getIntent().getStringExtra(Constants.CALENDARADDRESS);
                mId = getIntent().getIntExtra(Constants.CALENDARID, -1);
                mStatus = getIntent().getIntExtra(Constants.CALENDARSTATUS, 0);

                mStartHour = getIntent().getIntExtra(Constants.CALENDARSTARTHOUR, 9);
                mStartMin = getIntent().getIntExtra(Constants.CALENDARSTARTMIN, 0);

                mEndHour = getIntent().getIntExtra(Constants.CALENDARENDHOUR, 10);
                mEndMin = getIntent().getIntExtra(Constants.CALENDARENDMIN, 0);


                if (mStartHour == 9) {
                    mTaskStartTime.append("0").append(mStartHour).append(":");
                } else {
                    mTaskStartTime.append(mStartHour).append(":");
                }

                if (mStartMin < 10) {
                    mTaskStartTime.append("0").append(mStartMin);
                } else {
                    mTaskStartTime.append(mStartMin);
                }


                mTaskEndTime.append(mEndHour).append(":");

                if (mEndMin < 10) {
                    mTaskEndTime.append("0").append(mEndMin);
                } else {
                    mTaskEndTime.append(mEndMin);
                }

                break;
            //查看会议室
            case Constants.MEETINGCALENDAR:
                mTitle = getIntent().getStringExtra(Constants.CALENDARTITLE);
                mContent = getIntent().getStringExtra(Constants.CALENDARCONTENT);
                mDate = getIntent().getStringExtra(Constants.CALENDARDATE);
                mAddress = getIntent().getStringExtra(Constants.CALENDARADDRESS);
                mTaskId = getIntent().getIntExtra(Constants.SELECTEDTASTID, 0);


                mStartHour = getIntent().getIntExtra(Constants.CALENDARSTARTHOUR, 9);
                mStartMin = getIntent().getIntExtra(Constants.CALENDARSTARTMIN, 0);

                mEndHour = getIntent().getIntExtra(Constants.CALENDARENDHOUR, 10);
                mEndMin = getIntent().getIntExtra(Constants.CALENDARENDMIN, 0);

                if (mStartHour == 9) {
                    mTaskStartTime.append("0").append(mStartHour).append(":");
                } else {
                    mTaskStartTime.append(mStartHour).append(":");
                }

                if (mStartMin < 10) {
                    mTaskStartTime.append("0").append(mStartMin);
                } else {
                    mTaskStartTime.append(mStartMin);
                }


                mTaskEndTime.append(mEndHour).append(":");

                if (mEndMin < 10) {
                    mTaskEndTime.append("0").append(mEndMin);
                } else {
                    mTaskEndTime.append(mEndMin);
                }

                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvTaskAddress.setTextColor(getResources().getColor(R.color.black));
        mEdTaskDetails.setTextColor(getResources().getColor(R.color.black));
    }

    @OnClick({R.id.tv_task_end_date, R.id.btn_common_calendar, R.id.tv_task_start_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_common_calendar:
                switch (mItemType) {
                    //编辑周历
                    case Constants.WRITECALENDAR:
                        try {
                            if ("".equals(mEdTaskTheme.getText().toString().trim())) {
                                Toast.makeText(this, "任务主题不能为空！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONObject jsonObject = new JSONObject();
                            String taskDate = mYear + "-" + mMonth + "-" + mDate;
                            jsonObject.put("taskDate", taskDate);
                            jsonObject.put("taskTheme", mEdTaskTheme.getText().toString());
                            jsonObject.put("startTime", taskDate + " " + mTvTaskStartDate.getText().toString() + ":00");
                            jsonObject.put("endTime", taskDate + " " + mTvTaskEndDate.getText().toString() + ":00");
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
                        if ("编辑".equals(mBtnCommonCalendar.getText().toString())) {
                            mEdTaskTheme.setEnabled(true);
                            mTvTaskStartDate.setEnabled(true);
                            mTvTaskEndDate.setEnabled(true);
                            mTvTaskAddress.setEnabled(true);
                            mEdTaskDetails.setEnabled(true);
                            mCbCompletes.setVisibility(View.GONE);
                            mViewCompletes.setVisibility(View.GONE);
                            mBtnCommonCalendar.setText("完成");

                            mOldTheme = mEdTaskTheme.getText().toString().trim();
                            mOldStartTime = mTvTaskStartDate.getText().toString().trim();
                            mOldEndTime = mTvTaskEndDate.getText().toString().trim();
                            mOldAddress = mTvTaskAddress.getText().toString().trim();
                            mOldDetails = mEdTaskDetails.getText().toString().trim();
                        } else {

                            if ("".equals(mEdTaskTheme.getText().toString().trim())) {
                                Toast.makeText(this, "任务主题不能为空！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                boolean isTheme = mOldTheme.equals(mEdTaskTheme.getText().toString().trim());
                                boolean isStart = mOldStartTime.equals(mTvTaskStartDate.getText().toString().trim());
                                boolean isEnd = mOldEndTime.equals(mTvTaskEndDate.getText().toString().trim());
                                boolean isAddress = mOldAddress.equals(mTvTaskAddress.getText().toString().trim());
                                boolean isDetails = mOldDetails.equals(mEdTaskDetails.getText().toString().trim());
                                if (isTheme && isStart && isEnd && isAddress && isDetails) {
                                    showToast("内容无变更，完成失败");
                                } else {
                                    JSONObject jsonObject = new JSONObject();
                                    if (!isTheme) {
                                        jsonObject.put("taskTheme", mEdTaskTheme.getText().toString().trim());
                                    }

                                    if (!isStart) {
                                        jsonObject.put("startTime", mDate + " " + mTvTaskStartDate.getText().toString().trim() + ":00");
                                    }

                                    if (!isEnd) {
                                        jsonObject.put("endTime", mDate + " " + mTvTaskEndDate.getText().toString().trim() + ":00");
                                    }

                                    if (!isAddress) {
                                        jsonObject.put("address", mTvTaskAddress.getText().toString().trim());
                                    }

                                    if (!isDetails) {
                                        jsonObject.put("taskDetail", mEdTaskDetails.getText().toString().trim());
                                    }

                                    jsonObject.put("id", mId);
                                    String calendar = jsonObject.toString();
                                    mPresenter.updateCalendarSchedule(calendar);
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    //查看会议室
                    case Constants.MEETINGCALENDAR:

                        Intent intent = new Intent(this, MeetingInfoFillOutActivity.class);
                        intent.putExtra("isWriteMeetingInfo", false);
                        intent.putExtra("id", mTaskId);
                        intent.putExtra("isCalendar", true);
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

        if (start) {
            bundle.putInt(Constants.CALENDARSTARTHOUR, mStartHour);
            bundle.putInt(Constants.CALENDARSTARTMIN, mStartMin);
        } else {
            bundle.putInt(Constants.CALENDARENDHOUR, mEndHour);
            bundle.putInt(Constants.CALENDARENDMIN, mEndMin);
        }
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

                    if (startHour > mEndHour) {
                        showToast("开始时间无法大于结束时间");
                        return;
                    }

                    if (startHour == mEndHour) {
                        if (startMin > mEndMin) {
                            showToast("开始时间无法大于结束时间");
                            return;
                        }
                        if (startMin == mEndMin) {
                            showToast("开始时间不可以等于结束时间");
                            return;
                        }
                    }
                    mStartHour = event.getStartHour();
                    mStartMin = event.getStartMin();
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

                    if (endHour < mStartHour) {
                        showToast("结束时间无法小于开始时间");
                        return;
                    }

                    if (endHour == mStartHour) {
                        if (endMin < mStartMin) {
                            showToast("结束时间无法小于开始时间");
                            return;
                        }
                        if (endMin == mStartMin) {
                            showToast("结束时间不可以等于开始时间");
                            return;
                        }
                    }
                    mEndHour = event.getEndHour();
                    mEndMin = event.getEndMin();
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
        EventBus.getDefault().post(new SelectedWeekCalendarEvent("", "upDateCalendarScheduleSuccess"));
        setResult(-1);
        finish();
    }

    @Override
    public void addCalendarScheduleFailure(int errorCode, String errorMsg) {
        if ("auth error".equals(errorMsg)) {
            catchWarningByCode(errorMsg);
            return;
        }
        if (errorCode == -1) {
            showToast(getString(R.string.net_no_connection));
            return;
        }
        showToast("新增日程失败!");
    }

    @Override
    public void deleteCalendarScheduleSuccess() {
        showToast("删除日程成功");
        EventBus.getDefault().post(new SelectedWeekCalendarEvent("", "upDateCalendarScheduleSuccess"));
        setResult(-1);
        finish();
    }

    @Override
    public void deleteCalendarScheduleFailure(int errorCode, String errorMsg) {

        if ("auth error".equals(errorMsg)) {
            catchWarningByCode(errorMsg);
            return;
        }
        if (errorCode == -1) {
            showToast(getString(R.string.net_no_connection));
            return;
        }
        showToast("删除日程失败，请稍后重试");

    }

    @Override
    public void updateCalendarScheduleSuccess() {
        EventBus.getDefault().post(new SelectedWeekCalendarEvent("", "upDateCalendarScheduleSuccess"));
        if (mCbCompletes.isChecked()) {
            mCbCompletes.setEnabled(false);
            mBtnCommonCalendar.setEnabled(false);
            mBtnCommonCalendar.setBackgroundColor(getResources().getColor(R.color.FFDBDBDB));
            showToast("日程已完成");
        } else {
            mOldTheme = mEdTaskTheme.getText().toString().trim();
            mOldStartTime = mTvTaskStartDate.getText().toString().trim();
            mOldEndTime = mTvTaskEndDate.getText().toString().trim();
            mOldAddress = mTvTaskAddress.getText().toString().trim();
            mOldDetails = mEdTaskDetails.getText().toString().trim();

            mEdTaskTheme.setEnabled(false);
            mTvTaskStartDate.setEnabled(false);
            mTvTaskEndDate.setEnabled(false);
            mTvTaskAddress.setEnabled(false);
            mEdTaskDetails.setEnabled(false);
            mCbCompletes.setVisibility(View.VISIBLE);
            mViewCompletes.setVisibility(View.VISIBLE);
            mBtnCommonCalendar.setText("编辑");
            showToast("日程更新成功");
        }
        setResult(-1);
        finish();
    }

    @Override
    public void updateCalendarScheduleFailure(int errorCode, String errorMsg) {
        if ("auth error".equals(errorMsg)) {
            catchWarningByCode(errorMsg);
            return;
        }
        if (errorCode == -1) {
            showToast(getString(R.string.net_no_connection));
            return;
        }
        showToast("更新日程失败，请稍后重试");
    }

    public void showDeleteTip(String msg, final String posiStr, String negaStr) {
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
                    mPresenter.deleteCalendarSchedule(mId);
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

    public void showCompleteTip(String msg, final String posiStr, String negaStr) {
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
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr,
                (dialog, which) -> {
                    mCbCompletes.setChecked(false);
                });
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
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
                showCompleteTip("是否完成此次日程安排", "确认", "取消");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 隐藏软键盘 finish页面
     */
    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
        finish();
    }
}
