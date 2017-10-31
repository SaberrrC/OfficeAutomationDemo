package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.MapRenderer;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.contract.CreateMeetingContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.presenter.CreateMeetingPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.MyAttendenceActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter.MyAttendenceActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;

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

    private DatePicker picker;


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

    private void initData() {
        iv_state1.setOnClickListener(this);
        iv_state2.setOnClickListener(this);
        iv_state3.setOnClickListener(this);
        iv_state4.setOnClickListener(this);
        ll_time.setOnClickListener(this);

    }

    private void initView() {
        mTopView.setRightDrawable(getResources().getDrawable(R.mipmap.month_right));
        View rightView = mTopView.getRightView();
        rightView.setOnClickListener(view -> {
            Intent intent = new Intent(MyAttendenceActivity.this,AttandenceMonthActivity.class);
            startActivity(intent);

        });
    }


    @Override
    public void uidNull(int code) {

    }

    @Override
    public void sendDataSuccess() {

    }

    @Override
    public void sendDataFailed(int errCode, String msg) {

    }

    @Override
    public void sendDataFinish() {

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
                if (picker == null) {
                    picker = new DatePicker(MyAttendenceActivity.this, DatePicker.YEAR_MONTH);
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
                        tv_time.setText(year+"年"+month+"月");
                    }
                });
                picker.show();
                break;
            default:
                break;

        }
    }
}
