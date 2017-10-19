package com.shanlinjinrong.oa.ui.activity.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.User;
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

    private String mCode;//验证码内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_job_number);
        ButterKnife.bind(this);
        mPresenter.getIdentifyingCode();
        initView();
    }

    private void initView() {
        mJobNumber.addTextChangedListener(new TextChangeWatcher());
        mIdentifyingCode.addTextChangedListener(new TextChangeWatcher());
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
            if (!mIdentifyingCode.getText().toString().trim().equalsIgnoreCase(mCode)) {
                showToast("验证码不正确");
                mPresenter.getIdentifyingCode();
            } else {
                mPresenter.searchUser(mJobNumber.getText().toString().trim());
            }
        }
    }

    @Override
    public void getIdentifyingCodeSuccess(String picUrl, String mCode) {
        this.mCode = mCode;
//        if (BuildConfig.DEBUG) {
//            picUrl = Api.PHP_DEBUG_URL + picUrl;
//        } else {
//            picUrl = Api.PHP_URL + picUrl;
//        }

        picUrl = "data:image/gif;base64," + picUrl;
        Log.i("WriteJobNumberActivity", "mCode : " + mCode);
        Log.i("WriteJobNumberActivity", "picUrl : " + picUrl);

        mIdentifyingCodeImg.setImageBitmap(base64ToBitmap(picUrl));

        Log.i("WriteJobNumberActivity", "mCode : " + mCode);


//        Glide.with(this).load(picUrl).into(mIdentifyingCodeImg);


    }

    /**
     * base64转为bitmap
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        Bitmap bitmap = null;
        try {
            byte[] bytes = Base64.decode(base64Data.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    @Override
    public void getIdentifyingCodeFailed(int errorCode) {
        Log.i("WriteJobNumberActivity", "失败 : " + errorCode);
        mIdentifyingCodeImg.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.user_code_default, null));
    }

    @Override
    public void searchUserSuccess(User user) {
        String email = user.getEmail();
        Intent intent = new Intent(WriteJobNumberActivity.this, ConfirmCompanyEmailActivity.class);
        if (TextUtils.isEmpty(email)) {
            intent.putExtra(ConfirmCompanyEmailActivity.EMAIL_STATUS, false);
        } else {
            intent.putExtra(ConfirmCompanyEmailActivity.EMAIL_ADDRESS, email);
        }
        intent.putExtra("code", user.getCode());
        startActivity(intent);
    }

    @Override
    public void searchUserFailed(int errorCode, String errMsg) {
        mPresenter.getIdentifyingCode();
        showToast(errMsg);
    }

    @Override
    public void requestFinish() {

    }

    @Override
    public void uidNull(int code) {

    }

    class TextChangeWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(mJobNumber.getText().toString().trim()) || TextUtils.isEmpty(mIdentifyingCode.getText().toString().trim())) {
                mSureBtn.setEnabled(false);
            } else {
                mSureBtn.setEnabled(true);
            }
        }
    }
}
