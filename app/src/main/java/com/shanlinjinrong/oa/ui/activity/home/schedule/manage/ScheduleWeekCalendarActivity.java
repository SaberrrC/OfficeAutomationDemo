package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.UpdateTitleBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.fragment.MonthlyCalendarFragment;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.fragment.WeekCalendarFragment;
import com.shanlinjinrong.oa.utils.SelectedTimeFragment;
import com.shanlinjinrong.views.common.CommonTopView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
        mTopView.getTitleView().setOnClickListener(view -> {
            if (!mMonthlyCalendarFragment.isHidden() && mWeekCalendarFragment.isHidden()) {
                if (!EventBus.getDefault().isRegistered(mMonthlyCalendarFragment)) {
                    EventBus.getDefault().register(mMonthlyCalendarFragment);
                }
                EventBus.getDefault().post(new UpdateTitleBean("MonthlyCalendarFragment", "MonthlyCalendarFragment"));
            } else {
                if (!EventBus.getDefault().isRegistered(mWeekCalendarFragment)) {
                    EventBus.getDefault().register(mWeekCalendarFragment);
                }
                EventBus.getDefault().post(new SelectedWeekCalendarEvent(mTopView.getTitleView().getText().toString(), "mWeekCalendarFragment"));
//                    TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
//                    pvTime.setRange(1970, 2099);
//                    pvTime.setOnTimeSelectListener(this);
//                    pvTime.show();
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
        if (EventBus.getDefault().isRegistered(mWeekCalendarFragment)) {
            EventBus.getDefault().unregister(mWeekCalendarFragment);
        }
    }
}
