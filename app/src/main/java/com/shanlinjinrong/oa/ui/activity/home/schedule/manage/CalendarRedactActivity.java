package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.WriteWeeklyNewspaperActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.pickerview.OptionsPickerView;
import com.shanlinjinrong.views.common.CommonTopView;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    @BindView(R.id.btn_common_calendar)
    TextView      mBtnCommonCalendar;
    @BindView(R.id.cb_completes)
    CheckBox      mCbCompletes;
    @BindView(R.id.view_completes)
    View          mViewCompletes;

    private String            mDate;
    private int               mStartTime;
    private int               mEndTime;
    private String            mStartTimes;
    private String            mEndTimes;
    private String            mTitle;
    private String            mContent;
    private String            mYear;
    private String            mMonth;
    private int               mItemType;
    private OptionsPickerView beginTimeView;

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
            switch (mItemType) {
                case Constants.WRITECALENDAR:
                    finish();
                    break;
                //TODO 删除
                case Constants.LOOKCALENDAR:

                    break;
                case Constants.MEETINGCALENDAR:
                    break;
                default:
                    break;
            }

        });


        SimpleDateFormat from = new SimpleDateFormat("yyyyMMdd");
        String date = from.format(new Date());


        switch (mItemType) {
            case Constants.WRITECALENDAR:
                if (date.equals(mYear + mMonth + mDate + "")) {
                    mTvDate.setText(mMonth + "月" + mDate + "日  今天");
                } else {
                    mTvDate.setText(mMonth + "月" + mDate + "日");
                }
                mTvTaskDate.setText(mStartTime + ":00" + "-" + mEndTime + ":00");
                break;
            case Constants.LOOKCALENDAR:

                mTvDate.setText(mDate);
                mTopView.setRightText("删除");
                mBtnCommonCalendar.setText("编辑");
                mCbCompletes.setVisibility(View.VISIBLE);
                mViewCompletes.setVisibility(View.VISIBLE);

                mEdTaskTheme.setEnabled(false);
                mTvTaskDate.setEnabled(false);
                mEdTaskDetails.setEnabled(false);

                mEdTaskTheme.setText(mTitle);
                mTvTaskDate.setText(mStartTimes + "-" + mEndTimes);
                mEdTaskDetails.setText(mContent);
                break;
            case Constants.MEETINGCALENDAR:
                break;
            default:
                break;
        }
    }

    private void initData() {
        mItemType = getIntent().getIntExtra(Constants.CALENDARTYPE, -1);
        switch (mItemType) {
            //编辑周历
            case Constants.WRITECALENDAR:
                mYear = getIntent().getStringExtra(Constants.CALENDARYEAR);
                mMonth = getIntent().getStringExtra(Constants.CALENDARMONTH);
                mDate = getIntent().getStringExtra(Constants.CALENDARDATE);
                mStartTime = getIntent().getIntExtra(Constants.CALENDARSTARTTIME, -1);
                mEndTime = getIntent().getIntExtra(Constants.CALENDARENDTIME, -1);
                break;
            //查看周历
            case Constants.LOOKCALENDAR:
                mTitle = getIntent().getStringExtra(Constants.CALENDARTITLE);
                mContent = getIntent().getStringExtra(Constants.CALENDARCONTENT);
                mStartTimes = getIntent().getStringExtra(Constants.CALENDARSTARTTIME);
                mEndTimes = getIntent().getStringExtra(Constants.CALENDARENDTIME);
                mDate = getIntent().getStringExtra(Constants.CALENDARDATE);
                break;
            //查看会议室
            case Constants.MEETINGCALENDAR:
                break;
            default:
                break;
        }

    }

    @OnClick({R.id.tv_task_date, R.id.btn_common_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_common_calendar:
                switch (mItemType) {
                    //编辑周历
                    case Constants.WRITECALENDAR:
                        if ("".equals(mEdTaskTheme.getText().toString())) {
                            Toast.makeText(this, "任务主题不能为空！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    //查看周历
                    case Constants.LOOKCALENDAR:
                        if ("编辑".equals(mBtnCommonCalendar.getText().toString())) {
                            mEdTaskTheme.setEnabled(true);
                            mTvTaskDate.setEnabled(true);
                            mEdTaskDetails.setEnabled(true);
                            mBtnCommonCalendar.setText("更改");
                        } else {
                            mEdTaskTheme.setEnabled(false);
                            mTvTaskDate.setEnabled(false);
                            mEdTaskDetails.setEnabled(false);
                            mBtnCommonCalendar.setText("编辑");
                        }
                        break;
                    //查看会议室
                    case Constants.MEETINGCALENDAR:
                        break;
                    default:
                        break;

                }
                break;
            //TODO 弹出时间选择框
            case R.id.tv_task_date:

                break;
            default:
                break;
        }
    }

}
