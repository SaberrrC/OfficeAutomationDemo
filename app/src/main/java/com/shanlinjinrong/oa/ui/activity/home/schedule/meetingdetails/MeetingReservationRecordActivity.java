package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.MeetingReservationRecordAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingReservationRecordActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter.MeetingReservationRecordActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.apache.commons.logging.Log;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.kymjs.kjframe.http.HttpParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 会议室 预订记录
 */
public class MeetingReservationRecordActivity extends HttpBaseActivity<MeetingReservationRecordActivityPresenter> implements MeetingReservationRecordActivityContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.top_view)
    CommonTopView      mTopView;
    @BindView(R.id.rv_meeting_Reservation_Record)
    RecyclerView       mRvMeetingReservationRecord;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.tv_empty_view)
    TextView           mTvEmptyView;
    @BindView(R.id.tv_meeting_Reservation_Record)
    TextView           mTvMeetingReservationRecord;
    private int mPage;
    private int mNum = 15;
    private MeetingReservationRecordAdapter mRecordAdapter;
    private List<ReservationRecordBean.DataBeanX.DataBean> mData = new ArrayList<>();
    private boolean             mIsLoadMore;
    private LinearLayoutManager mLinearLayoutManager;

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
        refreshData();
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_dark);
        mTopView.getLeftView().setOnClickListener(this);
        mRecordAdapter = new MeetingReservationRecordAdapter(this, mData);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvMeetingReservationRecord.setLayoutManager(mLinearLayoutManager);
        mRvMeetingReservationRecord.setAdapter(mRecordAdapter);
        mRvMeetingReservationRecord.addOnItemTouchListener(new OnItemClick());
        mRecordAdapter.notifyDataSetChanged();

        mRvMeetingReservationRecord.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int lastPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    if (newState == 0 && lastPosition + 1 == mData.size()) {
                        android.util.Log.d("12313213", "!@31232131312312321aa");
                        View view = View.inflate(MeetingReservationRecordActivity.this, R.layout.load_more_layout, null);
                        mRecordAdapter.addFooterView(view);
                        mRecordAdapter.notifyDataSetChanged();
                        LoadMore();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastPosition = mLinearLayoutManager.findLastVisibleItemPosition();
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
        mIsLoadMore = false;
        mPage = 1;
        mPresenter.getMeetingRecord(httpParams, mPage, mNum, false);
    }

    public void LoadMore() {
        HttpParams httpParams = new HttpParams();
        mIsLoadMore = true;
        mPage++;
        mPresenter.getMeetingRecord(httpParams, mPage, mNum, true);
    }

    @Override
    public void getMeetingRecordSuccess(ReservationRecordBean.DataBeanX bean) {
        try {
            if (bean.getData().size() == 0 && !mIsLoadMore) {
                mTvEmptyView.setVisibility(View.VISIBLE);
                mTvEmptyView.setText("暂无预订记录");
                mRefresh.setRefreshing(false);
                return;
            }

            if (!mIsLoadMore) {
                mData.clear();
            }

            mTvMeetingReservationRecord.setVisibility(View.VISIBLE);
            mTvEmptyView.setVisibility(View.GONE);
            mData.addAll(bean.getData());
            mRecordAdapter.setNewData(mData);
            mRefresh.setRefreshing(false);
            if (mRecordAdapter.getFooterLayoutCount() > 0)
                mRecordAdapter.removeAllFooterView();
            mRecordAdapter.notifyDataSetChanged();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMeetingRecordFailed(int errorCode, String msgStr) {
        mRefresh.setRefreshing(false);
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.net_no_connection));
                mTvEmptyView.setText(R.string.string_not_network);
                mTvEmptyView.setVisibility(View.VISIBLE);
                break;
            default:
                mTvEmptyView.setVisibility(View.GONE);
                break;
        }
        mRefresh.setRefreshing(false);
        mRecordAdapter.removeAllFooterView();
        mRvMeetingReservationRecord.requestLayout();
        mRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeFooterView() {
        mRecordAdapter.removeAllFooterView();
        mRvMeetingReservationRecord.requestLayout();
        mRecordAdapter.notifyDataSetChanged();
        showToast(getString(R.string.string_not_more));
    }

    @Override
    public void getMeetingRecordEmpty() {
        mRefresh.setRefreshing(false);
        mTvEmptyView.setVisibility(View.VISIBLE);
        mTvEmptyView.setText(R.string.string_meeting_record_not_available);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MeetingDeleteSuccess(String str) {
        if (str.equals("meetingDeleteSuccess")) {
            mRefresh.setRefreshing(true);
        }
        if (str.equals("finish")) {
            finish();
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
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    public class OnItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent(MeetingReservationRecordActivity.this, MeetingInfoFillOutActivity.class);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            long currentTime = DateUtils.getTimestampFromString(str, "yyyy年MM月dd日   HH:mm");
            int start_time = mData.get(i).getStart_time();
            long startTime = start_time * 1000L;
            final boolean isMeetingPast = currentTime < startTime;
            intent.putExtra("isWriteMeetingInfo", false);
            intent.putExtra("isMeetingPast", isMeetingPast);
            intent.putExtra("isMeetingRecord", true);
            intent.putExtra("roomId", mData.get(i).getRoom_id());
            intent.putExtra("id", mData.get(i).getId());
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
