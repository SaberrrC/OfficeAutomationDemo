package com.shanlinjinrong.oa.ui.activity.notice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.Notice;
import com.shanlinjinrong.oa.ui.activity.notice.contract.NoticeListContract;
import com.shanlinjinrong.oa.ui.activity.notice.presenter.NoticeListPresenter;
import com.shanlinjinrong.oa.ui.activity.notice.adapter.NoticeDetailAdapter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/18 16:45.
 * Description:公告通知列表
 */
public class NoticeListActivity extends HttpBaseActivity<NoticeListPresenter> implements SwipeRefreshLayout.OnRefreshListener, NoticeListContract.View {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.notice_detail_recyclerView)
    RecyclerView mRecyclerView;
    private ArrayList<Notice> list;
    private NoticeDetailAdapter mAdapter;
    @Bind(R.id.layout_content)
    RelativeLayout mRootView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private int limit = 10;//当前条目数量
    private int currentPage = 1;//当前界面起始页
    private boolean hasMore = true;//是否有更多
    private Integer NO_MORE_CONTENT = 0;//没有更多内容的标识
    private Integer LOAD_MORE_CONTENT = 1;//jia zai更多内容的标识
    private boolean isLoading = false;

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == NO_MORE_CONTENT) {
                showToast("没有更多了");
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (msg.what == LOAD_MORE_CONTENT) {
                loadData(false, true);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWidget();
        loadData(false, false);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initWidget() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#468bff"),
                Color.parseColor("#468bff"), Color.parseColor("#468bff"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        mAdapter = new NoticeDetailAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int i) {

                list.get(i).setStatus("1");
                adapter.notifyDataSetChanged();


                Intent intent = new Intent(NoticeListActivity.this,
                        NoticesSingleInfoActivity.class);
                intent.putExtra("singleNotice", list.get(i));
                startActivity(intent);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 0屏幕停止滚动；1:滚动且用户仍在触碰或手指还在屏幕上2：随用户的操作，屏幕上产生的惯性滑动；
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == 0 && list.size() > 9) {
                    hasMore = true;
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition == list.size() - 1) {
                        //看到了最后一条，显示加载更多的状态
                        View view = View.inflate(NoticeListActivity.this, R.layout.load_more_layout, null);

                        if (hasMore) {
                            //如果没有在加载，才去加载
                            if (!isLoading) {
                                isLoading = true;
                                mAdapter.addFooterView(view, list.size());
                                handler.sendEmptyMessageDelayed(LOAD_MORE_CONTENT, 1000);
                            }
                        } else {
                            handler.sendEmptyMessage(NO_MORE_CONTENT);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 1;
    }

    @Override
    public void onRefresh() {
        loadData(true, false);
    }

    /**
     * @param isPull 是否是下拉或加载更多
     */
    private void loadData(boolean isPull, final boolean loadMore) {
        if (isPull) {
            mSwipeRefreshLayout.setRefreshing(true);
            currentPage = 1;
            list.clear();
            hasMore = true;
            LogUtils.e("当前在下拉刷新-》" + currentPage);
        }
        if (!isPull && !loadMore) {
            list.clear();
            showLoadingView();
            currentPage = 1;
            LogUtils.e("当前是正常加载数据-》" + currentPage);
        }
        if (loadMore) {
            currentPage++;
            LogUtils.e("当前是加载更多数据-》" + currentPage);
        }
        mPresenter.loadData(AppConfig.getAppConfig(this).getDepartmentId(), limit, currentPage, loadMore);
    }


    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("公告通知");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public void loadDataSuccess(List<Notice> listNotices, boolean loadMore) {
        if (loadMore) {
            //如果是加载更多的话，需要将最后一个view移除了
            mAdapter.removeAllFooterView();
        }
        isLoading = false;

        list.addAll(listNotices);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadDataFinish() {
        hideLoadingView();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadDataFailed(int errCode, String msg) {
        catchWarningByCode(errCode);
    }

    @Override
    public void loadDataEmpty() {
        showEmptyView(mRootView, "抱歉，当前公告通知为空", 0, false);
    }

    @Override
    public void loadDataNoMore() {
        hasMore = false;
        mAdapter.removeAllFooterView();
        showToast("没有更多了");
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }
}
