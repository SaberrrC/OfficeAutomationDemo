package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.WriteWeeklyNewspaperActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.ReportCheckAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.CheckReportItem;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.WorkReportCheckContract;
import com.shanlinjinrong.oa.ui.activity.home.workreport.presenter.WorkReportCheckPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.utils.DeviceUtil;
import com.shanlinjinrong.views.common.CommonTopView;
import com.shanlinjinrong.views.dialog.MaskDialog;
import com.shanlinjinrong.views.swipeRecycleview.SwipeItemClickListener;
import com.shanlinjinrong.views.swipeRecycleview.SwipeMenu;
import com.shanlinjinrong.views.swipeRecycleview.SwipeMenuBridge;
import com.shanlinjinrong.views.swipeRecycleview.SwipeMenuCreator;
import com.shanlinjinrong.views.swipeRecycleview.SwipeMenuItem;
import com.shanlinjinrong.views.swipeRecycleview.SwipeMenuItemClickListener;
import com.shanlinjinrong.views.swipeRecycleview.SwipeMenuRecyclerView;
import com.shanlinjinrong.views.swipeRecycleview.touch.OnItemMoveListener;
import com.shanlinjinrong.views.swipeRecycleview.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发给我的页面，日报周报审核工作
 */
public class WorkReportCheckActivity extends HttpBaseActivity<WorkReportCheckPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnItemMoveListener, WorkReportCheckContract.View {

    public static int FOR_RESULT_OK = 1 << 2;

    public static String HAS_EVALUATION = "has_evaluation";


    @Bind(R.id.report_check_list)
    SwipeMenuRecyclerView mReportCheckList;

    @Bind(R.id.report_no_check_list)
    SwipeMenuRecyclerView mReportNoCheckList;

    @Bind(R.id.tv_check_pending)
    RadioButton mTvCheckPending;

