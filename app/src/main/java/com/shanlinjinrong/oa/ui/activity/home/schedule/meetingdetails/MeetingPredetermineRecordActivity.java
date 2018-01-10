package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.utils.DateUtils.longToDateString;
import static com.shanlinjinrong.oa.utils.DateUtils.stringToDate;

/**
 * 选择预约时间
 */
public class MeetingPredetermineRecordActivity extends HttpBaseActivity<MeetingPredeterminePresenter> implements MeetingPredetermineContract.View, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.selected_meeting_date1)
    CheckBox mSelectedMeetingDate1;
    @BindView(R.id.selected_meeting_date2)
    CheckBox mSelectedMeetingDate2;
    @BindView(R.id.selected_meeting_date3)
    CheckBox mSelectedMeetingDate3;
    @BindView(R.id.selected_meeting_date4)
    CheckBox mSelectedMeetingDate4;
    @BindView(R.id.selected_meeting_date5)
    CheckBox mSelectedMeetingDate5;
    @BindView(R.id.selected_meeting_date6)
    CheckBox mSelectedMeetingDate6;
    @BindView(R.id.selected_meeting_date7)
    CheckBox mSelectedMeetingDate7;
    @BindView(R.id.selected_meeting_date8)
    CheckBox mSelectedMeetingDate8;
    @BindView(R.id.selected_meeting_date9)
    CheckBox mSelectedMeetingDate9;
    @BindView(R.id.btn_meeting_info_complete)
    TextView mBtnMeetingInfoComplete;
    @BindView(R.id.ll_day_selector)
    LinearLayout mLlDaySelector;
    @BindView(R.id.ll_month_selector)
    LinearLayout mLlMonthSelector;
    @BindView(R.id.ll_date_layout)
    LinearLayout mDateLayout;
    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_month)
    TextView mTvMonth;
    @BindView(R.id.tv_day)
    TextView mTvDay;
    @BindView(R.id.tv_week)
    TextView mTvWeek;
    @BindView(R.id.tv_not_network)
    TextView mTvNotNetwork;
    @BindView(R.id.ll_content_show)
    LinearLayout mLlContentShow;

    private int DateIndex;
    private String endDate;
    private int indexStart;
    private boolean isNetwork;
    private String beginDate;
    private List<CheckBox> mCheckBoxes = new ArrayList<>();

    private List<Integer> mDays = new ArrayList<>();
    List<MeetingBookItem.DataBean> mSelectTime = new ArrayList<>();

    DatePopWindow datePopWindow;

    private int mDayPos = 1;
    private int mMonthPos = 1;
    private int mWeekPos = 1;

    private String[] mMonthArrays = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private String[] mWeekArray = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    private boolean mModifyMeeting;
    private int mMeetingId;
    private String mBeginDate;
    private String mEndDate;
    private int mStart;
    private int mEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_predetermine_record);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initView() {
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

        mMeetingId = getIntent().getIntExtra("roomId", -1);
        if (mMeetingId != -1) {
            mPresenter.getMeetingPredetermine(mMeetingId);
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
        int pos = DateUtils.getWeek(month, day) - 1;
        return pos % 7;
    }

    @OnClick({R.id.btn_meeting_info_complete, R.id.ll_day_selector, R.id.ll_month_selector, R.id.tv_not_network})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_meeting_info_complete:
                beginDate = null;
                DateIndex = 0;
            /*    if (!isNetwork) {
                    showToast(getString(R.string.net_no_connection));
                    return;
                }*/
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


                if (mModifyMeeting) {


                    HttpParams httpParams = new HttpParams();
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("start_time", DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + beginDate);
//                        jsonObject.put("end_time", DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + endDate);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    httpParams.putJsonParams(jsonObject.toString());
                    httpParams.put("start_time", DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + beginDate);
                    httpParams.put("end_time", DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + endDate);
                    httpParams.put("meeting_id", getIntent().getIntExtra("id", -1));
                    httpParams.put("send_type", DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + endDate);

                    mPresenter.modifyMeetingRooms(httpParams);
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
                String replace = endDate.replace("—", "");
                intent.putExtra("start_time", DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + beginDate);
                intent.putExtra("end_time", DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + replace);
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
            case R.id.tv_not_network:
                mPresenter.getMeetingPredetermine(mMeetingId);
                break;
        }
    }

    private void refreshSelectTime(Date date) {
        for (int i = 0; i <= 8; i++) {
            setTimeEnable(i, true, false);
        }
        for (int i = 0; i < mSelectTime.size(); i++) {
            //接口返回的数据是秒
            long startTime = Long.parseLong(String.valueOf(mSelectTime.get(i).getStart_time()));
            long endTime = Long.parseLong(String.valueOf(mSelectTime.get(i).getEnd_time()));

            if (DateUtils.isSameDay(startTime * 1000, date)) {
                try {
                    mStart = Integer.valueOf(longToDateString(startTime * 1000, "HH"));
                    mEnd = Integer.valueOf(DateUtils.longToDateString(endTime * 1000, "HH"));

                    mBeginDate = getIntent().getStringExtra("startTime");
                    mEndDate = getIntent().getStringExtra("endTime");

                    for (int j = mStart - 9; j <= mEnd - 10; j++) {
                        setTimeEnable(j, false, false);
                    }
                    if (mBeginDate != null && mEndDate != null) {
                        String day = mBeginDate.substring(mBeginDate.indexOf("日") - 2, mBeginDate.indexOf("日"));
                        if (mBeginDate.contains(mStart + "") && mEndDate.contains(mEnd + "") && day.contains(mDayPos + "")) {
//                            setTimeEnable(mStart - 9, true, true);
                            for (int j = mStart - 9; j <= mEnd - 10; j++) {
                                setTimeEnable(j, true, true);
                                mBtnMeetingInfoComplete.setEnabled(false);
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
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
    public void requestFinish() {
        hideLoadingView();
    }

    @Override
    public void requestNetworkError() {

    }

    @Override
    public void getMeetingPredetermineSuccess(List<MeetingBookItem.DataBean> dataBeen) {
        try {
            mTvNotNetwork.setVisibility(View.GONE);
            mLlContentShow.setVisibility(View.VISIBLE);
            if (dataBeen == null) {
                dataBeen = new ArrayList<>();
            }
            mSelectTime.addAll(dataBeen);
            isNetwork = true;
            refreshSelectTime(stringToDate(DateUtils.getTodayDate(false), "yyyy-MM-dd"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMeetingPredetermineFailed(int errorCode, String msgStr) {
        switch (errorCode) {
            case -1:
                isNetwork = false;
                showToast(getString(R.string.net_no_connection));
                mTvNotNetwork.setVisibility(View.VISIBLE);
                mLlContentShow.setVisibility(View.GONE);
                break;
//            case Api.RESPONSES_CODE_DATA_EMPTY:
//                mTvNotNetwork.setText("会议室预定错误，请重试");
//                mTvNotNetwork.setVisibility(View.VISIBLE);
//                mLlContentShow.setVisibility(View.GONE);
//                break;
            default:
                mTvNotNetwork.setVisibility(View.GONE);
                mLlContentShow.setVisibility(View.VISIBLE);
                break;
        }
    }

    //会议室调期
    @Override
    public void modifyMeetingRoomsSuccess() {
        String startDate = DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos + " " + beginDate;
        EventBus.getDefault().post("finish");
//        MeetingReservationRecordActivity.mRecordActivity.finish();
        Intent intent = new Intent(this, MeetingReservationSucceedActivity.class);
        intent.putExtra("mReservation", "您已经成功调期");
        intent.putExtra("mMeetingDate", startDate.replace(" ", "  ") + " - " + endDate);
        intent.putExtra("mMeetingName", getIntent().getStringExtra("meeting_name"));
        startActivity(intent);
        EventBus.getDefault().post("finish");
//        MeetingPredetermineRecordActivity.mRecordActivity.finish();
//        MeetingReservationRecordActivity.mRecordActivity.finish();
        finish();
    }

    @Override
    public void modifyMeetingRoomsFailed(int errorCode, String strMsg) {


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
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    /*    if (!isNetwork) {
            mPresenter.getMeetingPredetermine(mMeetingId);
            return;
        }*/
        mBtnMeetingInfoComplete.setEnabled(true);
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

    public void setTimeEnable(int pos, boolean isEnable, boolean isChecked) {
        switch (pos) {
            case 0:
                mSelectedMeetingDate1.setChecked(isChecked);
                mSelectedMeetingDate1.setEnabled(isEnable);
                if (!isEnable) {
                    mSelectedMeetingDate1.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
                } else if (!isChecked) {
                    mSelectedMeetingDate1.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case 1:
                mSelectedMeetingDate2.setChecked(isChecked);
                mSelectedMeetingDate2.setEnabled(isEnable);
                if (!isEnable) {
                    mSelectedMeetingDate2.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
                } else if (!isChecked) {
                    mSelectedMeetingDate2.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case 2:
                mSelectedMeetingDate3.setChecked(isChecked);
                mSelectedMeetingDate3.setEnabled(isEnable);
                if (!isEnable) {
                    mSelectedMeetingDate3.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
                } else if (!isChecked) {
                    mSelectedMeetingDate3.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case 3:
                mSelectedMeetingDate4.setChecked(isChecked);
                mSelectedMeetingDate4.setEnabled(isEnable);
                if (!isEnable) {
                    mSelectedMeetingDate4.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
                } else if (!isChecked) {
                    mSelectedMeetingDate4.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case 4:
                mSelectedMeetingDate5.setChecked(isChecked);
                mSelectedMeetingDate5.setEnabled(isEnable);
                if (!isEnable) {
                    mSelectedMeetingDate5.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
                } else if (!isChecked) {
                    mSelectedMeetingDate5.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case 5:
                mSelectedMeetingDate6.setChecked(isChecked);
                mSelectedMeetingDate6.setEnabled(isEnable);
                if (!isEnable) {
                    mSelectedMeetingDate6.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
                } else if (!isChecked) {
                    mSelectedMeetingDate6.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case 6:
                mSelectedMeetingDate7.setChecked(isChecked);
                mSelectedMeetingDate7.setEnabled(isEnable);
                if (!isEnable) {
                    mSelectedMeetingDate7.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
                } else if (!isChecked) {
                    mSelectedMeetingDate7.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case 7:
                mSelectedMeetingDate8.setChecked(isChecked);
                mSelectedMeetingDate8.setEnabled(isEnable);
                if (!isEnable) {
                    mSelectedMeetingDate8.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
                } else if (!isChecked) {
                    mSelectedMeetingDate8.setBackgroundColor(getResources().getColor(R.color.white));
                }
                break;
            case 8:
                mSelectedMeetingDate9.setChecked(isChecked);
                mSelectedMeetingDate9.setEnabled(isEnable);
                if (!isEnable) {
                    mSelectedMeetingDate9.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
                } else if (!isChecked) {
                    mSelectedMeetingDate9.setBackgroundColor(getResources().getColor(R.color.white));
                }
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
        datePopWindow.setItemClick((isDay1, position) -> {
            if (isDay1) {
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

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinish(String str) {
        if (str.equals("finish"))
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
