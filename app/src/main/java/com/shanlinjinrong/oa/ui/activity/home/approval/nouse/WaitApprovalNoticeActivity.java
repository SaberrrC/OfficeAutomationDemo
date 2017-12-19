package com.shanlinjinrong.oa.ui.activity.home.approval.nouse;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.approval.ApprovalNotice;
import com.shanlinjinrong.oa.ui.activity.home.approval.WaitApprovalReplyActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/16 11:20
 * Description:待我审批(公告申请)，现在我发起的还有我审批过的都用的这个界面
 */
public class WaitApprovalNoticeActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.empty_view_container)
    RelativeLayout mRootView;
    @BindView(R.id.tv_event_type)
    TextView tvEventType;
    @BindView(R.id.tv_inform_department)
    TextView tvInformDepartMent;
    @BindView(R.id.tv_inform_content)
    TextView tvInformContent;


    @BindView(R.id.tv_launch_date)
    TextView tvLaunchDate;
    @BindView(R.id.tv_launch_time)
    TextView tvLaunchTime;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_postil)
    TextView tvPostil;
    @BindView(R.id.ll_approval_launcher_info_layout)
    LinearLayout mLlApprovalLaunchInfoContainer;

    @BindView(R.id.ll_not_approval_operate_layout)
    LinearLayout mLlNotApprovalLaunchInfoContainer;

    private String appr_id;
    private String oa_id;
    private String oal_id;
    private String titleName;
    private boolean isCanReply;
    private long lastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_approval_notice);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initData();
    }

    private void initData() {
        appr_id = getIntent().getStringExtra("appr_id");
         isCanReply = getIntent().getBooleanExtra("isCanReply",false);
        oal_id = getIntent().getStringExtra("oal_id");
        titleName = getIntent().getStringExtra("titleName");

        mTvTitle.setText(titleName);

        loadData();
    }

    private void loadData() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("oal_id", oal_id);
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        initKjHttp().post(Api.APPROVAL_NOTICE_INFO, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                mRootView.setVisibility(View.VISIBLE);
                LogUtils.e(t);
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
                        case Api.APPROVAL_DETAIL_NOT_EXIST:
                            showEmptyView(mRootView, "审批详情不存在", 0, false);
                            catchWarningByCode(Api.getCode(jo));
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
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

        if (!isCanReply) {
            mLlNotApprovalLaunchInfoContainer.setVisibility(View.GONE);
        }

        final ApprovalNotice an = new ApprovalNotice(data);

        mLlApprovalLaunchInfoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime < 1000) {
                    lastClickTime = currentTime;
                    return;
                }
                showDetailDialog("0", an.getCreate_date(), an.getCreate_time(),
                        an.getUsername(), an.getTitle());
            }
        });

        tvEventType.setText("通知公告");
        tvInformDepartMent.setText(an.getOrgnames());
        tvInformContent.setText(an.getContent());

        tvLaunchDate.setText(an.getCreate_date());

        tvLaunchTime.setText(an.getCreate_time());
        tvName.setText(an.getUsername());
        tvPostil.setText(an.getTitle());

    }

    public void showDetailDialog(String status, String timeBefore, String timeAfter, String user, String reply) {
        View contentView = LayoutInflater.from(this).inflate(R.layout
                .approval_popupwindow_content_view, null);
        PopupWindow popupWindow = new PopupWindow(contentView, Utils.dip2px(156), Utils.dip2px(156), false);
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
                topTips.setTextColor(Color.parseColor("#548ee9"));
//公告审批特殊加了这个属性
                bottomTips.setTextColor(Color.parseColor("#548ee9"));
                bottomTips.setVisibility(View.VISIBLE);
                tvRemark.setVisibility(View.VISIBLE);
                break;
            case 1:
                contentView.setBackgroundResource(R.drawable.wait_approval_ll_yellow_bg);
                topTips.setText("审批中");
                topTips.setTextColor(Color.parseColor("#f69933"));
                bottomTips.setVisibility(View.GONE);
                tvDate.setVisibility(View.GONE);
                tvTime.setVisibility(View.GONE);
                tvName.setVisibility(View.GONE);
                tvRemark.setVisibility(View.GONE);
                break;
            case 2:
                contentView.setBackgroundResource(R.drawable.wait_approval_ll_green_bg);
                topTips.setText("审批");
                topTips.setTextColor(Color.parseColor("#5dc470"));
                bottomTips.setTextColor(Color.parseColor("#5dc470"));
                break;
            case 3:
                contentView.setBackgroundResource(R.drawable.wait_approval_ll_red_bg);
                topTips.setText("驳回");
                topTips.setTextColor(Color.parseColor("#ff6666"));
                bottomTips.setTextColor(Color.parseColor("#ff6666"));
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

    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);

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


    @OnClick({
            R.id.iv_approval_pass, R.id.iv_approval_reject
    })
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_approval_pass:
                Intent i = new Intent(WaitApprovalNoticeActivity.this, WaitApprovalReplyActivity.class);
                i.putExtra("appr_id", appr_id);
                i.putExtra("oa_id", oa_id);
                i.putExtra("isReject", false);
                startActivity(i);
                break;
            case R.id.iv_approval_reject:
                Intent i2 = new Intent(WaitApprovalNoticeActivity.this, WaitApprovalReplyActivity.class);
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
    }


}
