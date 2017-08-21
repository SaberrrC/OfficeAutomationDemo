package com.shanlin.oa.ui.activity.home.workreport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.shanlin.oa.R;
import com.shanlin.oa.ui.activity.home.workreport.contract.WorkReportLaunchActivityContract;
import com.shanlin.oa.ui.activity.home.workreport.presenter.WorkReportLaunchActivityPresenter;
import com.shanlin.oa.ui.base.MyBaseActivity;

/**
 * create by lvdinghao
 * 发起日报 页面
 */
public class WorkReportLaunchActivity extends MyBaseActivity<WorkReportLaunchActivityPresenter> implements WorkReportLaunchActivityContract.View {

    // TODO: 2017/8/21 之前字段，先保留，应该不用多选
    public static final int REQUEST_CODE_MULTIPLE = 1;//多选，接收人

    public static final int WRITE_REPORT_OK = 100;//填写日报


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(int code) {

    }

    /**
     * 跳转填写时报信息
     */
    private void toWriteHourData() {
        Intent intent = new Intent(WorkReportLaunchActivity.this, WriteWorkReportActivity.class);
        startActivityForResult(intent, WRITE_REPORT_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == WRITE_REPORT_OK){

        }
    }
}
