package com.shanlinjinrong.oa.ui.activity.home.approval;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/15 9:58
 * Description:发起审批
 */
public class LaunchApprovalActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_approval);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
    }


    @OnClick({R.id.rl_leave, R.id.rl_travel, R.id.rl_spplies, R.id.rl_bleave, R.id.rl_overtime})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_leave:
//                intent = new Intent(this, ApplyForLeaveActivity.class);
                break;
            case R.id.rl_travel:
//                intent = new Intent(this, ApplyForTravelActivity.class);
                break;
            case R.id.rl_spplies:
                intent = new Intent(this, ApplyForOfficeSuppliesActivity.class);
                break;
            case R.id.rl_bleave:
//                intent = new Intent(this, ApplyForPublicOutActivity.class);
                break;
            case R.id.rl_overtime:
//                intent = new Intent(this, ApplyForOverTimeActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
