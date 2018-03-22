package com.shanlinjinrong.oa.ui.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.my.contract.ModifyPhoneActivityContract;
import com.shanlinjinrong.oa.ui.activity.my.presenter.ModifyPhoneActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 修改电话 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/31.<br />
 */
public class ModifyPhoneActivity extends HttpBaseActivity<ModifyPhoneActivityPresenter> implements ModifyPhoneActivityContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar  toolbar;
    @BindView(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @BindView(R.id.user_phone)
    EditText userPhone;
    @BindView(R.id.ed_verify_code)
    EditText mEdVerifyCode;
    @BindView(R.id.request_verify_code)
    TextView mRequestVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        userPhone.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PHONE));
        userPhone.setSelection(userPhone.getText().length());
//        try {
//            //EditText 自动搜索,间隔->输入停止1秒后自动搜索
//            RxTextView.textChanges(userPhone)
//                    .debounce(500, TimeUnit.MILLISECONDS)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(charSequence -> check());
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
    }


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @OnClick({R.id.toolbar_text_btn, R.id.request_verify_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_text_btn:
                if (TextUtils.isEmpty(mEdVerifyCode.getText().toString().trim())) {
                    showToast("请输入验证码！");
                }
                if (!check()) {
                    return;
                }
                mPresenter.modifyPhone(userPhone.getText().toString().trim(), mEdVerifyCode.getText().toString().trim());
//                tvTips.setText("");
//                final String newNumber = userPhone.getText().toString();
//                //如果号码与之前的一样，不去访问网络
//                if (newNumber.equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PHONE))) {
//                    showToast("不能与之前的电话号码相同");
//                    return;
//                }
//                //修改按钮
//                if (check()) {
//                    showLoadingView();
//                    mPresenter.modifyPhone(newNumber);
//                }
                break;
            case R.id.request_verify_code:
                if (!check()) {
                    return;
                }
                Utils.countDown(mRequestVerifyCode);
                mPresenter.RequestVerifyCode(userPhone.getText().toString().trim());
                break;
            default:

                break;
        }

    }

    private boolean check() {
        if (TextUtils.isEmpty(userPhone.getText().toString().trim())) {
            showToast("请输入您的电话号码！");
            //tvTips.setText("请输入您的电话号码");
            return false;
        }
        if (!Utils.isRegex(Constants.Regex.REGEX_PHONE, (userPhone.getText()).toString())) {
            showToast("手机号码格式不正确！");
            //tvTips.setText("手机号码格式不正确");
            return false;
        }

        if (userPhone.getText().toString().trim().length() != 11) {
            return false;
        }
        //tvTips.setText("");
        return true;
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        //必须在setSupportActionBar之前调用
        setTitle("");
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("资料修改");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbarTextBtn.setText("修改");
        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void modifySuccess(String newNumber) {
        hideLoadingView();
        showToast("修改成功！");
        AppConfig.getAppConfig(ModifyPhoneActivity.this).set(AppConfig.PREF_KEY_PHONE, newNumber);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void modifyFailed(int errorCode, String msg) {
        hideLoadingView();
        showToast(msg);
    }

    @Override
    public void RequestVerifyCodeSuccess() {
        showToast("发送成功！");
    }

    @Override
    public void modifyFinish() {
        hideLoadingView();
    }
}