package com.shanlinjinrong.oa.ui.activity.calendar;

import android.os.Bundle;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.calendar.contract.MouthCalenderContract;
import com.shanlinjinrong.oa.ui.activity.calendar.presenter.MouthCalenderPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

public class MouthCalenderActivity extends HttpBaseActivity<MouthCalenderPresenter> implements MouthCalenderContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouth_calender);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(int code) {

    }
}
