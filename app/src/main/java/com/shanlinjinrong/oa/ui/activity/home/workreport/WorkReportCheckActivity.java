package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.ReportCheckAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.CheckReportItem;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.WorkReportCheckContract;
import com.shanlinjinrong.oa.ui.activity.home.workreport.presenter.WorkReportCheckPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.utils.DeviceUtil;
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

public class WorkReportCheckActivity extends HttpBaseActivity<WorkReportCheckPresenter> implements OnItemMoveListener, WorkReportCheckContract.View {
    @Bind(R.id.report_check_list)
    SwipeMenuRecyclerView mReportCheckList;

    @Bind(R.id.tv_check_pending)
    RadioButton mTvCheckPending;

    @Bind(R.id.tv_checked)
    RadioButton mTvChecked;

    private int mReportStatus = 1;

    private int pageSize = 15;//页面数量

    private int pageNum = 1;//请求页

    private int timeType = 0;//时间类型

    private int reportType = 0;//发报类型

    private List<CheckReportItem> mReportData;

    private ReportCheckAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report_check);
        ButterKnife.bind(this);
        initListView();
        loadData(false);

    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initListView() {
        mReportCheckList.setLayoutManager(new LinearLayoutManager(this));
        mReportData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mReportData.add(new CheckReportItem(1, "张三", "2017-09-08", "2017-09-08"));
        }

        mReportCheckList.setLayoutManager(new LinearLayoutManager(this));
        mReportCheckList.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.driver_line)));

        mReportCheckList.setSwipeItemClickListener(mItemClickListener); // Item点击。
        mReportCheckList.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mReportCheckList.setSwipeMenuCreator(mSwipeMenuCreator); // 菜单创建器。

        mReportCheckList.setOnItemMoveListener(this);// 监听拖拽和侧滑删除

        mReportCheckList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.work_report_check_list_item, mReportCheckList, false));

        mAdapter = new ReportCheckAdapter(mReportData);
        mReportCheckList.setAdapter(mAdapter);
    }

    /**
     * load data
     *
     * @param isMore ：is load more data?
     */
    private void loadData(boolean isMore) {
        mPresenter.loadData(reportType, pageSize, pageNum, timeType, mReportStatus, isMore);
    }

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
                    .setText("删除")
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
            toCheckDailyReport();
        }


    };

    private void toCheckDailyReport() {
        Intent intent = new Intent(WorkReportCheckActivity.this, CheckDailyReportActivity.class);
        startActivity(intent);
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
                showToast("删除item " + adapterPosition);
            }
        }
    };

    @OnClick({R.id.tv_check_pending, R.id.tv_checked})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_check_pending:
                mReportStatus = 1;
                break;
            case R.id.tv_checked:
                mReportStatus = 3;
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
    public void loadDataSuccess(List<CheckReportItem> reports, int pageNum, int pageSize, boolean hasNextPage, boolean isLoadMore) {

    }

    @Override
    public void loadDataFailed(int errCode, String errMsg) {

    }

    @Override
    public void loadDataFinish() {

    }

    @Override
    public void loadDataEmpty() {
    }

    @Override
    public void uidNull(int code) {

    }
}
