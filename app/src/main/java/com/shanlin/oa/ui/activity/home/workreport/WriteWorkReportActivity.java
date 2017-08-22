package com.shanlin.oa.ui.activity.home.workreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.shanlin.common.CommonTopView;
import com.shanlin.oa.R;
import com.shanlin.oa.ui.activity.home.workreport.bean.HourReportBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 丁 on 2017/8/21.
 * 填写日报页面
 */
public class WriteWorkReportActivity extends Activity {
    @Bind(R.id.et_plan_work)
    EditText mPlanWork;

    @Bind(R.id.et_real_work)
    EditText mRealWork;

    @Bind(R.id.et_self_evaluate)
    EditText mSelfEvaluate;

    @Bind(R.id.top_view)
    CommonTopView mTopView;


    private String mTopTitle; //标题
    private int mPosition; //条目位置


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_wrok_report);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mTopView.setAppTitle(extra.getString("title"));
            mPosition = extra.getInt("position");
            HourReportBean hourReportBean = extra.getParcelable("hour_report");
            if (hourReportBean != null) {
                mPlanWork.setText(hourReportBean.getWorkPlan());
                mRealWork.setText(hourReportBean.getRealWork());
                mSelfEvaluate.setText(hourReportBean.getSelfEvaluate());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(mPlanWork.getText().toString()) || TextUtils.isEmpty(mRealWork.getText().toString()) ||
                TextUtils.isEmpty(mSelfEvaluate.getText().toString())) {
            Toast.makeText(this, "数据填写不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        setFinishResult();
    }

    /**
     * 设置回调数据
     */
    private void setFinishResult() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putInt("position", mPosition);
        HourReportBean hourReportBean = new HourReportBean(mPlanWork.getText().toString(), mRealWork.getText().toString(), mSelfEvaluate.getText().toString());
        extras.putParcelable("hour_report", hourReportBean);
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }
}
