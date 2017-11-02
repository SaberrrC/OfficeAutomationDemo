package com.shanlinjinrong.oa.utils;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.baidu.platform.comapi.map.E;
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
        for (int i = 0; i < 59; i++) {
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

//    /**
//     * 返回当前时间是上午还是下午
//     *
//     * @return
//     */
//    public static Integer getCurrDateAMorPM() {
//        Calendar calendar = Calendar.getInstance();
//        return calendar.get(Calendar.AM_PM);
//    }


    //天
//        dayList = new ArrayList<>();
//        dayList.add("今天");
//        dayList.add("明天");
//        wheelDayPicker.setData(dayList);
    //小时
//        hoursList = new ArrayList<>();
//        for (int i = 1; i <= 24; i++) {
//            hoursList.add(i + "");
//        }
//        wheelHoursPicker.setData(hoursList);
//        wheelHoursPicker.setSelectedItemPosition(8);
    //分钟
//        minList = new ArrayList<>();
//        for (int i = 0; i < 6; i++) {
//            if (i == 0) {
//                minList.add("00");
//            } else {
//                minList.add(i * 10 + "");
//            }
//        }
//        wheelMinPicker.setData(minList);
//        wheelMinPicker.setSelectedItemPosition(3);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
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
//                day.replace("")

                if (time.equals("下午")) {
                    int number = Integer.parseInt(hour);
                    int selectTime = number + 12;
                    if (currentItemPosition0 == 30) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date currentDate = new Date(System.currentTimeMillis());
                        String format = formatter.format(currentDate);
                        mSelectedDate = format + " " + selectTime + ":" + min;

                    }

//                    else xif (){
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.setTime(new Date());
//                        calendar.add();
//                    }
//                    else if (){
//
//                    }
                    EventBus.getDefault().post(new SelectedTypeBean("selectedDate", mSelectedDate, getArguments().getInt("index"), getArguments().getInt("isBegin")));
                }

//                EventBus.getDefault().post(new SelectedTypeBean("date", ));


//                int currentDayItemPosition = wheelDayPicker.getCurrentItemPosition();
//                int currentHourItemPosition = wheelHoursPicker.getCurrentItemPosition();
//                int currentMinItemPosition = wheelMinPicker.getCurrentItemPosition();
//                String day = dayList.get(currentDayItemPosition);
//                String hour = hoursList.get(currentHourItemPosition);
//                String min = minList.get(currentMinItemPosition);
//                Calendar calendar = Calendar.getInstance();
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int dayOfMon = calendar.get(Calendar.DAY_OF_MONTH);
//                if (currentDayItemPosition == 1 && day.equals("明天")) {
//                    calendar.set(year, month, dayOfMon + 1, Integer.parseInt(hour), Integer.parseInt(min));
//                } else if (currentDayItemPosition == 0 && day.equals("今天")) {
//                    calendar.set(year, month, dayOfMon, Integer.parseInt(hour), Integer.parseInt(min));
//                }
//                Date time = calendar.getTime();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String formatTime = simpleDateFormat.format(time);
//                if (dialogFragmentCallBack != null) {
//                    TimePickerBean timePickerBean = new TimePickerBean();
//                    timePickerBean.postTime = formatTime;
//                    timePickerBean.showTime = day + " " + hour + ":" + min;
//                    dialogFragmentCallBack.successDialog(timePickerBean);
//                }
//                PromptBoxUtil.showInfo(formatTime);


                dismiss();
                break;
        }
    }


//    public void setDialogCallBack(DialogFragmentCallBack dialogCallBack) {
//        this.dialogFragmentCallBack = dialogCallBack;
//    }

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
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

}
