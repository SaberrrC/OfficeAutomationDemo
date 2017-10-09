package com.shanlinjinrong.oa.ui.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.ui.activity.login.contract.WriteJobNumberContract;
import com.shanlinjinrong.oa.ui.activity.login.presenter.WriteJobNumberPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteJobNumberActivity extends HttpBaseActivity<WriteJobNumberPresenter> implements WriteJobNumberContract.View {

    @Bind(R.id.et_job_number)
    EditText mJobNumber;

    @Bind(R.id.et_identifying_code)
    EditText mIdentifyingCode;

    @Bind(R.id.iv_identifying_code)
    ImageView mIdentifyingCodeImg;

    @Bind(R.id.btn_sure)
    Button mSureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_job_number);
        ButterKnife.bind(this);
        mPresenter.getIdentifyingCode();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @OnClick(value = {R.id.iv_identifying_code, R.id.btn_sure})
    void onClicked(View v) {
        //点击验证码图片
        if (v.getId() == R.id.iv_identifying_code) {
            mPresenter.getIdentifyingCode();
        }

        //点击确定按钮
        if (v.getId() == R.id.btn_sure) {

        }
    }

    @Override
    public void getIdentifyingCodeSuccess(String picUrl) {
        Glide.with(this).load(ApiJava.BASE_URL + picUrl).into(mIdentifyingCodeImg);
    }

    @Override
    public void getIdentifyingCodeFailed(int errorCode) {

    }

    @Override
    public void uidNull(int code) {

    }
}
