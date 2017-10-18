package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingBookItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingPredetermineContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingPredeterminePresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.utils.DeviceUtil;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static butterknife.ButterKnife.bind;
import static com.shanlinjinrong.oa.utils.DateUtils.longToDateString;
import static com.shanlinjinrong.oa.utils.DateUtils.stringToDate;

/**
 * 选择预约时间
 */
public class MeetingPredetermineRecordActivity extends HttpBaseActivity<MeetingPredeterminePresenter> implements MeetingPredetermineContract.View, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.selected_meeting_date1)
    CheckBox mSelectedMeetingDate1;
    @Bind(R.id.selected_meeting_date2)
    CheckBox mSelectedMeetingDate2;
    @Bind(R.id.selected_meeting_date3)
    CheckBox mSelectedMeetingDate3;
    @Bind(R.id.selected_meeting_date4)
    CheckBox mSelectedMeetingDate4;
    @Bind(R.id.selected_meeting_date5)
    CheckBox mSelectedMeetingDate5;
    @Bind(R.id.selected_meeting_date6)
    CheckBox mSelectedMeetingDate6;
    @Bind(R.id.selected_meeting_date7)
    CheckBox mSelectedMeetingDate7;
    @Bind(R.id.selected_meeting_date8)
    CheckBox mSelectedMeetingDate8;
    @Bind(R.id.selected_meeting_date9)
    CheckBox mSelectedMeetingDate9;
    @Bind(R.id.btn_meeting_info_complete)
    TextView mBtnMeetingInfoComplete;
    @Bind(R.id.ll_day_selector)
    LinearLayout mLlDaySelector;
    @Bind(R.id.ll_month_selector)
    LinearLayout mLlMonthSelector;
    @Bind(R.id.ll_date_layout)
    LinearLayout mDateLayout;
    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.tv_month)
    TextView mTvMonth;

    @Bind(R.id.tv_day)
    TextView mTvDay;

    @Bind(R.id.tv_week)
    TextView mTvWeek;

    private int DateIndex;
    private String endDate;
    private int indexStart;
    private String beginDate;
    private List<CheckBox> mCheckBoxes = new ArrayList<>();

    private List<Integer> mDays = new ArrayList<>();
    List<MeetingBookItem.DataBean> mSelectTime = new ArrayList<>();
    public static MeetingPredetermineRecordActivity mRecordActivity;

    DatePopWindow datePopWindow;

    private int mDayPos = 1;
    private int mMonthPos = 1;
    private int mWeekPos = 1;

    private String[] mMonthArrays = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private String[] mWeekArray = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    private boolean mModifyMeeting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_predetermine_record);
        ButterKnife.bind(this);
        bind(this);
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initView() {
        mRecordActivity = this;
        mCheckBoxes.add(mSelectedMeetingDate1);
        mCheckBoxes.add(mSelectedMeetingDate2);
        mCheckBoxes.add(mSelectedMeetingDate3);
        mCheckBoxes.add(mSelectedMeetingDate4);
        mCheckBoxes.add(mSelectedMeetingDate5);
        mCheckBoxes.add(mSelectedMeetingDate6);
        mCheckBoxes.add(mSelectedMeetingDate7);
        mCheckBoxes.add(mSelectedMeetingDate8);
        mCheckBoxes.add(mSelectedMeetingDate9);
        for (int i = 0; i < mCheckBoxes.size(); i++) {
            mCheckBoxes.get(i).setOnCheckedChangeListener(this);
        }

        mMonthPos = DateUtils.getCurrentMonth();
        mTvMonth.setText(mMonthArrays[mMonthPos]);


        mDayPos = DateUtils.getCurrentDay();
        mTvDay.setText("" + findDay(mMonthPos + 1, mDayPos) + "日");

        mTvWeek.setText(mWeekArray[getWeek(mMonthPos + 1, mDayPos)]);

        int meetingId = getIntent().getIntExtra("roomId", -1);
        if (meetingId != -1) {
            mPresenter.getMeetingPredetermine(meetingId);
        }
        mModifyMeeting = getIntent().getBooleanExtra("modifyMeeting", false);
    }

    private int findDay(int month, int day) {
        int dayOne = 0;
        mDays = DateUtils.getDate(month);
        for (int i = 0; i < mDays.size(); i++) {
            if (mDays.get(i) == 1) {
                dayOne = i;
                break;
            }
        }
        return mDays.get(dayOne + day - 1);
    }

    public int getWeek(int month, int day) {
        Log.i("MeetingPredetermine", "month = " + month + ";;;;day = " + day);
        int pos = DateUtils.getWeek(month, day) - 1;
        return pos % 7;
    }

    @OnClick({R.id.btn_meeting_info_complete, R.id.ll_day_selector, R.id.ll_month_selector})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_meeting_info_complete:
                beginDate = null;
                DateIndex = 0;
                for (int i = 0; i < mCheckBoxes.size(); i++) {
                    if (mCheckBoxes.get(i).isChecked()) {
                        if (i - DateIndex == 1 || (DateIndex == 0 && beginDate == null)) {
                            if (beginDate == null) {
                                indexStart = mCheckBoxes.get(i).getText().toString().indexOf("—");
                                beginDate = mCheckBoxes.get(i).getText().toString().substring(0, indexStart);
                            }
                            DateIndex = i;
                            endDate = mCheckBoxes.get(i).getText().toString().substring(indexStart + 2, mCheckBoxes.get(i).getText().toString().length());
                        } else {
                            Toast.makeText(this, "只能预约连续的时间段", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                if (beginDate == null || endDate == null) {
                    Toast.makeText(this, "请选择预约时间段", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this, MeetingInfoFillOutActivity.class);
                intent.putExtra("endDate", endDate);
                intent.putExtra("isMeetingRecord", false);
                intent.putExtra("roomId", getIntent().getIntExtra("roomId", 0));
                intent.putExtra("meetingName", getIntent().getStringExtra("meetingName"));
                intent.putExtra("meetingDevice", getIntent().getStringExtra("meetingDevice"));
                intent.putExtra("meetingPeopleNumber", getIntent().getStringExtra("meetingPeopleNumber"));
                intent.putExtra("id", getIntent().getIntExtra("id", getIntent().getIntExtra("id", -1)));
                intent.putExtra("beginDate", (mMonthPos + 1) + "月" + mTvDay.getText().toString() + " " + beginDate);
                intent.putExtra("hoursOfUse", (mMonthPos + 1) + "月" + mTvDay.getText().toString() + "  " + beginDate + " -- " + endDate);
                if (mModifyMeeting) {
                    intent.putExtra("isWriteMeetingInfo", false);
                } else {
                    intent.putExtra("isWriteMeetingInfo", true);
                }

                intent.putExtra("start_time", DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + beginDate);
                intent.putExtra("end_time", DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + endDate);
                if (mModifyMeeting) {
                    intent.putExtra("modifyMeeting", true);
                    intent.putExtra("isMeetingRecord", true);
                    intent.putExtra("isMeetingPast", getIntent().getBooleanExtra("isMeetingPast", false));
                }
                startActivity(intent);
                break;
            case R.id.ll_day_selector:
                showDatePopWindow(true, mMonthPos + 1, mDayPos);
                break;
            case R.id.ll_month_selector:
                showDatePopWindow(false, 0, mMonthPos);
                break;
        }
    }

    public void showDatePopWindow(boolean isDay, final int month, int selectPos) {
        if (datePopWindow == null) {
            int height = DeviceUtil.getScreenHeight(this) - DeviceUtil.getStatusHeight(this) - mTopView.getHeight() - mDateLayout.getHeight();
            int topHeight = DeviceUtil.getScreenHeight(this) - height;
            datePopWindow = new DatePopWindow(this, mDateLayout, topHeight, height);
        }
        datePopWindow.setData(isDay, month, selectPos);
        datePopWindow.show();
        datePopWindow.setItemClick(new DatePopWindow.PopItemClick() {
            @Override
            public void onPopItemClick(boolean isDay, int position) {
                if (isDay) {
                    mDayPos = mDays.get(position);
                    mTvDay.setText("" + findDay(mMonthPos + 1, mDayPos) + "日");
                } else {
                    mMonthPos = position;
                    mTvMonth.setText(mMonthArrays[mMonthPos]);
                    int maxDay = DateUtils.getCurrentDaysInMonth(mMonthPos + 1);
                    if (mDayPos > maxDay) {
                        mDayPos = maxDay;
                    }

                    if (DateUtils.getCurrentMonth() == mMonthPos && mDayPos < DateUtils.getCurrentDay()) {
                        mDayPos = DateUtils.getCurrentDay();
                    }
                }

                mTvDay.setText("" + findDay(mMonthPos + 1, mDayPos) + "日");
                mTvWeek.setText(mWeekArray[getWeek(mMonthPos + 1, mDayPos)]);

                refreshSelectTime(DateUtils.stringToDate(DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos, "yyyy-MM-dd"));

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.selected_meeting_date1:
                if (mSelectedMeetingDate1.isChecked()) {
                    mSelectedMeetingDate1.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate1.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date2:
                if (mSelectedMeetingDate2.isChecked()) {
                    mSelectedMeetingDate2.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate2.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date3:
                if (mSelectedMeetingDate3.isChecked()) {
                    mSelectedMeetingDate3.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate3.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date4:
                if (mSelectedMeetingDate4.isChecked()) {
                    mSelectedMeetingDate4.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate4.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date5:
                if (mSelectedMeetingDate5.isChecked()) {
                    mSelectedMeetingDate5.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate5.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date6:
                if (mSelectedMeetingDate6.isChecked()) {
                    mSelectedMeetingDate6.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate6.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date7:
                if (mSelectedMeetingDate7.isChecked()) {
                    mSelectedMeetingDate7.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate7.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date8:
                if (mSelectedMeetingDate8.isChecked()) {
                    mSelectedMeetingDate8.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate8.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case R.id.selected_meeting_date9:
                if (mSelectedMeetingDate9.isChecked()) {
                    mSelectedMeetingDate9.setBackgroundColor(getResources().getColor(R.color.text_blue_color));
                } else {
                    mSelectedMeetingDate9.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
        }
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
    public void getMeetingPredetermineSuccess(List<MeetingBookItem.DataBean> dataBeen) {
        mSelectTime = dataBeen;
        refreshSelectTime(stringToDate(DateUtils.getTodayDate(false), "yyyy-MM-dd"));
    }

    private void refreshSelectTime(Date date) {
        for (int i = 0; i <= 8; i++) {
            setTimeEnable(i, true);
        }
        for (int i = 0; i < mSelectTime.size(); i++) {
            //接口返回的数据是秒
            long startTime = Long.parseLong(mSelectTime.get(i).getStart_time());
            long endTime = Long.parseLong(mSelectTime.get(i).getEnd_time());

//            Log.i("MeetingPredetermine", "startTime : " + DateUtils.longToDateString(startTime * 1000, "yyyy-MM-dd HH:MM"));
//            Log.i("MeetingPredetermine", "startTime : " + DateUtils.longToDateString(startTime * 1000, "HH"));
//            Log.i("MeetingPredetermine", "istoday = " + DateUtils.isToday(startTime * 1000));
//            Log.i("MeetingPredetermine", "endTime : " + DateUtils.longToDateString(endTime * 1000, "yyyy-MM-dd HH:MM"));

            if (DateUtils.isSameDay(startTime * 1000, date)) {
                try {
                    int start = Integer.valueOf(longToDateString(startTime * 1000, "HH"));
                    int end = Integer.valueOf(DateUtils.longToDateString(endTime * 1000, "HH"));
                    for (int j = start - 9; j <= end - 10; j++) {
                        setTimeEnable(j, false);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void setTimeEnable(int pos, boolean isEnable) {
        switch (pos) {
            case 0:
                mSelectedMeetingDate1.setEnabled(isEnable);
                break;
            case 1:
                mSelectedMeetingDate2.setEnabled(isEnable);
                break;
            case 2:
                mSelectedMeetingDate3.setEnabled(isEnable);
                break;
            case 3:
                mSelectedMeetingDate4.setEnabled(isEnable);
                break;
            case 4:
                mSelectedMeetingDate5.setEnabled(isEnable);
                break;
            case 5:
                mSelectedMeetingDate6.setEnabled(isEnable);
                break;
            case 6:
                mSelectedMeetingDate7.setEnabled(isEnable);
                break;
            case 7:
                mSelectedMeetingDate8.setEnabled(isEnable);
                break;
            case 8:
                mSelectedMeetingDate9.setEnabled(isEnable);
                break;
        }

    }


    @Override
    public void getMeetingPredetermineFailed(String msgStr) {

    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }
}
