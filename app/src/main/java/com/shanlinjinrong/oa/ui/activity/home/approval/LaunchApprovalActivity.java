package com.shanlinjinrong.oa.ui.activity.home.approval;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;

import com.shanlinjinrong.oa.BuildConfig;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.approval.adapter.LaunchApprovalListAdapter;
import com.shanlinjinrong.oa.ui.activity.home.approval.bean.LaunchApprovalItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.InitiateThingsRequestActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.listview.decoration.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//发起审批
public class LaunchApprovalActivity extends BaseActivity implements LaunchApprovalListAdapter.OnItemClickListener {
    @Bind(R.id.tv_title)
    TextView mTvTitle;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.rv_launch_approval)
    RecyclerView mLaunchApprovalList;//发起审批列表

    private List<LaunchApprovalItem> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_approval);
        setTranslucentStatus(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initToolBar();
        initListData();
        mLaunchApprovalList.setLayoutManager(new GridLayoutManager(LaunchApprovalActivity.this, 3));
        mLaunchApprovalList.addItemDecoration(new GridItemDecoration(LaunchApprovalActivity.this, R.drawable.driver_line));
        LaunchApprovalListAdapter approvalListAdapter = new LaunchApprovalListAdapter(mListData);
        approvalListAdapter.setOnItemClickListener(this);
        mLaunchApprovalList.setAdapter(approvalListAdapter);
    }

    private void initListData() {
        mListData = new ArrayList<>();
        mListData.add(new LaunchApprovalItem("出差申请", R.drawable.icon_on_business_request));
        mListData.add(new LaunchApprovalItem("加班申请", R.drawable.icon_over_time_work_request));
        mListData.add(new LaunchApprovalItem("休假申请", R.drawable.icon_annual_leave_request));
        mListData.add(new LaunchApprovalItem("签卡申请", R.drawable.icon_registration_card_request));
    }


    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("发起审批");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClicked(int position) {

        switch (position) {
            case 0:
                //出差申请
                Intent intent1 = new Intent(this, InitiateThingsRequestActivity.class);
                intent1.putExtra("type", 0);
                startActivity(intent1);
                break;
            case 1:
                //加班申请
                Intent intent2 = new Intent(this, InitiateThingsRequestActivity.class);
                intent2.putExtra("type", 1);
                startActivity(intent2);
                break;
            case 2:
                //休假申请
                Intent intent3 = new Intent(this, InitiateThingsRequestActivity.class);
                intent3.putExtra("type", 2);
                startActivity(intent3);
                break;
            case 3:
                //签卡申请
                Intent intent4 = new Intent(this, InitiateThingsRequestActivity.class);
                intent4.putExtra("type", 3);
                startActivity(intent4);
                break;
        }
    }
}
