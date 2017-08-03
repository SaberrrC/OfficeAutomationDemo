package com.shanlin.oa.ui.activity;

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

import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.model.approval.ApprovalLeave;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.StringUtils;
import com.shanlin.oa.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/16 16:01
 * Description:我发起的(请假)
 */
public class MeLaunchLeaveActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rl_rootView)
    RelativeLayout mRootView;
    @Bind(R.id.iv_top_status)
    ImageView ivApprovalTopState;
    @Bind(R.id.tv_event_type)
    TextView tvEventType;

    @Bind(R.id.tv_duration)
    TextView tvDuration;
    @Bind(R.id.tv_reason)
    TextView tvReason;
    @Bind(R.id.tv_time_start)
    TextView tvTimeStart;
    @Bind(R.id.tv_time_end)
    TextView tvTimeEnd;
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
    private String appr_id;
    private String oal_id;
    private String status = "0";
    private String titleName;
    private PopupWindow popupWindow;
    private String oa_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_launch_leave);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initData();
    }

    private void initData() {
        appr_id = getIntent().getStringExtra("appr_id");
        oal_id = getIntent().getStringExtra("oal_id");
        oa_id = getIntent().getStringExtra("oa_id");
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
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("appr_id", appr_id);
        params.put("oal_id", oal_id);

        initKjHttp().post(Api.APPROVAL_INFO, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("MeLaunchLeaveActivity->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONObject data = Api.getDataToJSONObject(jo);
                            setDataForWidget(data);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            showEmptyView(mRootView, "内容为空", 0, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            showEmptyView(mRootView, "内容为空", 0, false);
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                showEmptyView(mRootView, "内容为空", 0, false);
                catchWarningByCode(errorNo);
            }
        });
    }

    private void setDataForWidget(JSONObject data) {
        mRootView.setVisibility(View.VISIBLE);
        mLlApprovalProcessLayout.setVisibility(View.VISIBLE);
        final ApprovalLeave al = new ApprovalLeave(data);
        tvEventType.setText(al.getInfo().getType());
        tvTimeStart.setText(al.getInfo().getStart_time() );
        tvTimeEnd.setText( al.getInfo().getEnd_time());
        tvDuration.setText(al.getInfo().getDay() + "小时");
        tvReason.setText(al.getInfo().getRemark().replace("&nbsp;", " ").replace("<br/>", "\n"));
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
            ApprovalLeave.ApproversList approvers = al.getApproversLists().get(i);
            //添加左侧审批图标
            addLeftStateImage(i, approvers);

            //添加右侧审批信息
            addRightInfo(i, approvers);

        }

    }

    private void addRightInfo(int i, final ApprovalLeave.ApproversList approvers) {
        View view = View.inflate(this, R.layout.approval_process_item_info_layout, null);
        TextView tvPostil=((TextView) view.findViewById(R.id.tv_postil));
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
        popupWindow = new PopupWindow(contentView, Utils.dip2px(200), Utils.dip2px(200), false);
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
        });popupWindow.setFocusable(true);

        popupWindow.setAnimationStyle(R.style.dialog_pop_anim_style);
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }


    /**
     * 添加左侧状态图片
     *
     * @param i
     * @param approvers
     */
    private void addLeftStateImage(int i, ApprovalLeave.ApproversList approvers) {
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
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }


}
