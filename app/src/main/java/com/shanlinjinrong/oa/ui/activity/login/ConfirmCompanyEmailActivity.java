package com.shanlinjinrong.oa.ui.activity.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ConfirmCompanyEmailActivity extends BaseActivity implements View.OnTouchListener {

    public static String EMAIL_STATUS = "email_status";

    @Bind(R.id.ll_confirm_email)
    LinearLayout mConfirmEmailLayout;

    @Bind(R.id.et_email)
    EditText mEmail;

    @Bind(R.id.tv_email_suffix)
    AutoCompleteTextView mEmailSuffix;

    @Bind(R.id.btn_sure)
    Button mSureBtn;

    private boolean mStatus; //是否查找到邮箱的状态


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_company_email);
        ButterKnife.bind(this);
        initView();
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
                if (TextUtils.isEmpty(mEmail.getText().toString().trim()) || TextUtils.isEmpty(mEmailSuffix.getText().toString().trim())) {
                    Toast.makeText(ConfirmCompanyEmailActivity.this, "请输入完整的邮箱格式！", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ConfirmCompanyEmailActivity.this, mEmail.getText().toString().trim() + mEmailSuffix.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ConfirmCompanyEmailActivity.this, EmailConfirmFinishActivity.class));
                }
            }
        });

        mStatus = getIntent().getBooleanExtra(EMAIL_STATUS, true);
        if (mStatus) {
            mConfirmEmailLayout.setVisibility(View.VISIBLE);
        } else {
            mConfirmEmailLayout.setVisibility(View.GONE);
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
}
