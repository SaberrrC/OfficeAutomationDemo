package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.WriteWeeklyNewspaperActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.AllReportAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.MyLaunchReportItem;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.MyLaunchWorkReportContract;
import com.shanlinjinrong.oa.ui.activity.home.workreport.presenter.MyLaunchWorkReportPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;
import com.shanlinjinrong.views.swipeRecycleview.SwipeItemClickListener;
import com.shanlinjinrong.views.swipeRecycleview.SwipeMenuRecyclerView;
import com.shanlinjinrong.views.swipeRecycleview.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 丁 on 2017/9/21.
 * 我发起的日报列表
 */
public class MyLaunchWorkReportActivity extends HttpBaseActivity<MyLaunchWorkReportPresenter> implements MyLaunchWorkReportContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.all_report_list)
    SwipeMenuRecyclerView mAllReportList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_empty)
    TextView      mTvEmpty;

    private AllReportAdapter mAllReportAdapter;
    private List<MyLaunchReportItem> mItemList = new ArrayList<>();

    private int pageSize = 20;//页面数量

    private int pageNum = 1;//请求页

    private int timeType = 0;//时间类型

    private int reportType = 1;//发报类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lacunch_work_report);
        ButterKnife.bind(this);
        initListView();
        mRefreshLayout.setRefreshing(true);
        loadData(false);
    }

    /**
     * load data
     *
     * @param isMore ：is load more data?
     */
    private void loadData(boolean isMore) {
        if (reportType == 2)
            mPresenter.loadWeekReportList(pageSize, pageNum, timeType, isMore);
        else
            mPresenter.loadDailyReport(pageSize, pageNum, timeType, isMore);
    }

    private void initListView() {
        mRefreshLayout.setOnRefreshListener(this); // 刷新监听。
        mAllReportList.setLayoutManager(new LinearLayoutManager(this));
        mAllReportList.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.driver_line)));
        mAllReportList.setSwipeItemClickListener(mItemClickListener); // RecyclerView Item点击监听。

        mAllReportList.useDefaultLoadMore(); // 使用默认的加载更多的View。
        mAllReportList.setLoadMoreListener(mLoadMoreListener); // 加载更多的监听。

        //设置头部标题
        mAllReportList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.my_launch_report_list_item, mAllReportList, false));

        mAllReportAdapter = new AllReportAdapter(mItemList);
        mAllReportList.setAdapter(mAllReportAdapter);

        //标题栏右侧按钮
        mTopView.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportType == 1)
                    reportType = 2;
                else
                    reportType = 1;

                mAllReportList.setVisibility(View.GONE);
                mRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }


    /**
     * load more
     */
    private SwipeMenuRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeMenuRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            loadData(true);
        }
    };

    /**
     * Item点击监听。
     */
    private SwipeItemClickListener mItemClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            Intent intent = null;
            if (reportType == 1) {
                if (mItemList.get(position).getStatus() == 3) {
                    intent = new Intent(MyLaunchWorkReportActivity.this, CheckDailyReportActivity.class);
                    intent.putExtra("has_evaluation", true);
                    intent.putExtra("user_name", mItemList.get(position).getReportAccount());
                } else {
                    intent = new Intent(MyLaunchWorkReportActivity.this, WorkReportUpdateActivity.class);
                }
                intent.putExtra("dailyid", mItemList.get(position).getDailyId());
            } else if (reportType == 2) {
                intent = new Intent(MyLaunchWorkReportActivity.this, WriteWeeklyNewspaperActivity.class);
                intent.putExtra("function", WriteWeeklyNewspaperActivity.FUNCTION_EDIT);
                intent.putExtra("user_name", AppConfig.getAppConfig(MyLaunchWorkReportActivity.this).getPrivateName());
                intent.putExtra("dailyid", mItemList.get(position).getDailyId());
                if (mItemList.get(position).getStatus() == 3) {
                    intent.putExtra("has_evaluation", true);
                    intent.putExtra("function", WriteWeeklyNewspaperActivity.FUNCTION_EVALUATION);
                }
            }

            if (intent != null)
                startActivityForResult(intent, 100);
        }
    };

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(Api.RESPONSES_CODE_UID_NULL);
    }

    @Override
    public void loadDataSuccess(List<MyLaunchReportItem> reports, int pageNum, int pageSize, boolean hasNextPage, boolean isLoadMore) {
        mRefreshLayout.setVisibility(View.VISIBLE);
        if (!isLoadMore) {
            mItemList.clear();
            mAllReportList.setVisibility(View.VISIBLE);
        }
        mItemList.addAll(reports);
        mAllReportList.loadMoreFinish(false, hasNextPage);
        this.pageNum = pageNum + 1;
        mAllReportList.requestLayout();
        mAllReportAdapter.notifyDataSetChanged();
        if (mItemList.size() == 0) {
            mTvEmpty.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    public void loadDataFailed(String errCode, String errMsg) {
        mRefreshLayout.setVisibility(View.VISIBLE);
        if (errCode.equals("-1")) {
            if (errMsg.equals("auth error")) {
                catchWarningByCode(Api.RESPONSES_CODE_UID_NULL);
            } else {
                showToast(getString(R.string.net_no_connection));
            }
        } else {
            showToast("数据加载失败，请稍后重试！");
        }
        if (mItemList.size() == 0) {
            mTvEmpty.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    public void loadDataFinish() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
        if (mItemList.size() == 0) {
            mTvEmpty.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    public void loadDataEmpty() {
        mRefreshLayout.setVisibility(View.VISIBLE);
        mAllReportList.loadMoreFinish(true, false);
        if (mItemList.size() == 0) {
            mTvEmpty.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        loadData(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data.getBooleanExtra("refresh_data", false)) {
                onRefresh();
            }
        }
    }
}
