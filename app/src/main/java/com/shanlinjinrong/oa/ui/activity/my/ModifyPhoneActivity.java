package com.shanlinjinrong.oa.ui.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 修改电话 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/31.<br />
 */
public class ModifyPhoneActivity extends HttpBaseActivity<ModifyPhoneActivityPresenter> implements ModifyPhoneActivityContract.View {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    @Bind(R.id.user_phone)
    EditText userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        userPhone.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PHONE));
        userPhone.setSelection(userPhone.getText().length());
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @OnClick(R.id.toolbar_text_btn)
    public void onClick() {
        tvTips.setText("");
        final String newNumber = userPhone.getText().toString();
        //如果号码与之前的一样，不去访问网络
        if (newNumber.equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PHONE))) {
            showToast("不能与之前的电话号码相同");
            return;
        }
        //修改按钮
        if (check()) {
            showLoadingView();
            mPresenter.modifyPhone(AppConfig.getAppConfig(this).getDepartmentId(), newNumber);
        }
    }

    private boolean check() {
        if (userPhone.getText().toString().equals("")) {
            tvTips.setText("请输入您的电话号码");
            return false;
        }
        if (!Utils.isRegex(Constants.Regex.REGEX_PHONE, (userPhone.getText()).toString())) {
            tvTips.setText("手机号码格式不正确");
            return false;
        }
        return true;
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void modifySuccess(String newNumber) {
        showToast("修改成功");
        AppConfig.getAppConfig(ModifyPhoneActivity.this).set(
                AppConfig.PREF_KEY_PHONE, newNumber);
        setResult(RESULT_OK);
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
}