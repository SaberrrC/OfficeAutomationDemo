package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 编辑日历
 */
public class CalendarRedactActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_date)
    TextView      mTvDate;
    @BindView(R.id.ed_task_theme)
    EditText      mEdTaskTheme;
    @BindView(R.id.tv_task_date)
    TextView      mTvTaskDate;
    @BindView(R.id.ed_task_details)
    EditText      mEdTaskDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_redact);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        mTopView.getRightView().setOnClickListener(view -> {
            //TODO 提交按钮
        });
    }

    private void initData() {
        String year = getIntent().getStringExtra("year");
        String month = getIntent().getStringExtra("month");
        String day = getIntent().getStringExtra("day");
        int startTime = getIntent().getIntExtra("startTime", -1);
        int endTime = getIntent().getIntExtra("endTime", -1);
        mTvDate.setText(month + "月" + day + "日");
        mTvTaskDate.setText(startTime + ":00" + "-" + endTime + ":00");
    }

    @OnClick(R.id.tv_task_date)
    public void onViewClicked() {
        //TODO 弹出时间选择框

    }
}
