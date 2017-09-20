package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorkReportCheckActivity extends BaseActivity {
    @Bind(R.id.report_check_list)
    RecyclerView mReportCheckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report_check);
        ButterKnife.bind(this);
        mReportCheckList.setLayoutManager(new LinearLayoutManager(this));
//        mReportCheckList.setAdapter();
    }


}
