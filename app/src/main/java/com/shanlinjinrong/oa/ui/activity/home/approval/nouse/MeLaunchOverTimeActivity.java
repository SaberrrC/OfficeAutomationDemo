package com.shanlinjinrong.oa.ui.activity.home.approval.nouse;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.approval.ApprovalOverTime;
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.ui.activity.home.approval.WaitApprovalReplyActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.thirdParty.iflytek.IflytekUtil;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.StringUtils;
import com.shanlinjinrong.oa.utils.Utils;
import com.shanlinjinrong.pickerview.TimePickerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2017/3/1 10:57
 * Description:加班申请
 */
public class MeLaunchOverTimeActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_top_text_button)
    TextView mTvTopButton;
    @Bind(R.id.iv_top_status)
    ImageView mIvTopStatus;
    @Bind(R.id.iv_middle_status)
    ImageView mIvMiddleStatus;
    @Bind(R.id.et_change_reason)
    EditText mEtChangeReason;
    @Bind(R.id.btn_voice_input)
    Button btnVoiceInput;


    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.et_reality_over_time_duration)
    TextView etRealityOverTimeDuration;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.tv_over_time_stage)
    TextView tvOverTimeStage;
    @Bind(R.id.tv_over_time_duration)
    TextView tvOverTimeDuration;
    @Bind(R.id.tv_over_time_reason)
    TextView tvOverTimeReason;
    @Bind(R.id.tv_reality_start_time)
    TextView tvRealityStartTime;
    @Bind(R.id.tv_reality_end_time)
    TextView tvRealityEndTime;
    @Bind(R.id.tv_change_reason)
    TextView tvChangeReason;

    @Bind(R.id.rl_reality_time_container)
    RelativeLayout rlRealityTimeContainer;
    @Bind(R.id.ll_change_reason)
    LinearLayout llChangeReason;
    @Bind(R.id.ll_approval_launcher_info_layout)
    LinearLayout mLlApprovalLaunchInfoContainer;
    //
    @Bind(R.id.ll_approval_state_iv_container)
    LinearLayout mLlApprovalStateIvContainer;
    @Bind(R.id.ll_approval_info_container)
    LinearLayout mLlApprovalInfoContainer;
    @Bind(R.id.ll_not_approval_operate_layout)
    LinearLayout mLlApprovalOperateContainer;
    //

    @Bind(R.id.rl_root_view)
    RelativeLayout mRootView;

    private String appr_id;
    private String oal_id;
    private String status = "0";
    private String titleName;
    private PopupWindow popupWindow;
    private TimePickerView pvTime;
    private String oa_id;
    boolean noChangeState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_time);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        initWidget();
        initData();
    }

    private void initWidget() {
        mTvTopButton.setVisibility(View.GONE);
        mTvTopButton.setText("变更");
        mTvTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlRealityTimeContainer.setVisibility(View.VISIBLE);
                mIvMiddleStatus.setVisibility(View.GONE);
                llChangeReason.setVisibility(View.VISIBLE);
                if (mTvTopButton.getText().toString().trim().equals("提交")) {
                    editOverTime();
                } else {
                    mTvTopButton.setText("提交");
                }
            }
        });
        boolean isCanReply = getIntent().getBooleanExtra("isCanReply", false);
        if (isCanReply) {
            mLlApprovalOperateContainer.setVisibility(View.VISIBLE);
        } else {
            mLlApprovalOperateContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 变更加班
     */
    private void editOverTime() {
        if (judgeEditOverTime()) {
            sendEditData();
            LogUtils.e("sendEditData。。。");
        }
    }

    /**
     * 发送变更数据
     */
    private void sendEditData() {
        if (!checkChangeData()) return;
        HttpParams params = new HttpParams();
        params.put("cal_id", oal_id);
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("start_time", tvRealityStartTime.getText().toString());
        params.put("end_time", tvRealityEndTime.getText().toString());
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("time", etRealityOverTimeDuration.getText().toString());
        params.put("reason", mEtChangeReason.getText().toString());


        initKjHttp().post(Api.APPROVAL_EDITOVERTIME, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            showToast("变更成功");
                            finish();
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
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
                LogUtils.e(errorNo + strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }

    private boolean checkChangeData() {
        if (StringUtils.isBlank(mEtChangeReason.getText().toString())) {
            showToast("变更事由不能为空");
            return false;
        }
        if (tvRealityStartTime.getText().toString().equals("点击选择时间") || tvRealityEndTime
                .getText().toString().equals("点击选择时间")) {
            showToast("请选择变更时间");
            return false;
        }
        return true;
    }

    private boolean judgeEditOverTime() {
        if (tvRealityStartTime.getText().toString().equals("点击选择时间")
                || tvRealityEndTime.getText().toString().equals("点击选择时间")) {
            showToast("请选择要变更的时间");
            return false;
        }
        return true;
    }


    private void initData() {
        appr_id = getIntent().getStringExtra("appr_id");
        oal_id = getIntent().getStringExtra("oal_id");
        oa_id = getIntent().getStringExtra("oa_id");
        titleName = getIntent().getStringExtra("titleName");
        mTvTitle.setText(titleName);
        if (titleName.equals("我审批的")) {
            noChangeState = true;
            mTvTopButton.setVisibility(View.GONE);
        }
        loadData(false);
    }


    private void loadData(final boolean isTopButton) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("appr_id", appr_id);
        params.put("oal_id", oal_id);

        initKjHttp().post(Api.APPROVAL_APPROVALINFO, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("MeLaunchOverTimeActivity->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONObject data = Api.getDataToJSONObject(jo);
                            setDataForWidget(data);
                            if (isTopButton) {
                                changeTextAndShowSomeView();
                                btnVoiceInput.setVisibility(View.GONE);
                                mTvTopButton.setText("");
                                mEtChangeReason.setBackground(null);
                                tvRealityStartTime.setClickable(false);
                                tvRealityEndTime.setClickable(false);
                            }
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            showEmptyView(mRootView, "内容为空", 0, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            showEmptyView(mRootView, "内容为空", 0, false);
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
                LogUtils.e(errorNo + strMsg);
                showEmptyView(mRootView, "内容为空", 0, false);
                catchWarningByCode(errorNo);
            }
        });
    }

    private void setDataForWidget(JSONObject jo) {
        mRootView.setVisibility(View.VISIBLE);
        try {
            final JSONObject data = jo.getJSONObject("info");
            tvType.setText("加班");
            tvStartTime.setText(data.getString("start_time_plan_before") + " " + data.getString("start_time_plan_after"));
            tvEndTime.setText(data.getString("end_time_plan_before") + " " + data.getString("end_time_plan_after"));
            //设置加班类型
            setOverTimeType(data);
            //设置顶部状态
            setTopStatus(data);
            tvOverTimeDuration.setText(data.getString("time_plan") + "小时");
            tvOverTimeReason.setText(data.getString("reason_plan"));
            //设置是否可以变更的状态
            setCanEditStatus(data);


            //设置发起人数据
            final String time_before = data.getString("time_before");
            ((TextView) mLlApprovalLaunchInfoContainer.findViewById(R.id.tv_launch_date)).setText(time_before);
            final String time_after = data.getString("time_after");
            ((TextView) mLlApprovalLaunchInfoContainer.findViewById(R.id.tv_launch_time)).setText(time_after);
            final String username = data.getString("username");
            ((TextView) mLlApprovalLaunchInfoContainer.findViewById(R.id.tv_name)).setText(username);
            TextView tvPostil = (TextView) mLlApprovalLaunchInfoContainer.findViewById(R.id.tv_postil);
            tvPostil.setTextColor(Color.parseColor("#0EA7ED"));
            tvPostil.setText("发起审批");
            mLlApprovalLaunchInfoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetailDialog("0", time_before, time_after,
                            username, "");
                }
            });

            ApprovalOverTime al = new ApprovalOverTime(jo);
            //循环添加审批流程
            for (int i = 0; i < al.getApproversLists().size(); i++) {
                ApprovalOverTime.ApproversList approvers = al.getApproversLists().get(i);
                //添加左侧审批图标
                addLeftStateImage(i, approvers);

                //添加右侧审批信息
                addRightInfo(i, approvers);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加左侧状态图片
     *
     * @param i
     * @param approvers
     */
    private void addLeftStateImage(int i, ApprovalOverTime.ApproversList approvers) {
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
            case 4:
                iv.setImageResource(R.drawable.approval_state_launch_image);
                break;
        }

        mLlApprovalStateIvContainer.addView(iv);
    }

    private void addRightInfo(int i, final ApprovalOverTime.ApproversList approvers) {
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
            case 4://
                view.setBackgroundResource(R.drawable.approval_item_launch_bg);
                tvPostil.setTextColor(Color.parseColor("#0EA7ED"));
                tvPostil.setText("发起审批");
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
            case 4:
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
        popupWindow.setAnimationStyle(R.style.dialog_pop_anim_style);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }

    /**
     * 设置是否可以变更的状态
     *
     * @param data
     * @throws JSONException
     */
    private void setCanEditStatus(JSONObject data) throws JSONException {
        String can_edit = data.getString("can_edit");
        String status = data.getString("status");

        //加班变更后的状态（‘’，1审批中，2通过，3驳回）	string	不存在变更时为空
        if (status.equals("")) {

            rlRealityTimeContainer.setVisibility(View.GONE);
            llChangeReason.setVisibility(View.GONE);
        } else {
            switch (Integer.parseInt(status)) {
                case 1:
                    mIvMiddleStatus.setImageResource(R.drawable.approval_top_state_approvaling_image);
                    break;
                case 2:
                    mIvMiddleStatus.setImageResource(R.drawable.approval_top_state_passed_image);
                    break;
                case 3:
                    mIvMiddleStatus.setImageResource(R.drawable.approval_top_state_reject_image);
                    break;
            }
            rlRealityTimeContainer.setVisibility(View.VISIBLE);
            llChangeReason.setVisibility(View.VISIBLE);
        }

        if (!data.getString("start_time_before").equals("")) {
            rlRealityTimeContainer.setVisibility(View.VISIBLE);
            tvRealityStartTime.setTextColor(Color.parseColor("#333333"));
            tvRealityEndTime.setTextColor(Color.parseColor("#333333"));
            tvRealityStartTime.setText(data.getString("start_time_before") + " " + data.getString("start_time_after"));
            tvRealityEndTime.setText(data.getString("end_time_before") + " " + data.getString("end_time_after"));

            etRealityOverTimeDuration.setText(data.getString("time") + "小时");
            etRealityOverTimeDuration.setFocusable(false);
            etRealityOverTimeDuration.setBackground(null);

            llChangeReason.setVisibility(View.GONE);
            if (!data.getString("reason").equals("")) {
                llChangeReason.setVisibility(View.VISIBLE);
                tvChangeReason.setVisibility(View.VISIBLE);
                mEtChangeReason.setVisibility(View.GONE);
                btnVoiceInput.setVisibility(View.GONE);
                tvChangeReason.setText(data.getString("reason"));
            }
        }

        //是否显示变更按钮（1不显示，2显示）
        switch (Integer.parseInt(can_edit)) {
            case 1:
                mTvTopButton.setVisibility(View.GONE);
                break;
            case 2:
                if (!noChangeState) {

                    mTvTopButton.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void setTopStatus(JSONObject data) throws JSONException {
        String status_plan = data.getString("status_plan");
        // 状态（1审批中，2通过，3驳回）
        switch (Integer.parseInt(status_plan)) {
            case 1:
                mIvTopStatus.setImageResource(R.drawable.approval_top_state_approvaling_image);
                break;
            case 2:
                mIvTopStatus.setImageResource(R.drawable.approval_top_state_passed_image);
                break;
            case 3:
                mIvTopStatus.setImageResource(R.drawable.approval_top_state_reject_image);
                break;
        }
    }

    private void setOverTimeType(JSONObject data) throws JSONException {
        //加班时段（1工作日加班，2周休加班，3法定节假日加班，4其他假日加班）
        String type = data.getString("type");
        String strType = "";
        switch (Integer.parseInt(type)) {
            case 1:
                strType = "工作日加班";
                break;
            case 2:
                strType = "周休加班";
                break;
            case 3:
                strType = "法定节假日加班";
                break;
            case 4:
                strType = "其他假日加班";
                break;
        }
        tvOverTimeStage.setText(strType);
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

    @OnClick({R.id.tv_reality_end_time, R.id.btn_voice_input, R.id.iv_approval_pass, R.id.iv_approval_reject,
            R.id.tv_top_text_button, R.id.iv_top_toolbar_back, R.id.tv_reality_start_time,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_approval_pass:
                Intent i = new Intent(MeLaunchOverTimeActivity.this, WaitApprovalReplyActivity.class);
                i.putExtra("appr_id", appr_id);
                i.putExtra("oa_id", oa_id);
                i.putExtra("isReject", false);
                startActivity(i);
                break;
            case R.id.iv_approval_reject:
                Intent i2 = new Intent(MeLaunchOverTimeActivity.this, WaitApprovalReplyActivity.class);
                i2.putExtra("appr_id", appr_id);
                i2.putExtra("oa_id", oa_id);
                i2.putExtra("isReject", true);
                startActivity(i2);
                break;
            case R.id.iv_top_toolbar_back:
                finish();
                break;
            case R.id.tv_reality_start_time:
                showDoneDatePicker(tvRealityStartTime, false);
                break;
            case R.id.tv_reality_end_time:
                showDoneDatePicker(tvRealityEndTime, true);
                break;
            case R.id.btn_voice_input:
                showDialog();
                break;
            case R.id.tv_top_text_button:
                loadData(true);
                break;
        }
    }

    private void showDialog() {
        requestRunTimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionListener() {
            @Override
            public void onGranted() {
                IflytekUtil iflytekUtil = new IflytekUtil(MeLaunchOverTimeActivity.this, mEtChangeReason);
                iflytekUtil.showIatDialog();
            }

            @Override
            public void onDenied() {
                showToast("录音权限被拒绝！请手动设置");
            }
        });

    }

    private void showDoneDatePicker(final TextView tv, final boolean isEndTime) {
        if (pvTime == null) {
            pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        }
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 1, calendar.get(Calendar.YEAR) + 1);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tv.setText(DateUtils.getTime(date));
                if (isEndTime) {
                    boolean isOrder = DateUtils.judeDateOrder(tvRealityStartTime.getText().toString(), DateUtils.getTime(date));
                    if (isOrder) {
                        int hourD_value = DateUtils.getHourD_Value(tvRealityStartTime.getText().toString(), DateUtils.getTime(date));
                        etRealityOverTimeDuration.setText(String.valueOf(hourD_value));
                        etRealityOverTimeDuration.setFocusable(true);
                        etRealityOverTimeDuration.setFocusableInTouchMode(true);
                        etRealityOverTimeDuration.requestFocus();
                        mTvTopButton.setText("提交");
                    } else {
                        showToast("结束时间必须晚于开始时间");
                        tv.setText("点击选择时间");

                    }
                }
            }
        });
        pvTime.show();

    }

    private void changeTextAndShowSomeView() {

        rlRealityTimeContainer.setVisibility(View.VISIBLE);
        llChangeReason.setVisibility(View.VISIBLE);
    }
}
