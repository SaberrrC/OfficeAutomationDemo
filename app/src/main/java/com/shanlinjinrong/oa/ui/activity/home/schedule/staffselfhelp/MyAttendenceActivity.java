package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.MyAttendenceActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter.MyAttendenceActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.views.MonthSelectPopWindow;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的考勤
 */
public class MyAttendenceActivity extends HttpBaseActivity<MyAttendenceActivityPresenter> implements MyAttendenceActivityContract.View, View.OnClickListener {

    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_delay)
    TextView tv_delay;
    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.ll_rootView)
    LinearLayout mRootView;
    @BindView(R.id.tv_leave_early)
    TextView tv_leave_early;
    @BindView(R.id.tv_business_travel)
    TextView tv_business_travel;
    @BindView(R.id.tv_absenteeism)
    TextView tv_absenteeism;
    @BindView(R.id.tv_sign_card_num)
    TextView tv_sign_card_num;

    private Calendar cal;
    private int mCurrentYear;
    private int mCurrentMonth;
    private String mPrivateCode = "";
    private MonthSelectPopWindow monthSelectPopWindow;
    private MyAttandanceResponse mMyAttandanceResponse1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_work_record);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initView() {
        mTopView.setRightDrawable(getResources().getDrawable(R.mipmap.month_right));
        View rightView = mTopView.getRightView();
        rightView.setOnClickListener(view -> {
            Intent intent = new Intent(MyAttendenceActivity.this, AttandenceMonthActivity.class);
            startActivity(intent);
        });
    }

    private void initData() {
        tv_delay.setOnClickListener(this);
        tv_leave_early.setOnClickListener(this);
        tv_absenteeism.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        cal = Calendar.getInstance();
        mCurrentMonth = cal.get(Calendar.MONTH) + 1;
        mCurrentYear = cal.get(Calendar.YEAR);
        tv_time.setText(mCurrentYear + "年" + mCurrentMonth + "日");
        mPrivateCode = AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();
        mPresenter.sendData(mPrivateCode, mCurrentYear + "", mCurrentMonth + "", MyAttendenceActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delay:
            case R.id.tv_leave_early:
            case R.id.tv_absenteeism:
                try {
                    Intent intent = new Intent(MyAttendenceActivity.this, AttandenceRecorderActivity.class);
                    intent.putExtra("date", tv_time.getText().toString());
                    if (mMyAttandanceResponse1 != null)
                        intent.putExtra("attandance", mMyAttandanceResponse1);
                    startActivity(intent);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_time:
                monthSelectPopWindow = new MonthSelectPopWindow(MyAttendenceActivity.this, new MonthSelectPopWindow.PopListener() {
                    @Override
                    public void cancle() {
                        monthSelectPopWindow.dismiss();
                    }

                    @Override
                    public void confirm(String year, String month) {
                        mCurrentYear = Integer.parseInt(year);
                        mCurrentMonth = Integer.parseInt(month);
                        tv_time.setText(year + "年" + month + "月");
                        mPresenter.sendData(mPrivateCode, mCurrentYear + "", mCurrentMonth + "", MyAttendenceActivity.this);
                        monthSelectPopWindow.dismiss();
                    }
                });
                monthSelectPopWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
                break;
            default:
                break;

        }
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void sendDataSuccess(MyAttandanceResponse myAttandanceResponse) {
        try {
            if (myAttandanceResponse != null) {
                mMyAttandanceResponse1 = myAttandanceResponse;
                //迟到
                String cdCount = myAttandanceResponse.getCdCount();
                tv_delay.setText(cdCount);
                //早退
                String ztCount = myAttandanceResponse.getZtCount();
                tv_leave_early.setText(ztCount);
                //旷工
                String kgCount = myAttandanceResponse.getKgCount();
                tv_absenteeism.setText(kgCount);
                int cd_count = Integer.parseInt(cdCount);
                int zt_count = Integer.parseInt(ztCount);
                int kg_count = Integer.parseInt(kgCount);
                if (cd_count > 0) {
                    tv_delay.setTextColor(getResources().getColor(R.color.pie_ring_five_level));
                } else {
                    tv_delay.setTextColor(getResources().getColor(R.color.grey));
                }
                if (zt_count > 0) {
                    tv_leave_early.setTextColor(getResources().getColor(R.color.pie_ring_five_level));
                } else {
                    tv_leave_early.setTextColor(getResources().getColor(R.color.grey));
                }
                if (kg_count > 0) {
                    tv_absenteeism.setTextColor(getResources().getColor(R.color.pie_ring_five_level));
                } else {
                    tv_absenteeism.setTextColor(getResources().getColor(R.color.grey));
                }
                tv_sign_card_num.setText("已签卡  " + myAttandanceResponse.getBillCount() + "  次");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDataFailed(String errCode, String msg) {
        Toast.makeText(MyAttendenceActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendDataFinish() {
    }
}
