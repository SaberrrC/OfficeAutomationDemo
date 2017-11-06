package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baidu.platform.comapi.map.E;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
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

    private AttandenceRecorderAdapter mAdapter;
    private List<MyAttandanceResponse.AllWorkAttendanceListBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance_recorder_list);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
    }


    private void initView() {
        mTvCalendarYear.setText("  " + getIntent().getStringExtra("date"));
        View leftView = mTopView.getLeftView();
        leftView.setOnClickListener(view -> {
            finish();
        });
    }

    private class AttandenceRecorderAdapter extends BaseQuickAdapter<MyAttandanceResponse.AllWorkAttendanceListBean> {

        public AttandenceRecorderAdapter(List<MyAttandanceResponse.AllWorkAttendanceListBean> data) {
            super(R.layout.work_month_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, MyAttandanceResponse.AllWorkAttendanceListBean bean) {
            baseViewHolder.setText(R.id.tv_name, bean.getPsname());
            baseViewHolder.setText(R.id.tv_date, bean.getCalendar());
            baseViewHolder.setText(R.id.tv_gowork_time, bean.getOnebegintime());
            baseViewHolder.setText(R.id.tv_off_gowork_time, bean.getTwoendtime());
            baseViewHolder.setText(R.id.tv_state, bean.getTbmstatus());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void AttandenceQueryData(MyAttandanceResponse bean) {
        List<MyAttandanceResponse.AllWorkAttendanceListBean> allWorkAttendanceList = bean.getAllWorkAttendanceList();
        for (int i = 0; i < allWorkAttendanceList.size(); i++) {
            if (allWorkAttendanceList.equals("[旷工]")) {
                allWorkAttendanceList.remove(i);
            }
        }
        mAdapter = new AttandenceRecorderAdapter(allWorkAttendanceList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
