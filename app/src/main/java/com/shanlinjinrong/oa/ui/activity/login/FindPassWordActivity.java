package com.shanlinjinrong.oa.ui.activity.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.ui.activity.login.contract.FindPassWordContract;
import com.shanlinjinrong.oa.ui.activity.login.presenter.FindPasswordPresenter;
import com.shanlinjinrong.oa.ui.base.MyBaseActivity;
import com.shanlinjinrong.oa.utils.AndroidAdjustResizeBugFix;
import com.shanlinjinrong.oa.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找回密码
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/9 15:59
 * Description:
 */
public class FindPassWordActivity extends MyBaseActivity<FindPasswordPresenter> implements FindPassWordContract.View {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_email)
    EditText mEtEmail;
    @Bind(R.id.tv_reminder)
    TextView mEtReminder;
    @Bind(R.id.btn_get_pwd)
    TextView mBtnGetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        initToolBar();
        //CXP添加,修复软键盘adjustResize属性的bug
        AndroidAdjustResizeBugFix.assistActivity(this);
        setTranslucentStatus(this);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("找回密码");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_get_pwd)
    public void onClick() {
        if (check()) {
            showLoadingView();
            mPresenter.findPassword(mEtEmail.getText().toString().trim());
        }

    }

    private boolean check() {
        if (mEtEmail.getText().toString().equals("")) {
            showToast("请输入您的邮箱帐号");
            return false;
        }
        if (!Utils.isRegex(Constants.Regex.REGEX_EMAIL, mEtEmail.getText().toString())) {
            showToast("请您输入正确的邮箱帐号");
            return false;
        }

        return true;
    }

    @Override
    public void findSuccess() {
        mEtReminder.setText("随机密码已经发送至您的邮箱，请登录后设置新的密码");
        mBtnGetPwd.setClickable(false);
        mBtnGetPwd.setBackgroundColor(Color.parseColor("#999999"));
    }

    @Override
    public void findFinish() {
        hideLoadingView();
    }

    @Override
    public void findFailed() {
        mEtReminder.setText("邮件发送失败,请重试");
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }
}
