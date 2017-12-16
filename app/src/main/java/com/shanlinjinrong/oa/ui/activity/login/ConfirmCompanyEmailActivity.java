package com.shanlinjinrong.oa.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.login.contract.ConfirmEmailContract;
import com.shanlinjinrong.oa.ui.activity.login.presenter.ConfirmEmailPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmCompanyEmailActivity extends HttpBaseActivity<ConfirmEmailPresenter> implements ConfirmEmailContract.View {

    public static String EMAIL_STATUS = "email_status";
    public static String EMAIL_ADDRESS = "email_address";


    @BindView(R.id.ll_confirm_email)
    LinearLayout mConfirmEmailLayout;

    @BindView(R.id.ll_find_email_layout)
    LinearLayout mFindEmailLayout;

    @BindView(R.id.iv_icon)
    ImageView mIcon;

    @BindView(R.id.et_email)
    EditText mEmail;

    @BindView(R.id.tv_email_suffix)
    AutoCompleteTextView mEmailSuffix;

    @BindView(R.id.tv_email_address)
    TextView mUserEmail;

    @BindView(R.id.btn_sure)
    Button mSureBtn;

    @BindView(R.id.iv_email_select_arrow)
    LinearLayout mEmailSelectIcon;

    private boolean mStatus; //是否查找到邮箱的状态
    private String mEmailAddress;
    private String userCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_company_email);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initView() {
        List<String> mData = new ArrayList<>();
        mData.add("@shanlinjinrong.com");
        mData.add("@shanlinbao.com");
        mData.add("@shanlincaifu.com");

        mEmailSuffix.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mData));

        mSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mStatus) {
                    if ((TextUtils.isEmpty(mEmail.getText().toString().trim()) || TextUtils.isEmpty(mEmailSuffix.getText().toString().trim()))) {
                        Toast.makeText(ConfirmCompanyEmailActivity.this, "请输入完整的邮箱格式！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        mEmailAddress = mEmail.getText().toString().trim() + mEmailSuffix.getText().toString().trim();
                    }
                }
                mPresenter.sendEmail(userCode, mEmailAddress);
            }
        });

        mStatus = getIntent().getBooleanExtra(EMAIL_STATUS, true);
        mEmailAddress = getIntent().getStringExtra(EMAIL_ADDRESS);
        userCode = getIntent().getStringExtra("code");
        if (mStatus) {
            mIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.find_email, null));
            mConfirmEmailLayout.setVisibility(View.GONE);
            mFindEmailLayout.setVisibility(View.VISIBLE);
            mUserEmail.setText(mEmailAddress);
        } else {
            mIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.not_find_email, null));
            mConfirmEmailLayout.setVisibility(View.VISIBLE);
            mFindEmailLayout.setVisibility(View.GONE);
        }

        mEmailSelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmailSuffix.isPopupShowing()) {
                    mEmailSuffix.dismissDropDown();
                } else {
                    // 进入这表示图片被选中，可以处理相应的逻辑了
                    mEmailSuffix.showDropDown();
                }
            }
        });
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
    public void uidNull(int code) {

    }
}
