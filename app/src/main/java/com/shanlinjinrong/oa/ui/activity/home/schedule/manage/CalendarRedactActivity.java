package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.example.retrofit.model.ScheduleBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract.CalendarRedactActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter.CalendarRedactActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.SelectedTimeFragment;
import com.shanlinjinrong.pickerview.OptionsPickerView;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 编辑日历
 */
public class CalendarRedactActivity extends HttpBaseActivity<CalendarRedactActivityPresenter> implements CalendarRedactActivityContract.View {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_date)
    TextView      mTvDate;
    @BindView(R.id.ed_task_theme)
    EditText      mEdTaskTheme;
    @BindView(R.id.tv_task_date)
    TextView      mTvTaskDate;
    @BindView(R.id.ed_task_details)
    EditText      mEdTaskDetails;
    @BindView(R.id.btn_common_calendar)
    TextView      mBtnCommonCalendar;
    @BindView(R.id.cb_completes)
    CheckBox      mCbCompletes;
    @BindView(R.id.view_completes)
    View          mViewCompletes;

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

        SimpleDateFormat from = new SimpleDateFormat("yyyyMMdd");
        String date = from.format(new Date());

        switch (mItemType) {
            case Constants.WRITECALENDAR:
                if (date.equals(mYear + mMonth + mDate + "")) {
                    mTvDate.setText(mMonth + "月" + mDate + "日  今天");
                } else {
                    mTvDate.setText(mMonth + "月" + mDate + "日");
                }
                mTvTaskDate.setText(mStartTime + ":00" + "-" + mEndTime + ":00");
                break;
            case Constants.LOOKCALENDAR:

                mTvDate.setText(mDate);
                mTopView.setRightText("删除");
                mBtnCommonCalendar.setText("编辑");
                mCbCompletes.setVisibility(View.VISIBLE);
                mViewCompletes.setVisibility(View.VISIBLE);

                mEdTaskTheme.setEnabled(false);
                mTvTaskDate.setEnabled(false);
                mEdTaskDetails.setEnabled(false);

                mEdTaskTheme.setText(mTitle);
                mTvTaskDate.setText(mStartTime + ":00-" + mEndTime + ":00");
                mEdTaskDetails.setText(mContent);
                break;
            case Constants.MEETINGCALENDAR:
                break;
            default:
                break;
        }
    }

    private void initData() {
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
                mId = getIntent().getIntExtra(Constants.CALENDARID, -1);

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
                break;
            default:
                break;
        }

    }

    @OnClick({R.id.tv_task_date, R.id.btn_common_calendar})
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
                            jsonObject.put("taskDetail", mEdTaskDetails.getText().toString());
                            jsonObject.put("taskType", 2);
                            mPresenter.addCalendarSchedule(jsonObject.toString());
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        break;
                    //查看周历
                    case Constants.LOOKCALENDAR:
                        if ("编辑".equals(mBtnCommonCalendar.getText().toString())) {
                            mEdTaskTheme.setEnabled(true);
                            mTvTaskDate.setEnabled(true);
                            mEdTaskDetails.setEnabled(true);
                            mBtnCommonCalendar.setText("更改");
                        } else {
                            mEdTaskTheme.setEnabled(false);
                            mTvTaskDate.setEnabled(false);
                            mEdTaskDetails.setEnabled(false);
                            mBtnCommonCalendar.setText("编辑");
                        }
                        break;
                    //查看会议室
                    case Constants.MEETINGCALENDAR:
                        break;
                    default:
                        break;

                }
                break;
            //TODO 弹出时间选择框
            case R.id.tv_task_date:
                try {
                    if (mSelectedTimeFragment == null) {
                        mSelectedTimeFragment = new SelectedTimeFragment();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.SELECTEDPOSITION, getIntent().getIntExtra(Constants.SELECTEDPOSITION, 0));
                    bundle.putInt(Constants.CALENDARSTARTTIME, mStartTime);
                    bundle.putInt(Constants.CALENDARENDTIME, mEndTime);
                    mSelectedTimeFragment.setArguments(bundle);
                    mSelectedTimeFragment.show(getSupportFragmentManager(), "0");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SelectedWeekCalendarEvent event) {
        switch (event.getEvent()) {
            case Constants.SELECTEDTIME:
                mTaskStartTime = event.getStartTime();
                mTaskEndTime = event.getEndTime();
                mTvTaskDate.setText(mTaskStartTime + ":00" + "-" + mTaskEndTime + ":00");
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
        finish();
    }

    @Override
    public void addCalendarScheduleFailure(int errorCode, String errorMsg) {
        showToast("新增日程失败!");
    }

    @Override
    public void deleteCalendarScheduleSuccess() {
        showToast("删除日程成功");
        finish();
    }

    @Override
    public void deleteCalendarScheduleFailure(int errorCode, String errorMsg) {
        showToast("删除日程失败，请稍后重试");
    }

    @Override
    public void updateCalendarScheduleSuccess() {

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
}
