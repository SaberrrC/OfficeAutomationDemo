package com.shanlinjinrong.oa.ui.activity.upcomingtasks;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.adpter.FinalRecycleAdapter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingInfoDetailBodyBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingInfoStateBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingInfoTopBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingInfobottomBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksInfoContract;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter.UpcomingTasksInfoPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UpcomingTasksInfoActivity extends HttpBaseActivity<UpcomingTasksInfoPresenter> implements UpcomingTasksInfoContract.View, FinalRecycleAdapter.OnViewAttachListener {

    @Bind(R.id.tv_title)
    TextView     mTvTitle;
    @Bind(R.id.toolbar_text_btn)
    TextView     mToolbarTextBtn;
    @Bind(R.id.toolbar)
    Toolbar      mToolbar;
    @Bind(R.id.rv_content)
    RecyclerView mRecyclerView;
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
        setContentView(R.layout.activity_upcoming_tasks_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolbar();
        initList();
    }

    private void initList() {
        initData();
        Map<Class, Integer> map = new HashMap<>();
        map.put(UpcomingInfoTopBean.class, R.layout.layout_item_upcominginfo_top);
        map.put(UpcomingInfoStateBean.class, R.layout.commonality_initiate_approval_item);
        map.put(UpcomingInfoDetailBodyBean.class, R.layout.layout_item_upcominginfo_detail_body);
        map.put(UpcomingInfobottomBean.class, R.layout.layout_item_upcominginfoo_bottom);
        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFinalRecycleAdapter);
    }

    private void initData() {
        mDatas.add(new UpcomingInfoTopBean());
        mDatas.add(new UpcomingInfoStateBean());
        mDatas.add(new UpcomingInfoDetailBodyBean());
        mDatas.add(new UpcomingInfobottomBean());
    }

    private void initToolbar() {
        if (mToolbar == null) {
            return;
        }
        mToolbar.setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        mTvTitle.setText("朱展宏的出差申请");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mToolbarTextBtn.setVisibility(View.VISIBLE);
        mToolbarTextBtn.setText("评阅情况");
        mTvTitle.setLayoutParams(lp);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {

    }
}
