package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.DatePopWindow;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.utils.DeviceUtil;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class AttandenceMonthActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ll_chose_time)
    LinearLayout mLlChoseTime;
    @Bind(R.id.ll_count_people)
    LinearLayout ll_count_people;
    @Bind(R.id.ll_currentday_state)
    LinearLayout mLlCurrentdayState;

    private DatePicker picker;
    DatePopWindow datePopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_month);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //统计时间选择
            case R.id.ll_chose_time:
                if (picker == null) {
                    picker = new DatePicker(AttandenceMonthActivity.this, DatePicker.YEAR_MONTH);
                }
                Calendar cal = Calendar.getInstance();
                picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1
                );
                picker.setSubmitText("完成");
                picker.setSubmitTextColor(Color.parseColor("#2d9dff"));
                picker.setTextColor(Color.parseColor("#2d9dff"));
                picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                    @Override
                    public void onDatePicked(String year, String month) {

                    }
                });
                picker.show();
                break;
            //统计人员选择
            case R.id.ll_count_people:
                Intent intent = new Intent(AttandenceMonthActivity.this,CountPeopleActivity.class);
                startActivity(intent);
                break;
            //考勤记录
            case R.id.ll_currentday_state:
                break;

        }
    }


    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("考勤月历");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    public void showDatePopWindow(boolean isDay, final int month, int selectPos) {
//        if (datePopWindow == null) {
//            int height = DeviceUtil.getScreenHeight(this) - DeviceUtil.getStatusHeight(this) - mTopView.getHeight() - mDateLayout.getHeight();
//            int topHeight = DeviceUtil.getScreenHeight(this) - height;
//            datePopWindow = new DatePopWindow(this, mDateLayout, topHeight, height);
//        }
//        datePopWindow.setData(isDay, month, selectPos);
//        datePopWindow.show();
//        datePopWindow.setItemClick(new DatePopWindow.PopItemClick() {
//            @Override
//            public void onPopItemClick(boolean isDay, int position) {
//                if (isDay) {
//                    mDayPos = mDays.get(position);
//                    mTvDay.setText("" + findDay(mMonthPos + 1, mDayPos) + "日");
//                } else {
//                    mMonthPos = position;
//                    mTvMonth.setText(mMonthArrays[mMonthPos]);
//                    int maxDay = DateUtils.getCurrentDaysInMonth(mMonthPos + 1);
//                    if (mDayPos > maxDay) {
//                        mDayPos = maxDay;
//                    }
//
//                    if (DateUtils.getCurrentMonth() == mMonthPos && mDayPos < DateUtils.getCurrentDay()) {
//                        mDayPos = DateUtils.getCurrentDay();
//                    }
//                }
//
//                mTvDay.setText("" + findDay(mMonthPos + 1, mDayPos) + "日");
//                mTvWeek.setText(mWeekArray[getWeek(mMonthPos + 1, mDayPos)]);
//
//                refreshSelectTime(DateUtils.stringToDate(DateUtils.getCurrentYear() + "-" + (mMonthPos + 1) + "-" + mDayPos, "yyyy-MM-dd"));
//
//            }
//        });
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
