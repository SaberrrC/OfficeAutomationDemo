package com.shanlinjinrong.oa.ui.activity.home.approval;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.thirdParty.iflytek.IflytekUtil;
import com.shanlinjinrong.oa.ui.activity.home.approval.contract.WaitApprovalReplyContract;
import com.shanlinjinrong.oa.ui.activity.home.approval.presenter.WaitApprovalReplyPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/16 16:26
 * Description:待我审批回复
 */
public class WaitApprovalReplyActivity extends HttpBaseActivity<WaitApprovalReplyPresenter> implements View.OnClickListener, WaitApprovalReplyContract.View {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_voice_input)
    Button mBtnVoiceInput;
    @BindView(R.id.et_reply)
    EditText mEtReply;
    @BindView(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @BindView(R.id.tv_add_pic_tips)
    TextView tvAddPicTips;

    private String appr_id;
    private String oa_id;
    private boolean isReject;//是否是驳回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        setTranslucentStatus(this);
        initWidget();
        initData();
        if (isReject) {
            initToolBar("驳回回复");
        } else {
            initToolBar("通过回复");
        }
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initWidget() {
        tvAddPicTips.setVisibility(View.GONE);
    }

    private void initData() {
        appr_id = getIntent().getStringExtra("appr_id");
        oa_id = getIntent().getStringExtra("oa_id");
        isReject = getIntent().getBooleanExtra("isReject", false);
        mBtnVoiceInput.setOnClickListener(this);
    }

    private void initToolBar(String titleText) {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText(titleText);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);
        toolbarTextBtn.setText("完成");
        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbarTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishReply();
            }
        });
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!(mEtReply.getText().toString().equals("")))) {
                    showTip("是否放弃编辑", "确定", "取消");
                } else {
                    finish();
                }
            }
        });
    }

    public void showTip(String msg, final String posiStr, String negaStr) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_editor, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(this,
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, posiStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((!(mEtReply.getText().toString().equals("")))) {
                    showTip("是否放弃编辑", "确定", "取消");
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 回复
     */
    private void finishReply() {
        showLoadingView();
        mPresenter.finishReply(isReject, appr_id, oa_id, mEtReply.getText().toString().trim());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_voice_input:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        requestRunTimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionListener() {
            @Override
            public void onGranted() {
                IflytekUtil iflytekUtil = new IflytekUtil(WaitApprovalReplyActivity.this, mEtReply);
                iflytekUtil.showIatDialog();
            }

            @Override
            public void onDenied() {
                showToast("录音权限被拒绝！请手动设置");
            }
        });
    }


    @Override
    public void replySuccess() {
        showToast("发送成功");
        Intent intent = new Intent(WaitApprovalReplyActivity.this, ApprovalListActivity.class);
        intent.putExtra("oa_id", oa_id);
        intent.putExtra("whichList", 2);
        startActivity(intent);
        finish();
    }

    @Override
    public void replyFailed(int errorNo, String strMsg) {
        catchWarningByCode(errorNo);
    }

    @Override
    public void replyResponseOther(String strMsg) {
        showToast(strMsg);

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

