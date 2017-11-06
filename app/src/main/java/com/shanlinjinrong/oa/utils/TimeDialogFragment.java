package com.shanlinjinrong.oa.utils;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//时间选择器
public class TimeDialogFragment extends DialogFragment {
    Context context;
    List<String> mDayList, hoursList, minList, timeFrameList;
    @Bind(R.id.wheelDayPickerYear)
    WheelPicker mWheelDayPickerMonth;
    @Bind(R.id.wheelMinPickerHour)
    WheelPicker mWheelMinPickerHour;
    @Bind(R.id.wheelHoursPickerMinutes)
    WheelPicker mWheelHoursPickerMinutes;
    @Bind(R.id.wheelTimePicker)
    WheelPicker mWheelTimePicker;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    private String mWeek;
    private String mSelectedDate;
    private int mSelectTime;
    private String tag;
    private String mHour;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_dialog_fragment, container, false);
        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        String title = arguments.getString("title");
        mTvTitle.setText(title);
        initWheelPicker();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
//        window.getAttributes().windowAnimations = R.style.dialogAnim;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initWheelPicker() {
        //日期
        mDayList = new ArrayList<>();
        getDate(false);
        mDayList.add("今天");
        int position = mDayList.size();
        getDate(true);
        mWheelDayPickerMonth.setData(mDayList);
        mWheelDayPickerMonth.setSelectedItemPosition(position - 1);


        //上午下午
        timeFrameList = new ArrayList<>();
        timeFrameList.add("上午");
        timeFrameList.add("下午");
        mWheelTimePicker.setData(timeFrameList);

        //小时 处理
        hoursList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            hoursList.add(i + 1 + "");
        }
        mWheelMinPickerHour.setData(hoursList);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(simpleDateFormat.format(new Date()));
        if (hour > 12) {
            mWheelTimePicker.setSelectedItemPosition(1);
            hour = hour - 12;
        }
        mWheelMinPickerHour.setSelectedItemPosition(hour - 1);

        //分钟 处理
        minList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                String s = "0" + i;
                minList.add(s);
            } else {
                minList.add(i + "");
            }
        }
        mWheelHoursPickerMinutes.setData(minList);
        SimpleDateFormat simpleDateFormatmin = new SimpleDateFormat("mm");
        int min = Integer.parseInt(simpleDateFormatmin.format(new Date()));
        mWheelHoursPickerMinutes.setSelectedItemPosition(min - 1);
    }

    public List<String> getDate(boolean isNextMonth) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (!isNextMonth) {
            for (int i = 0; i < 30; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                String month_day = simpleDateFormat.format(calendar.getTime()).substring(simpleDateFormat.format(calendar.getTime()).indexOf("年") + 1, simpleDateFormat.format(calendar.getTime()).length());
                mDayList.add(0, month_day + "  " + getWeek(simpleDateFormat.format(calendar.getTime())));
            }
        } else {
            for (int i = 0; i < 30; i++) {
                calendar.add(Calendar.DAY_OF_MONTH, +1);
                String month_day = simpleDateFormat.format(calendar.getTime()).substring(simpleDateFormat.format(calendar.getTime()).indexOf("年") + 1, simpleDateFormat.format(calendar.getTime()).length());
                mDayList.add(month_day + "  " + getWeek(simpleDateFormat.format(calendar.getTime())));
            }
        }
        return mDayList;
    }

    public String getWeek(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
            Date date1 = sdf1.parse(date);
            mWeek = sdf.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mWeek;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.dimissButton, R.id.sureButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dimissButton:
                dismiss();
                break;
            case R.id.sureButton:
                int currentItemPosition0 = mWheelDayPickerMonth.getCurrentItemPosition();
                int currentItemPosition1 = mWheelMinPickerHour.getCurrentItemPosition();
                int currentItemPosition2 = mWheelHoursPickerMinutes.getCurrentItemPosition();
                int currentItemPosition3 = mWheelTimePicker.getCurrentItemPosition();
                String day = mDayList.get(currentItemPosition0);
                String hour = hoursList.get(currentItemPosition1);
                String min = minList.get(currentItemPosition2);
                String time = timeFrameList.get(currentItemPosition3);
                int number = Integer.parseInt(hour);
                if (time.equals("下午")) {
                    mSelectTime = number + 12;
                } else {
                    mSelectTime = number;
                }
                if (mSelectTime < 10) {
                    mHour = "0" + mSelectTime;
                } else if (mSelectTime == 24) {
                    mHour = "00";
                } else {
                    mHour = "" + mSelectTime;
                }
                if (currentItemPosition0 == 30) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentDate = new Date(System.currentTimeMillis());
                    String format = formatter.format(currentDate);
                    mSelectedDate = format + " " + mHour + ":" + min;
                } else if (currentItemPosition0 < 30) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    if (mSelectTime == 24) {
                        calendar.add(Calendar.DAY_OF_MONTH, -(30 - currentItemPosition0 - 1));
                    } else {
                        calendar.add(Calendar.DAY_OF_MONTH, -(30 - currentItemPosition0));
                    }
                    String format = simpleDateFormat.format(calendar.getTimeInMillis());
                    mSelectedDate = format + " " + mHour + ":" + min;
                } else if (currentItemPosition0 > 30) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    if (mSelectTime == 24) {
                        calendar.add(Calendar.DAY_OF_MONTH, 30 - (60 - currentItemPosition0) + 1);
                    } else {
                        calendar.add(Calendar.DAY_OF_MONTH, 30 - (60 - currentItemPosition0));
                    }
                    String format = simpleDateFormat.format(calendar.getTimeInMillis());
                    mSelectedDate = format + " " + mHour + ":" + min;
                }

                SelectedTypeBean event = new SelectedTypeBean("selectedDate", mSelectedDate, getArguments().getInt("index"), getArguments().getInt("isBegin"));
                if (!TextUtils.isEmpty(tag)) {
                    event.setTag(tag);
                }
                EventBus.getDefault().post(event);
                dismiss();
                break;
        }
    }

    class TimePickerBean {
        //格式：yyyy-MM-dd HH:mm:ss
        public String postTime;
        //格式：今天 18:30
        public String showTime;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded() && !isVisible() && !isRemoving()) {
            FragmentTransaction ft = manager.beginTransaction();
            this.tag = tag;
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

}
