package com.shanlinjinrong.oa.ui.activity.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class ConfirmCompanyEmailActivity extends HttpBaseActivity<ConfirmEmailPresenter> implements View.OnTouchListener, ConfirmEmailContract.View {

    public static String EMAIL_STATUS = "email_status";
    public static String EMAIL_ADDRESS = "email_address";


    @Bind(R.id.ll_confirm_email)
    LinearLayout mConfirmEmailLayout;

    @Bind(R.id.ll_find_email_layout)
    LinearLayout mFindEmailLayout;

    @Bind(R.id.iv_icon)
    ImageView mIcon;

    @Bind(R.id.et_email)
    EditText mEmail;

    @Bind(R.id.tv_email_suffix)
    AutoCompleteTextView mEmailSuffix;

    @Bind(R.id.tv_email_address)
    TextView mUserEmail;

    @Bind(R.id.btn_sure)
    Button mSureBtn;

    private boolean mStatus; //是否查找到邮箱的状态
    private String mEmailAddress;


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
        mData.add("@qq.com");
        mData.add("@126.com");
        mData.add("@163.com");


        mEmailSuffix.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mData));

        mEmailSuffix.setOnTouchListener(this);

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
                // TODO: 2017/10/11
                mPresenter.sendEmail("", mEmailAddress);
            }
        });

        mStatus = getIntent().getBooleanExtra(EMAIL_STATUS, true);
        mEmailAddress = getIntent().getStringExtra(EMAIL_ADDRESS);
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
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // getCompoundDrawablesRelative() 可以获取一个长度为4的数组，
        // 存放drawableStart，Top，End, Bottom四个图片资源对象
        // index=2 表示的是 drawableEnd 图片资源对象
        Drawable[] drawables = mEmailSuffix.getCompoundDrawables();
        if (drawables[2] == null) {
            return false;
        }

        if (event.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }

        // drawable.getIntrinsicWidth() 获取drawable资源图片呈现的宽度
        if (event.getX() > mEmailSuffix.getWidth() - mEmailSuffix.getPaddingRight()
                - drawables[2].getIntrinsicWidth()) {
            if (mEmailSuffix.isPopupShowing()) {
                mEmailSuffix.dismissDropDown();
            } else {
                // 进入这表示图片被选中，可以处理相应的逻辑了
                mEmailSuffix.showDropDown();
            }

        }

        return false;
    }

    @Override
    public void sendEmailSuccess() {
        startActivity(new Intent(ConfirmCompanyEmailActivity.this, EmailConfirmFinishActivity.class));
    }

    @Override
    public void sendEmailFailed(int errorCode, String errMsg) {

    }

    @Override
    public void uidNull(int code) {

    }
}
