package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//考勤月历
public class AttandenceRecorderActivity extends BaseActivity {
    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.tv_calendar_year)
    TextView mTvCalendarYear;
    @Bind(R.id.recycler_view1)
    RecyclerView mRecyclerView;

    AttandenceRecorderAdapter mAdapter;
    List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance_recorder_list);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
        mTvCalendarYear.setText("  " + formatter.format(new Date()));

        mList.add("55");
        mList.add("55");
        mList.add("55");
        mList.add("55");
        mAdapter = new AttandenceRecorderAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();

    }

    private void initView() {
        View leftView = mTopView.getLeftView();
        leftView.setOnClickListener(view -> {
            finish();
        });
    }

    private class AttandenceRecorderAdapter extends BaseQuickAdapter<String> {

        public AttandenceRecorderAdapter(List<String> data) {
            super(R.layout.work_month_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, String str) {
            holder.setText(R.id.tv_date, "20155555");
            holder.setText(R.id.tv_name, "小明");
            holder.setText(R.id.tv_gowork_time, "asdah");
            holder.setText(R.id.tv_off_gowork_time, "asssd");
            holder.setText(R.id.tv_state, "加班");
        }

    }

}
