package com.shanlinjinrong.oa.ui.activity.home.approval;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.approval.ApprovalOfficeSupplies;
import com.shanlinjinrong.oa.ui.activity.home.approval.contract.MeLaunchOfficesSuppliesContract;
import com.shanlinjinrong.oa.ui.activity.home.approval.presenter.MeLaunchOfficesSuppliesPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.StringUtils;
import com.shanlinjinrong.oa.utils.Utils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/16 16:01
 * Description:我发起的(办公用品)
 */
public class MeLaunchOfficesSuppliesActivity extends HttpBaseActivity<MeLaunchOfficesSuppliesPresenter> implements MeLaunchOfficesSuppliesContract.View {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rl_root_view)
    RelativeLayout mRootView;
    @Bind(R.id.iv_top_status)
    ImageView ivApprovalTopState;
    @Bind(R.id.tv_event_type)
    TextView tvEventType;
    @Bind(R.id.tv_application_date)
    TextView tvApprovalDate;
    @Bind(R.id.tv_approval_content)
    TextView mTvApprovalContent;
    private String appr_id;
    private String oal_id;
    @Bind(R.id.ll_approval_state_iv_container)
    LinearLayout mLlApprovalStateIvContainer;
    @Bind(R.id.ll_approval_info_container)
    LinearLayout mLlApprovalInfoContainer;
    @Bind(R.id.ll_approval_launcher_info_layout)
    LinearLayout mLlApprovalLaunchInfoContainer;
    @Bind(R.id.ll_approval_process_layout)
    LinearLayout mLlApprovalProcessLayout;
    @Bind(R.id.ll_not_approval_operate_layout)
    LinearLayout mLlApprovalOperateContainer;
    private String status = "0";
    private String titleName;
    private String oa_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_launch_offices_supplies);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initData();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        appr_id = getIntent().getStringExtra("appr_id");
        oal_id = getIntent().getStringExtra("oal_id");
        oa_id = getIntent().getStringExtra("oal_id");
        titleName = getIntent().getStringExtra("titleName");
        mTvTitle.setText(titleName);
        boolean isCanReply = getIntent().getBooleanExtra("isCanReply", false);
        if (isCanReply) {
            mLlApprovalOperateContainer.setVisibility(View.VISIBLE);
        } else {
            mLlApprovalOperateContainer.setVisibility(View.GONE);
        }
        loadData();
    }

    private void loadData() {
        showLoadingView();
        mPresenter.loadData(appr_id, oal_id);
    }

    private void setDataForWidget(JSONObject data) {
        mRootView.setVisibility(View.VISIBLE);
        mLlApprovalProcessLayout.setVisibility(View.VISIBLE);
        final ApprovalOfficeSupplies al = new ApprovalOfficeSupplies(data);
        tvEventType.setText("办公用品");
        mTvApprovalContent.setText(al.getInfo().getArticle_name().replace("&nbsp;", " ").replace("<br/>", "\n"));
        tvApprovalDate.setText(al.getInfo().getApplication_time());
        // 1审批中 2通过 3驳回
        status = al.getInfo().getStatus();
        switch (Integer.parseInt(status)) {
            case 1:
                ivApprovalTopState.setImageResource(R.drawable.approval_top_state_approvaling_image);

                break;
            case 2:
                ivApprovalTopState.setImageResource(R.drawable.approval_top_state_passed_image);
                break;
            case 3:
                ivApprovalTopState.setImageResource(R.drawable.approval_top_state_reject_image);

                break;
        }
        //设置发起人数据
        ((TextView) mLlApprovalLaunchInfoContainer.findViewById(R.id.tv_launch_date)).setText(al
                .getInfo().getTime_before());
        ((TextView) mLlApprovalLaunchInfoContainer.findViewById(R.id.tv_launch_time)).setText(al
                .getInfo().getTime_after());
        ((TextView) mLlApprovalLaunchInfoContainer.findViewById(R.id.tv_name)).setText(al.getInfo()
                .getUsername());
        TextView tvPostil = (TextView) mLlApprovalLaunchInfoContainer.findViewById(R.id.tv_postil);
        tvPostil.setTextColor(Color.parseColor("#0EA7ED"));
        tvPostil.setText("发起审批");

        mLlApprovalLaunchInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailDialog("0", al.getInfo().getTime_before(), al.getInfo().getTime_after(),
                        al.getInfo().getUsername(), "");
            }
        });

        //循环添加审批流程
        for (int i = 0; i < al.getApproversLists().size(); i++) {
            ApprovalOfficeSupplies.ApproversList approvers = al.getApproversLists().get(i);
            //添加左侧审批图标
            addLeftStateImage(i, approvers);

            //添加右侧审批信息
            addRightInfo(i, approvers);

        }


    }

    private void addRightInfo(int i, final ApprovalOfficeSupplies.ApproversList approvers) {
        View view = View.inflate(this, R.layout.approval_process_item_info_layout, null);
        TextView tvPostil = ((TextView) view.findViewById(R.id.tv_postil));
        switch (Integer.parseInt(approvers.getApprovalStatus())) {
            //状态 1审批中，2通过，3驳回
            case 1:
                view.setBackgroundResource(R.drawable.approval_item_approvaling_bg);
                tvPostil.setTextColor(Color.parseColor("#F7931E"));
                tvPostil.setText("审批中");
                break;
            case 2:
                view.setBackgroundResource(R.drawable.approval_item_passed_bg);
                tvPostil.setTextColor(Color.parseColor("#10C48B"));
                break;
            case 3:
                view.setBackgroundResource(R.drawable.approval_item_reject_bg);
                tvPostil.setTextColor(Color.parseColor("#C1272D"));
                break;

        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(43));
        if (i >= 0) {
            lp.setMargins(0, Utils.dip2px(26), 0, 0);
        }
        view.setLayoutParams(lp);
        if (!StringUtils.isBlank(approvers.getTime_before())) {
            (view.findViewById(R.id.tv_launch_date)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_launch_date)).setText(approvers.getTime_before());
        } else {
            (view.findViewById(R.id.tv_launch_date)).setVisibility(View.GONE);
        }
        if (!StringUtils.isBlank(approvers.getTime_after())) {
            (view.findViewById(R.id.tv_launch_time)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_launch_time)).setText(approvers.getTime_after());
        } else {
            (view.findViewById(R.id.tv_launch_time)).setVisibility(View.GONE);
        }
        if (!StringUtils.isBlank(approvers.getUser())) {
            (view.findViewById(R.id.tv_name)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_name)).setText(approvers.getUser());
        } else {
            (view.findViewById(R.id.tv_name)).setVisibility(View.GONE);
        }
        if (!StringUtils.isBlank(approvers.getReply())) {
            ((TextView) view.findViewById(R.id.tv_postil)).setText(approvers.getReply());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailDialog(approvers.getApprovalStatus(), approvers.getTime_before(), approvers.getTime_after(), approvers.getUser(), approvers.getReply());
            }
        });
        mLlApprovalInfoContainer.addView(view);
    }

    public void showDetailDialog(String status, String timeBefore, String timeAfter, String user, String reply) {
        View contentView = LayoutInflater.from(this).inflate(R.layout
                .approval_popupwindow_content_view, null);
        PopupWindow popupWindow = new PopupWindow(contentView, Utils.dip2px(200), Utils.dip2px(200), false);
        TextView topTips = (TextView) contentView.findViewById(R.id.tv_dialog_top_tips);
        TextView bottomTips = (TextView) contentView.findViewById(R.id.tv_dialog_bottom_tips);
        TextView tvDate = (TextView) contentView.findViewById(R.id.tv_dialog_date);
        TextView tvTime = (TextView) contentView.findViewById(R.id.tv_dialog_time);
        TextView tvName = (TextView) contentView.findViewById(R.id.tv_dialog_name);
        TextView tvRemark = (TextView) contentView.findViewById(R.id.tv_dialog_content);

        tvDate.setText(timeBefore);
        tvTime.setText(timeAfter);
        tvName.setText(user);
        tvRemark.setText(reply.replace("&nbsp;", " ").replace("<br/>", "\n"));
        switch (Integer.parseInt(status)) {
            //状态 0发起 1审批中，2通过，3驳回
            case 0:
                contentView.setBackgroundResource(R.drawable.wait_approval_ll_blue_bg);
                topTips.setText("发起");
                topTips.setTextColor(Color.parseColor("#0EA7ED"));
                bottomTips.setVisibility(View.GONE);
                tvRemark.setVisibility(View.GONE);
                break;
            case 1:
                contentView.setBackgroundResource(R.drawable.wait_approval_ll_yellow_bg);
                topTips.setText("审批中");
                topTips.setTextColor(Color.parseColor("#F7931E"));
                bottomTips.setVisibility(View.GONE);
                tvDate.setVisibility(View.GONE);
                tvTime.setVisibility(View.GONE);
                tvName.setVisibility(View.GONE);
                tvRemark.setVisibility(View.GONE);
                break;
            case 2:
                contentView.setBackgroundResource(R.drawable.wait_approval_ll_green_bg);
                topTips.setText("审批");
                topTips.setTextColor(Color.parseColor("#10C48B"));
                bottomTips.setTextColor(Color.parseColor("#10C48B"));
                break;
            case 3:
                contentView.setBackgroundResource(R.drawable.wait_approval_ll_red_bg);
                topTips.setText("驳回");
                topTips.setTextColor(Color.parseColor("#C1272D"));
                bottomTips.setTextColor(Color.parseColor("#C1272D"));
                break;
        }

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.dialog_pop_anim_style);
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }

    /**
     * 添加左侧状态图片
     *
     * @param i
     * @param approvers
     */
    private void addLeftStateImage(int i, ApprovalOfficeSupplies.ApproversList approvers) {
        ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.dip2px(43), Utils.dip2px(43));
        if (i >= 0) {
            lp.setMargins(0, Utils.dip2px(26), 0, 0);
        }
        iv.setLayoutParams(lp);
        switch (Integer.parseInt(approvers.getApprovalStatus())) {
            //状态 1审批中，2通过，3驳回
            case 1:
                iv.setImageResource(R.drawable.approval_state_approval_image);
                break;
            case 2:
                iv.setImageResource(R.drawable.approval_state_passed_image);
                break;
            case 3:
                iv.setImageResource(R.drawable.approval_state_reject_image);
                break;
        }

        mLlApprovalStateIvContainer.addView(iv);
    }

    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("我发起的");
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

    @OnClick({R.id.iv_approval_pass, R.id.iv_approval_reject})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_approval_pass:
                Intent i = new Intent(this, WaitApprovalReplyActivity.class);
                i.putExtra("appr_id", appr_id);
                i.putExtra("oa_id", oa_id);
                i.putExtra("isReject", false);
                startActivity(i);
                break;
            case R.id.iv_approval_reject:
                Intent i2 = new Intent(this, WaitApprovalReplyActivity.class);
                i2.putExtra("appr_id", appr_id);
                i2.putExtra("oa_id", oa_id);
                i2.putExtra("isReject", true);
                startActivity(i2);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @Override
    public void loadDataSuccess(JSONObject data) {
        setDataForWidget(data);
    }

    @Override
    public void loadDataFailed(int errorNo, String strMsg) {
        showEmptyView(mRootView, "内容为空", 0, false);
        catchWarningByCode(errorNo);
    }

    @Override
    public void loadDataEmpty() {
        showEmptyView(mRootView, "内容为空", 0, false);
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }
}
