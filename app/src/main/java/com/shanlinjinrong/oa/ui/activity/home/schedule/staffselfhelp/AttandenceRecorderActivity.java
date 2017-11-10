package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//考勤月历
public class AttandenceRecorderActivity extends BaseActivity {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_calendar_year)
    TextView mTvCalendarYear;
    @BindView(R.id.recycler_view1)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_attendence)
    TextView mTvEmptyAttendence;

    private AttandenceRecorderAdapter mAdapter;
    private List<MyAttandanceResponse.AllWorkAttendanceListBean> mAllWorkAttendanceList;
    private List<MyAttandanceResponse.AllWorkAttendanceListBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance_recorder_list);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        if (mAllWorkAttendanceList != null) {
            mAllWorkAttendanceList.clear();
        }
        try {
            MyAttandanceResponse attandance = (MyAttandanceResponse) getIntent().getSerializableExtra("attandance");
            if (attandance != null) {
                mAllWorkAttendanceList = attandance.getAllWorkAttendanceList();
                for (int i = 0; i < mAllWorkAttendanceList.size(); i++) {
                    try {
                        if (mAllWorkAttendanceList.get(i).getTbmstatus().equals("[出差]"))
                            continue;
                        mData.add(mAllWorkAttendanceList.get(i));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        mData.add(mAllWorkAttendanceList.get(i));
                    }
                }
                if (mAllWorkAttendanceList.size() > 0) {
                    mTvEmptyAttendence.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        mTvCalendarYear.setText("  " + getIntent().getStringExtra("date"));
        View leftView = mTopView.getLeftView();
        leftView.setOnClickListener(view -> {
            finish();
        });
        if (mAllWorkAttendanceList != null) {
            mAdapter = new AttandenceRecorderAdapter(mData);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class AttandenceRecorderAdapter extends BaseQuickAdapter<MyAttandanceResponse.AllWorkAttendanceListBean> {

        public AttandenceRecorderAdapter(List<MyAttandanceResponse.AllWorkAttendanceListBean> data) {
            super(R.layout.work_month_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, MyAttandanceResponse.AllWorkAttendanceListBean bean) {
            baseViewHolder.setVisible(R.id.ll_currentday_state,true);
            baseViewHolder.setVisible(R.id.tv_empty_layout,false);
            baseViewHolder.setText(R.id.tv_name, bean.getPsname());
            baseViewHolder.setText(R.id.tv_date, bean.getCalendar());
            baseViewHolder.setText(R.id.tv_gowork_time, bean.getOnebegintime());
            baseViewHolder.setText(R.id.tv_off_gowork_time, bean.getTwoendtime());
            baseViewHolder.setText(R.id.tv_state, bean.getTbmstatus());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
