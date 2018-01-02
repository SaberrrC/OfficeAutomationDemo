package com.shanlinjinrong.oa.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.login.contract.ConfirmEmailContract;
import com.shanlinjinrong.oa.ui.activity.login.presenter.ConfirmEmailPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.views.KeyboardLinearLayout;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmCompanyEmailActivity extends HttpBaseActivity<ConfirmEmailPresenter> implements ConfirmEmailContract.View {

    @BindView(R.id.iv_icon)
    ImageView    mIvIcon;
    @BindView(R.id.tv_phone_number)
    TextView     mTvPhoneNumber;
    @BindView(R.id.ll_find_email_layout)
    LinearLayout mLlFindEmailLayout;
    @BindView(R.id.ll_confirm_email)
    LinearLayout mLlConfirmEmail;
    @BindView(R.id.btn_sure)
    Button       mBtnSure;

    private String  mPhone;
    private boolean mIsStatus;
    private String  mUserCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_company_email);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mPhone = getIntent().getStringExtra(Constants.PHONE_NUMBER);
        mIsStatus = getIntent().getBooleanExtra(Constants.PHONE_STATUS, false);
        mUserCode = getIntent().getStringExtra(Constants.USER_CODE);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initView() {

        mBtnSure.setOnClickListener(v -> {
            if (!mIsStatus) {
                startActivity(new Intent(ConfirmCompanyEmailActivity.this, LoginActivity.class));
                AppManager.sharedInstance().finishAllActivity();
                return;
            }
            mPresenter.sendEmail(mUserCode);
        });
        mIvIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.find_email, null));
        if (mIsStatus) {
            mLlConfirmEmail.setVisibility(View.GONE);
            mLlFindEmailLayout.setVisibility(View.VISIBLE);
            mTvPhoneNumber.setText(mPhone);
        } else {
            //没有手机号
            mLlConfirmEmail.setVisibility(View.VISIBLE);
            mLlFindEmailLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void hideLoading() {
        hideLoadingView();
    }

    @Override
    public void sendEmailSuccess() {
        startActivity(new Intent(ConfirmCompanyEmailActivity.this, EmailConfirmFinishActivity.class));
    }

    @Override
    public void sendEmailFailed(int errorCode, String errMsg) {
        if (errorCode == -1) {
            showToast("网络出错，请稍后重试");
        } else {
            showToast(errMsg);
        }
    }

    @Override
    public void uidNull(String code) {
    }
}
