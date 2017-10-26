package com.shanlinjinrong.oa.ui.activity.home.approval;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.approval.adapter.LaunchApprovalListAdapter;
import com.shanlinjinrong.oa.ui.activity.home.approval.bean.LaunchApprovalItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.AnnualLeaveRequestActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.MissionWorkRequestActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.OverTimeWorkingActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.RegistrationCardRequestActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.listview.decoration.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by lvdinghao 2016/11/15
 * Description:发起审批
 */
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
        mListData.add(new LaunchApprovalItem(getString(R.string.approval_office_supplies), R.drawable.icon_launch_approval_supplies));
        mListData.add(new LaunchApprovalItem("出差申请", R.drawable.icon_launch_approval_supplies));
//        mListData.add(new LaunchApprovalItem("加班申请", R.drawable.icon_launch_approval_supplies));
//        mListData.add(new LaunchApprovalItem("办休假申", R.drawable.icon_launch_approval_supplies));
//        mListData.add(new LaunchApprovalItem("签卡申请", R.drawable.icon_launch_approval_supplies));
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
                Intent intent0 = new Intent(this, ApplyForOfficeSuppliesActivity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1 = new Intent(this, MissionWorkRequestActivity.class);
                startActivity(intent1);
                break;
//            case 2:
//                Intent intent2 = new Intent(this, OverTimeWorkingActivity.class);
//                startActivity(intent2);
//                break;
//            case 3:
//                Intent intent3 = new Intent(this, AnnualLeaveRequestActivity.class);
//                startActivity(intent3);
//                break;
//            case 4:
//                Intent intent4 = new Intent(this, RegistrationCardRequestActivity.class);
//                startActivity(intent4);
//                break;
        }
    }
}
