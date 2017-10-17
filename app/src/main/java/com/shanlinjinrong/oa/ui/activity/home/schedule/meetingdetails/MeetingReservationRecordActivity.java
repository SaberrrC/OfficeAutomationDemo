package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.MeetingReservationRecordAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingReservationRecordActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingReservationRecordActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 会议室 预订记录
 */
public class MeetingReservationRecordActivity extends HttpBaseActivity<MeetingReservationRecordActivityPresenter> implements MeetingReservationRecordActivityContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.rv_meeting_Reservation_Record)
    RecyclerView mRvMeetingReservationRecord;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @Bind(R.id.tv_empty_view)
    TextView mTvEmptyView;
    private int mPage;
    private int mNum;
    private MeetingReservationRecordAdapter mRecordAdapter;
    private List<ReservationRecordBean.DataBean> data = new ArrayList<>();
    public static MeetingReservationRecordActivity mRecordActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_reservation_record);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
    }

    private void initData() {
        mRecordActivity = this;
        refreshData();
        mTopView.getLeftView().setOnClickListener(this);
        mRecordAdapter = new MeetingReservationRecordAdapter(this, data);

        mRvMeetingReservationRecord.setLayoutManager(new LinearLayoutManager(this));
        mRvMeetingReservationRecord.setAdapter(mRecordAdapter);
        mRecordAdapter.notifyDataSetChanged();

        mRvMeetingReservationRecord.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (newState == 0 && data.size() > 10) {
                        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                        if (lastVisibleItemPosition == data.size() - 1) {
                            View view = View.inflate(MeetingReservationRecordActivity.this, R.layout.load_more_layout, null);
                            try {
                                if (!data.isEmpty())
                                    mRecordAdapter.addFooterView(view);
                                mRecordAdapter.notifyDataSetChanged();
                                LoadMore();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void refreshData() {
        mRefresh.setOnRefreshListener(this);
        mRefresh.setRefreshing(true);
        HttpParams httpParams = new HttpParams();
        mPage = 1;
        mNum = 15;
        mPresenter.getMeetingRecord(httpParams, mPage, mNum, false, data);
    }

    public void LoadMore() {
        HttpParams httpParams = new HttpParams();
        mPage++;
        mNum = 15;
        mPresenter.getMeetingRecord(httpParams, mPage, mNum, true, data);
    }

    @Override
    public void getMeetingRecordSuccess(List<ReservationRecordBean.DataBean> bean) {
        mRecordAdapter.setNewData(bean);
        mRecordAdapter.notifyDataSetChanged();
        mRefresh.setRefreshing(false);
        mRecordAdapter.removeAllFooterView();
    }

    @Override
    public void getMeetingRecordFailed(String msgStr) {
        mRefresh.setRefreshing(false);
        mRecordAdapter.removeAllFooterView();
    }

    @Override
    public void removeFooterView() {
        mRecordAdapter.removeAllFooterView();
        mRecordAdapter.notifyDataSetChanged();
        showToast("没有更多了");
    }

    @Override
    public void getMeetingRecordEmpty() {
        mRefresh.setRefreshing(false);
        mTvEmptyView.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MeetingDeleteSuccess(String str) {
        if (str.equals("meetingDeleteSuccess")) {
            mRefresh.setRefreshing(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

}
