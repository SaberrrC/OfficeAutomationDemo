package com.shanlinjinrong.oa.ui.activity.upcomingtasks;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.adpter.FinalRecycleAdapter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksContract;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter.UpcomingTasksPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by saberrrc on 2017/10/26.
 */

public class UpcomingTasksActivity extends HttpBaseActivity<UpcomingTasksPresenter> implements UpcomingTasksContract.View, FinalRecycleAdapter.OnViewAttachListener {

    @Bind(R.id.tv_title)
    TextView           mTvTitle;
    @Bind(R.id.toolbar_image_btn)
    ImageView          mTolbarTextBtn;
    @Bind(R.id.toolbar)
    Toolbar            mToolbar;
    @Bind(R.id.et_content)
    EditText           mEtContent;
    @Bind(R.id.tv_approval)
    TextView           mTvApproval;
    @Bind(R.id.rv_list)
    RecyclerView       mRvList;
    @Bind(R.id.sr_refresh)
    SwipeRefreshLayout mSrRefresh;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mFinalRecycleAdapter;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(int code) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_tasks);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        init();
    }

    private void init() {
        initToolbar();
        initList();
        initPullToRefresh();
    }

    private void initPullToRefresh() {
        //下拉刷新颜色设置
        mSrRefresh.setColorSchemeColors(getResources().getColor(R.color.tab_bar_text_light));
        //下拉刷新监听
        mSrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrRefresh.setRefreshing(false);
            }
        });

    }

    private void initList() {
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        Map<Class, Integer> map = FinalRecycleAdapter.getMap();
        map.put(UpcomingTaskItemBean.class, R.layout.layout_item_upcoming_task);
        getData();
        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRvList.setAdapter(mFinalRecycleAdapter);
    }

    private void getData() {
        for (int i = 0; i < 3; i++) {
            mDatas.add(new UpcomingTaskItemBean());
        }
    }

    private void initToolbar() {
        if (mToolbar == null) {
            return;
        }
        mToolbar.setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        mTvTitle.setText("待办事宜");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mTolbarTextBtn.setImageResource(R.mipmap.upcoming_filter);
        mTolbarTextBtn.setVisibility(View.VISIBLE);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.toolbar_image_btn, R.id.tv_approval})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_image_btn:
                break;
            case R.id.tv_approval:
                break;
        }
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof UpcomingTaskItemBean) {

        }
    }
}
