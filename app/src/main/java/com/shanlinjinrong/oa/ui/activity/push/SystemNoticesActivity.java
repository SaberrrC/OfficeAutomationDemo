package com.shanlinjinrong.oa.ui.activity.push;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.SystemNotice;
import com.shanlinjinrong.oa.ui.activity.push.contract.SystemNoticesContract;
import com.shanlinjinrong.oa.ui.activity.push.presenter.SystemNoticesPresenter;
import com.shanlinjinrong.oa.ui.activity.push.adapter.SystemNoticeDetailAdapter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by CXP on Date: 2016/9/19 11:31.
 * Description:系统消息activity
 */
public class SystemNoticesActivity extends HttpBaseActivity<SystemNoticesPresenter> implements SystemNoticesContract.View {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notice_detail_recyclerView)
    RecyclerView mRecyclerView;
    private List<SystemNotice> list;
    private SystemNoticeDetailAdapter mAdapter;
    @BindView(R.id.layout_content)
    RelativeLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notice_detail);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWidget();
        loadData();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void loadData() {
        showLoadingView();
        mPresenter.loadData(AppConfig.getAppConfig(this).getDepartmentId());
    }

    private void setDataForRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SystemNoticeDetailAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 系统消息全部已读接口
     */
    private void readAllSystemNotices() {
        mPresenter.readAllSystemNotices(AppConfig.getAppConfig(this).getDepartmentId());
    }


    private void initWidget() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SystemNoticeDetailAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("系统消息");
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
    public void loadDataSuccess(List<SystemNotice> pushMessages) {
        list = pushMessages;
        mRecyclerView.requestLayout();
        mAdapter.notifyDataSetChanged();
        setDataForRecyclerView();
        readAllSystemNotices();
    }

    @Override
    public void loadDataEmpty() {
        showEmptyView(mRootView, "抱歉，暂时还未接收到消息",
                R.drawable.contacts_empty_icon, false);
    }


    @Override
    public void loadDataFailed(int errCode, String errMsg) {
        hideLoadingView();
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
    }

    @Override
    public void readSystemNoticesFailed(int errCode, String errMsg) {
        catchWarningByCode(errCode);
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }
}
