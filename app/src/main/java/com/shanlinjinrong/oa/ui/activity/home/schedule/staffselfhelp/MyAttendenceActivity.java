package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的考勤
 */
public class MyAttendenceActivity extends HttpBaseActivity<MyAttendenceActivityPresenter> implements MyAttendenceActivityContract.View ,View.OnClickListener{

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.iv_state1)
    ImageView iv_state1;
    @Bind(R.id.iv_state2)
    ImageView iv_state2;
    @Bind(R.id.iv_state3)
    ImageView iv_state3;
    @Bind(R.id.iv_state4)
    ImageView iv_state4;
    @Bind(R.id.ll_time)
    LinearLayout ll_time;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.ll_rootView)
    LinearLayout mRootView;
    @Bind(R.id.tv_delay)
    TextView tv_delay;
    @Bind(R.id.tv_leave_early)
    TextView tv_leave_early;
    @Bind(R.id.tv_business_travel)
    TextView tv_business_travel;
    @Bind(R.id.tv_absenteeism)
    TextView tv_absenteeism;
    @Bind(R.id.tv_sign_card_num)
    TextView tv_sign_card_num;

    MonthSelectPopWindow monthSelectPopWindow;
    private Calendar cal;
    private int mCurrentYear;
    private int mCurrentMonth ;
    String mPrivateCode = "";

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
            Intent intent = new Intent(MyAttendenceActivity.this,AttandenceMonthActivity.class);
            startActivity(intent);

        });
    }
    private void initData() {
        iv_state1.setOnClickListener(this);
        iv_state2.setOnClickListener(this);
        iv_state3.setOnClickListener(this);
        iv_state4.setOnClickListener(this);
        ll_time.setOnClickListener(this);
        cal = Calendar.getInstance();
        mCurrentMonth=cal.get(Calendar.MONTH)+1;
        mCurrentYear=cal.get(Calendar.YEAR);
        tv_time.setText(mCurrentYear+"年"+mCurrentMonth+"日");

        mPrivateCode = AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();
        mPresenter.sendData(mPrivateCode,mCurrentYear+"", mCurrentMonth+"",MyAttendenceActivity.this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_state1:
            case R.id.iv_state2:
            case R.id.iv_state3:
            case R.id.iv_state4:
                Intent intent = new Intent(MyAttendenceActivity.this,AttandenceRecorderActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_time:
                 monthSelectPopWindow = new MonthSelectPopWindow(MyAttendenceActivity.this,
                         new MonthSelectPopWindow.PopListener() {
                    @Override
                    public void cancle() {
                        monthSelectPopWindow.dismiss();
                    }
                    @Override
                    public void confirm(String year, String month) {
                        mCurrentYear=Integer.parseInt(year);
                        mCurrentMonth=Integer.parseInt(month);
                        tv_time.setText(year+"年"+month+"月");
                        mPresenter.sendData(mPrivateCode,mCurrentYear+"", mCurrentMonth+"",MyAttendenceActivity.this);
                        monthSelectPopWindow.dismiss();
                    }
                });
                monthSelectPopWindow.showAtLocation(mRootView, Gravity.BOTTOM,0,0);
                break;
            default:
                break;

        }
    }

    @Override
    public void uidNull(int code) {

    }

    @Override
    public void sendDataSuccess(MyAttandanceResponse myAttandanceResponse) {
        String cdCount = myAttandanceResponse.getCdCount();
        tv_delay.setText(cdCount);
        String ztCount = myAttandanceResponse.getZtCount();
        tv_leave_early.setText(ztCount);
        String ccCount = myAttandanceResponse.getCcCount();
        tv_business_travel.setText(ccCount);
        String kgCount = myAttandanceResponse.getKgCount();
        tv_absenteeism.setText(kgCount);
        tv_sign_card_num.setText(myAttandanceResponse.getBillCount());
    }

    @Override
    public void sendDataFailed(String errCode, String msg) {
        Toast.makeText(MyAttendenceActivity.this,msg,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void sendDataFinish() {

    }



}
