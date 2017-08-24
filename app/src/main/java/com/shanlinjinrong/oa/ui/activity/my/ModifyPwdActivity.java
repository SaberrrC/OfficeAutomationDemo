package com.shanlinjinrong.oa.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.login.LoginActivity;
import com.shanlinjinrong.oa.ui.activity.my.contract.ModifyPswActivityContract;
import com.shanlinjinrong.oa.ui.activity.my.presenter.ModifyPswActivityPresenter;
import com.shanlinjinrong.oa.ui.base.MyBaseActivity;
import com.shanlinjinrong.oa.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 修改密码 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/31.<br />
 */
public class ModifyPwdActivity extends MyBaseActivity<ModifyPswActivityPresenter> implements ModifyPswActivityContract.View {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_image_btn)
    ImageView toolbarBack;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.old_pwd)
    EditText oldPwd;
    @Bind(R.id.new_pwd)
    EditText newPwd;
    @Bind(R.id.new_pwd_confirm)
    EditText newPwdConfirm;
    @Bind(R.id.tv_original_pwd)
    TextView tvOriginalPwd;

    @Bind(R.id.tv_new_pwd)
    TextView tvNewPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


    @OnClick(R.id.toolbar_text_btn)
    public void onClick() {
        //完成按钮
        if (check()) {
            modifyPwd();
        }
    }

    private void modifyPwd() {
        showLoadingView();
        mPresenter.modifyPsw(AppConfig.getAppConfig(this).getDepartmentId(),
                oldPwd.getText().toString(),
                newPwd.getText().toString(),
                newPwdConfirm.getText().toString(),
                AppConfig.getAppConfig(ModifyPwdActivity.this).get(
                        AppConfig.PREF_KEY_USER_EMAIL));
    }

    private boolean check() {

        goneAllTipsTextView();

        if (oldPwd.getText().toString().equals("")) {
            tvOriginalPwd.setVisibility(View.VISIBLE);
            tvOriginalPwd.setText("请输入您的原密码");
            return false;
        }
        if (!Utils.isRegex(Constants.Regex.REGEX_PASSWORD, oldPwd.getText().toString())) {
            tvOriginalPwd.setVisibility(View.VISIBLE);
            tvOriginalPwd.setText("密码格式不正确，建议6–15位字母和数字");
            return false;
        }
        if (newPwd.getText().toString().equals("")) {
            tvNewPwd.setVisibility(View.VISIBLE);
            tvNewPwd.setText("请输入您的新密码");
            return false;
        }
        if (!Utils.isRegex(Constants.Regex.REGEX_PASSWORD, newPwd.getText().toString())) {
            tvNewPwd.setVisibility(View.VISIBLE);
            tvNewPwd.setText("密码格式不正确，建议6–15位字母和数字");
            return false;
        }
        if (newPwdConfirm.getText().toString().equals("")) {
            tvNewPwd.setVisibility(View.VISIBLE);
            tvNewPwd.setText("请再次输入您的新密码");
            return false;
        }
        if (!newPwd.getText().toString().equals(newPwdConfirm.getText().toString())) {
            tvNewPwd.setVisibility(View.VISIBLE);
            tvNewPwd.setText("两次输入密码不一致");
            return false;
        }
        if (newPwd.getText().toString().equals(oldPwd.getText().toString())) {
            tvNewPwd.setVisibility(View.VISIBLE);
            tvNewPwd.setText("不能与原始密码相同");
            return false;
        }
        return true;
    }

    private void goneAllTipsTextView() {
        tvOriginalPwd.setVisibility(View.GONE);
        tvNewPwd.setVisibility(View.GONE);
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("修改密码");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);
        toolbarTextBtn.setText("完成");
        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void modifySuccess() {
        showToast("修改密码成功,请重新登录！");
        startActivity(new Intent(ModifyPwdActivity.this, LoginActivity.class));
        AppConfig.getAppConfig(ModifyPwdActivity.this).clearLoginInfo();
        finish();
    }

    @Override
    public void modifyFailed(int errorCode, String msg) {
        showToast(msg);
    }

    @Override
    public void modifyFinish() {
        hideLoadingView();
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }
}