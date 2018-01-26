package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.UpdateTitleBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.fragment.MonthlyCalendarFragment;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.fragment.WeekCalendarFragment;
import com.shanlinjinrong.oa.utils.SelectedTimeFragment;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Date;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 周历 月历
 */
public class ScheduleWeekCalendarActivity extends AppCompatActivity {

    @BindView(R.id.topView)
    CommonTopView mTopView;

    private WeekCalendarFragment    mWeekCalendarFragment;
    private MonthlyCalendarFragment mMonthlyCalendarFragment;
    private SelectedTimeFragment    mSelectedTimeFragment;
    private String weekLastTitle  = "";
    private String monthLastTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_week_calendar);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();


        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(1970, 1, 1);
        endDate.set(2098, 12, 31);


        mTopView.getTitleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mMonthlyCalendarFragment.isHidden() && mWeekCalendarFragment.isHidden()) {
                    if (!EventBus.getDefault().isRegistered(mMonthlyCalendarFragment)) {
                        EventBus.getDefault().register(mMonthlyCalendarFragment);
                    }
                    EventBus.getDefault().postSticky(new UpdateTitleBean("MonthlyCalendarFragment", "MonthlyCalendarFragment"));
                } else {
                    //TODO 时间选择器

                    //时间选择器
                    TimePickerView pvTime = new TimePickerView.Builder(ScheduleWeekCalendarActivity.this, new TimePickerView.OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(java.util.Date date, View v) {

                        }
                    }).build();
                    pvTime.setDate(Calendar.getInstance());
                    pvTime.show();


//                    if (mSelectedTimeFragment == null) {
//                        mSelectedTimeFragment = new SelectedTimeFragment();
//                    }
//                    mSelectedTimeFragment.show(getSupportFragmentManager(), "0");
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(Constants.SELECTEDPOSITION, getIntent().getIntExtra(Constants.SELECTEDPOSITION, 0));
//                    bundle.putInt(Constants.CALENDARSTARTTIME, 1);
//                    bundle.putInt(Constants.CALENDARENDTIME, 2);
//                    mSelectedTimeFragment.setArguments(bundle);
                    //  mBuild.show();

//                    EventBus.getDefault().post(new SelectedWeekCalendarEvent( Constants.SELECTEDTIME,"1999-01-23","1999-01-24"));
                }

            }
        });
    }

    private void initData() {
        mWeekCalendarFragment = new WeekCalendarFragment();
        mMonthlyCalendarFragment = new MonthlyCalendarFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container_layout, mWeekCalendarFragment).hide(mWeekCalendarFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container_layout, mMonthlyCalendarFragment).commit();

        mTopView.getRightView().setOnClickListener(view -> {

            if (mMonthlyCalendarFragment.isHidden()) {
                getSupportFragmentManager().beginTransaction().show(mMonthlyCalendarFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(mWeekCalendarFragment).commit();
                mTopView.setAppTitle(monthLastTitle);
            } else {
                getSupportFragmentManager().beginTransaction().show(mWeekCalendarFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(mMonthlyCalendarFragment).commit();
                mTopView.setAppTitle(weekLastTitle);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshTitle(UpdateTitleBean event) {
        if ("updateTitle".equals(event.getEvent())) {
            weekLastTitle = event.getTitle();
            mTopView.setAppTitle(weekLastTitle);
        } else if ("monthLUpdateTitle".equals(event.getEvent())) {
            monthLastTitle = event.getTitle();
            mTopView.setAppTitle(monthLastTitle);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (EventBus.getDefault().isRegistered(mMonthlyCalendarFragment)) {
            EventBus.getDefault().unregister(mMonthlyCalendarFragment);
        }
    }
}
