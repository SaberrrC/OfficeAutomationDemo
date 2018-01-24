package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
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
    private SelectedTimeFragment mSelectedTimeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_week_calendar);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();

        mTopView.getTitleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTimeFragment == null) {
                    mSelectedTimeFragment = new SelectedTimeFragment();
                }
                mSelectedTimeFragment.show(getSupportFragmentManager(), "0");
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.SELECTEDPOSITION, getIntent().getIntExtra(Constants.SELECTEDPOSITION, 0));
                bundle.putInt(Constants.CALENDARSTARTTIME, 1);
                bundle.putInt(Constants.CALENDARENDTIME, 2);
                mSelectedTimeFragment.setArguments(bundle);
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
            } else {
                getSupportFragmentManager().beginTransaction().show(mWeekCalendarFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(mMonthlyCalendarFragment).commit();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshTitle(UpdateTitleBean event) {
        if ("updateTitle".equals(event.getEvent())) {
            mTopView.setAppTitle(event.getTitle());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
