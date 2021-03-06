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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.aigestudio.wheelpicker.WheelPicker;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectedTimeFragment extends DialogFragment {

    @BindView(R.id.picker_empty)
    WheelPicker mPickerEmpty;
    @BindView(R.id.picker_hour_time)
    WheelPicker mPickerHourTime;
    @BindView(R.id.picker_min_time)
    WheelPicker mPickerMinTime;


    private List<String> mStartTimes;
    private List<String> mEndTimes;
    private int          mStartTime;
    private int          mEndTime;
    private boolean      mIsStart;
    private Context      mContext;
    private String       tag;
    private String       mMinTime;
    private String       mHourTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_time_widget, container, false);
        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        mStartTime = arguments.getInt(Constants.CALENDARSTARTTIME);
        mEndTime = arguments.getInt(Constants.CALENDARENDTIME);
        mIsStart = arguments.getBoolean("isStart", true);
        initWheelPicker();

        slideToUp(view);
        return view;
    }

    private void initWheelPicker() {

        mStartTimes = new ArrayList<>();
        mEndTimes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                mStartTimes.add("09点");
            } else {
                mStartTimes.add(9 + i + "点");
            }
        }
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                mEndTimes.add("0" + i + "分");
            } else {
                mEndTimes.add(i + "分");
            }
        }


        mPickerHourTime.setData(mStartTimes);
        mPickerMinTime.setData(mEndTimes);
        mPickerEmpty.setData(new ArrayList());

        try {
            //int position1 = mStartTime - 9;
            //int position2 = mEndTime - 10;


            if (mIsStart) {
                mPickerHourTime.setSelectedItemPosition(getArguments().getInt(Constants.CALENDARSTARTHOUR) - 9);
                mPickerMinTime.setSelectedItemPosition(getArguments().getInt(Constants.CALENDARSTARTMIN));
            } else {
                mPickerHourTime.setSelectedItemPosition(getArguments().getInt(Constants.CALENDARENDHOUR) - 9);
                mPickerMinTime.setSelectedItemPosition(getArguments().getInt(Constants.CALENDARENDMIN));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    public void slideToUp(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @OnClick({R.id.btn_cancel, R.id.btn_affirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_affirm:
                int currentHourTime = mPickerHourTime.getCurrentItemPosition();
                int currentMinTime = mPickerMinTime.getCurrentItemPosition();

                if (mIsStart) {
                    EventBus.getDefault().post(new SelectedWeekCalendarEvent(Constants.SELECTEDTIME, mIsStart, currentHourTime + 9, currentMinTime));
                    dismiss();
                } else {
                    EventBus.getDefault().post(new SelectedWeekCalendarEvent(Constants.SELECTEDTIME, currentHourTime + 9, currentMinTime, mIsStart));
                    dismiss();
                }
                break;
            default:
                dismiss();
                break;
        }
    }
}
