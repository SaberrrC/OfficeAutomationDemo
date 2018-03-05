package com.shanlinjinrong.oa.ui.activity.home.approval;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.approval.adapter.LaunchApprovalAdapter;
import com.shanlinjinrong.oa.ui.activity.home.approval.bean.LaunchApprovalItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.InitiateThingsRequestActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.listview.decoration.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//发起审批
public class LaunchApprovalActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_launch_approval)
    RecyclerView mLaunchApprovalList;

    @BindView(R.id.toolbar_image_btn)
    ImageView toolbarImageBtn;

    @BindView(R.id.rv_launch_office_supplies)
    RecyclerView mRvLaunchOfficeSupplies;

    private List<LaunchApprovalItem> mListData;
    private List<LaunchApprovalItem> mOfficeSupplies;


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

        mLaunchApprovalList.setLayoutManager(new GridLayoutManager(LaunchApprovalActivity.this, 4));
        mLaunchApprovalList.addItemDecoration(new GridItemDecoration(LaunchApprovalActivity.this, R.drawable.driver_line));
        LaunchApprovalAdapter approvalListAdapter = new LaunchApprovalAdapter(mListData);
        mLaunchApprovalList.addOnItemTouchListener(new OnHRClick());
        mLaunchApprovalList.setAdapter(approvalListAdapter);

        mRvLaunchOfficeSupplies.setLayoutManager(new GridLayoutManager(LaunchApprovalActivity.this, 4));
        mRvLaunchOfficeSupplies.addItemDecoration(new GridItemDecoration(LaunchApprovalActivity.this, R.drawable.driver_line));
        LaunchApprovalAdapter mOfficeSuppliesAdapter = new LaunchApprovalAdapter(mOfficeSupplies);
        mRvLaunchOfficeSupplies.addOnItemTouchListener(new OnAdministrationClick());
        mRvLaunchOfficeSupplies.setAdapter(mOfficeSuppliesAdapter);
    }

    private void initListData() {
        mListData = new ArrayList<>();
        mListData.add(new LaunchApprovalItem("出差申请", R.drawable.icon_on_business_request));
        mListData.add(new LaunchApprovalItem("加班申请", R.drawable.icon_over_time_work_request));
        mListData.add(new LaunchApprovalItem("休假申请", R.drawable.icon_annual_leave_request));
        mListData.add(new LaunchApprovalItem("签卡申请", R.drawable.icon_registration_card_request));

        mOfficeSupplies = new ArrayList<>();
        mOfficeSupplies.add(new LaunchApprovalItem("办公品申请", R.drawable.icon_launch_approval_supplies));
    }

    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        toolbarImageBtn.setVisibility(View.GONE);
        setTitle("");
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("发起申请");
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
    }

    class OnHRClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            switch (i) {
                case 0:
                    //出差申请
                    Intent intent1 = new Intent(LaunchApprovalActivity.this, InitiateThingsRequestActivity.class);
                    intent1.putExtra("type", 0);
                    startActivity(intent1);
                    break;
                case 1:
                    //加班申请
                    Intent intent2 = new Intent(LaunchApprovalActivity.this, InitiateThingsRequestActivity.class);
                    intent2.putExtra("type", 1);
                    startActivity(intent2);
                    break;
                case 2:
                    //休假申请
                    Intent intent3 = new Intent(LaunchApprovalActivity.this, InitiateThingsRequestActivity.class);
                    intent3.putExtra("type", 2);
                    startActivity(intent3);
                    break;
                case 3:
                    //签卡申请
                    Intent intent4 = new Intent(LaunchApprovalActivity.this, InitiateThingsRequestActivity.class);
                    intent4.putExtra("type", 3);
                    startActivity(intent4);
                    break;
                default:
                    break;
            }
        }
    }

    class OnAdministrationClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            switch (i) {
                case 0:
                    Intent intent = new Intent(LaunchApprovalActivity.this, OfficeSuppliesActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}