    @Bind(R.id.tv_checked)
    RadioButton mTvChecked;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.top_view)
    CommonTopView mTopView;

    private static int DEFAULT_PAGE_SIZE = 15;

    private int mReportStatus = 1;

    private int pageSize = DEFAULT_PAGE_SIZE;//页面数量

    private int checkPageNum = 1;//请求页

    private int noCheckPageNum = 1;//请求页

    private int timeType = 0;//时间类型

    private int reportType = 1;//发报类型 1:日报  2：周报

    private List<CheckReportItem> mReportCheckData;

    private List<CheckReportItem> mReportNoCheckData;

    private ReportCheckAdapter mCheckAdapter;

    private ReportCheckAdapter mNoCheckAdapter;

    private boolean mContentChange = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report_check);
        ButterKnife.bind(this);
        initListView();
        mRefreshLayout.setRefreshing(true);
        loadData(false);
        showMask();
    }

    private void showMask() {
        String SHOW_MASK = "show_mask";
        String IS_SHOW_MASK = "is_show_mask";
        SharedPreferences sp = getSharedPreferences(SHOW_MASK, MODE_PRIVATE);
        boolean isShowMask = sp.getBoolean(IS_SHOW_MASK, true);
        if (isShowMask) {
            new MaskDialog(this, R.mipmap.report_check_mask_image).show();
            sp.edit().putBoolean(IS_SHOW_MASK, false).apply();
        }
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initListView() {
        //初始化未审核list
        mReportNoCheckData = new ArrayList<>();
        mReportNoCheckList.setLayoutManager(new LinearLayoutManager(this));
        mReportNoCheckList.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.driver_line)));

        mReportNoCheckList.setSwipeItemClickListener(mItemClickListener); // Item点击。
        mReportNoCheckList.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mReportNoCheckList.setSwipeMenuCreator(mSwipeMenuCreator); // 菜单创建器。

        mReportNoCheckList.setLoadMoreListener(mLoadMoreListener);
        mReportNoCheckList.useDefaultLoadMore();//加载更多

        mReportNoCheckList.setOnItemMoveListener(this);// 监听拖拽和侧滑删除

        mReportNoCheckList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.work_report_check_list_item, mReportNoCheckList, false));

        mNoCheckAdapter = new ReportCheckAdapter(mReportNoCheckData);
        mReportNoCheckList.setAdapter(mNoCheckAdapter);


        //初始化已审核list
        mReportCheckData = new ArrayList<>();
        mReportCheckList.setLayoutManager(new LinearLayoutManager(this));
        mReportCheckList.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.driver_line)));

        mReportCheckList.setSwipeItemClickListener(mItemClickListener); // Item点击。

        mReportCheckList.setLoadMoreListener(mLoadMoreListener);
        mReportCheckList.useDefaultLoadMore();//加载更多

        mReportCheckList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.work_report_check_list_item, mReportCheckList, false));

        mCheckAdapter = new ReportCheckAdapter(mReportCheckData);
        mReportCheckList.setAdapter(mCheckAdapter);


        //下拉刷新
        mRefreshLayout.setOnRefreshListener(this);

        mTopView.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportType == 1) {
                    reportType = 2;
                } else {
                    reportType = 1;
                }
                mRefreshLayout.setRefreshing(true);
                mReportCheckList.setVisibility(View.GONE);
                mReportNoCheckList.setVisibility(View.GONE);
                //点击周报日报切换时，标记内容改变
                mContentChange = true;
                onRefresh();
            }
        });

    }

    private void showCheckList(boolean showCheck) {
        if (showCheck) {
            mReportCheckList.setVisibility(View.VISIBLE);
            mReportNoCheckList.setVisibility(View.GONE);
        } else {
            mReportCheckList.setVisibility(View.GONE);
            mReportNoCheckList.setVisibility(View.VISIBLE);
        }
    }

    /**
     * load data
     *
     * @param isMore ：is load more data?
     */
    private void loadData(boolean isMore) {
        if (!isMore) {
            noCheckPageNum = mReportStatus == 1 ? 0 : noCheckPageNum;
            checkPageNum = mReportStatus == 3 ? 0 : checkPageNum;
        }
        int pageNum = mReportStatus == 1 ? noCheckPageNum : checkPageNum;
        mPresenter.loadData(reportType, pageSize, pageNum, timeType, mReportStatus, isMore);
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
     * 菜单创建器。
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = DeviceUtil.dip2px(WorkReportCheckActivity.this, 80);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            SwipeMenuItem deleteItem = new SwipeMenuItem(WorkReportCheckActivity.this)
                    .setBackground(R.color.red)
                    .setText("退回")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };

    /**
     * Item点击监听。
     */
    private SwipeItemClickListener mItemClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            if (reportType == 1)//日报
                toCheckDailyReport(position);

            if (reportType == 2) {
                //周报
                toCheckWeekReport(position);
            }
        }
    };

    private void toCheckDailyReport(int position) {
        Intent intent = new Intent(WorkReportCheckActivity.this, CheckDailyReportActivity.class);
        int dailyId = mReportStatus == 1 ? mReportNoCheckData.get(position).getDailyId() : mReportCheckData.get(position).getDailyId();
        intent.putExtra("dailyid", dailyId);
        intent.putExtra("user_name", mReportNoCheckData.get(position).getReportAccount());
        if (mReportStatus == 3) {
            intent.putExtra(HAS_EVALUATION, true);
            intent.putExtra("user_name", mReportCheckData.get(position).getReportAccount());
        }
        startActivityForResult(intent, FOR_RESULT_OK);
    }


    private void toCheckWeekReport(int position) {
        Intent intent = new Intent(WorkReportCheckActivity.this, WriteWeeklyNewspaperActivity.class);
        int dailyId = mReportStatus == 1 ? mReportNoCheckData.get(position).getDailyId() : mReportCheckData.get(position).getDailyId();
        intent.putExtra("dailyid", dailyId);
        String userName = mReportStatus == 1 ? mReportNoCheckData.get(position).getReportAccount() : mReportCheckData.get(position).getReportAccount();
        intent.putExtra("user_name", userName);
        intent.putExtra("function", WriteWeeklyNewspaperActivity.FUNCTION_EVALUATION);
        if (mReportStatus == 3) {
            intent.putExtra("has_evaluation", true);
            intent.putExtra("user_name", mReportCheckData.get(position).getReportAccount());
        }
        startActivityForResult(intent, FOR_RESULT_OK);
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                mPresenter.rejectReport(reportType, mReportNoCheckData.get(adapterPosition).getDailyId(), adapterPosition);
            }
        }
    };

    /**
     * 切换已审核 待审核
     *
     * @param view
     */
    @OnClick({R.id.tv_check_pending, R.id.tv_checked})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_check_pending:
                mReportStatus = 1;
                showCheckList(false);
                //切换的时候如果没有数据或者有日报、周报按钮的切换，刷新数据
                if (mReportNoCheckData.size() == 0 || mContentChange) {
                    mContentChange = false;
                    mRefreshLayout.setRefreshing(true);
                    onRefresh();
                }
                break;
            case R.id.tv_checked:
                mReportStatus = 3;
                showCheckList(true);
                //切换的时候如果没有数据或者有日报、周报按钮的切换，刷新数据
                if (mReportCheckData.size() == 0 || mContentChange) {
                    mContentChange = false;
                    mRefreshLayout.setRefreshing(true);
                    onRefresh();
                }
                break;
        }

    }


    @Override
    public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
        return false;
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {

    }

    @Override
    public void loadDataSuccess(List<CheckReportItem> reports, int pageNum, boolean hasNextPage, boolean isLoadMore) {
        if (!isLoadMore) {
            if (mReportStatus == 1) {
                mReportNoCheckData.clear();
            } else {
                mReportCheckData.clear();
            }
        }

        if (mReportStatus == 1) {
            mReportNoCheckData.addAll(reports);
            mNoCheckAdapter.notifyDataSetChanged();
            this.noCheckPageNum = pageNum + 1;
            mReportNoCheckList.setVisibility(View.VISIBLE);
            mReportNoCheckList.loadMoreFinish(false, hasNextPage);
        } else {
            mReportCheckData.addAll(reports);
            mCheckAdapter.notifyDataSetChanged();
            this.checkPageNum = pageNum + 1;
            mReportCheckList.setVisibility(View.VISIBLE);
            mReportCheckList.loadMoreFinish(false, hasNextPage);
        }

    }

    @Override
    public void loadDataFailed(int errCode, String errMsg) {
//        mReportCheckList.loadMoreError(errCode, errMsg);
        if (errCode == -1) {
            showToast(getString(R.string.net_no_connection));
        } else {
            showToast(getString(R.string.data_load_error));
        }
    }

    @Override
    public void loadDataFinish() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataEmpty() {
        if (mReportStatus == 1) {
            mReportNoCheckData.clear();
            mNoCheckAdapter.notifyDataSetChanged();
            mReportNoCheckList.loadMoreFinish(true, false);
        } else {
            mReportCheckData.clear();
            mCheckAdapter.notifyDataSetChanged();
            mReportCheckList.loadMoreFinish(true, false);
        }

    }

    @Override
    public void rejectReportSuccess(int position) {
        mReportNoCheckData.remove(position);
        mNoCheckAdapter.notifyDataSetChanged();
        showToast("退回成功！");
    }

    @Override
    public void rejectReportFailed(int errCode, String errMsg) {
        showToast("退回失败！");
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(Api.RESPONSES_CODE_UID_NULL);
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FOR_RESULT_OK && resultCode == RESULT_OK) {
            boolean isEvaluationOk = data.getBooleanExtra("evaluation_ok", false);
            if (isEvaluationOk) {
                //日报审核成功时，标记内容改变
                mContentChange = true;
                onRefresh();
            }
        }
    }
}
