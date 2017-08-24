package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.common.CommonTopView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.HourReportBean;

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
        if (TextUtils.isEmpty(mPlanWork.getText().toString()) && TextUtils.isEmpty(mRealWork.getText().toString()) &&
                TextUtils.isEmpty(mSelfEvaluate.getText().toString())) {
            super.onBackPressed();
        } else if (!TextUtils.isEmpty(mPlanWork.getText().toString()) && !TextUtils.isEmpty(mRealWork.getText().toString()) &&
                !TextUtils.isEmpty(mSelfEvaluate.getText().toString())) {
            setFinishResult();
        } else {
            showTip("是否放弃编辑", "确定", "取消");
        }
    }


    public void showTip(String msg, final String posiStr, String negaStr) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_editor, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(this,
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, posiStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
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
