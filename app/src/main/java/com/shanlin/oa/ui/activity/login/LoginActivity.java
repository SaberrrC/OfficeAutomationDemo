package com.shanlin.oa.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.oa.R;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.helper.DoubleClickExitHelper;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.model.User;
import com.shanlin.oa.ui.activity.login.contract.LoginActivityContract;
import com.shanlin.oa.ui.activity.login.presenter.LoginActivityPresenter;
import com.shanlin.oa.ui.activity.main.MainController;
import com.shanlin.oa.ui.base.MyBaseActivity;
import com.shanlin.oa.utils.AndroidAdjustResizeBugFix;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.NetWorkUtils;
import com.shanlin.oa.utils.Utils;
import com.shanlin.oa.views.KeyboardLayout;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 登录界面 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/30.<br />
 */
public class LoginActivity extends MyBaseActivity<LoginActivityPresenter> implements LoginActivityContract.View {
    @Bind(R.id.user_email)
    EditText userEmail;
    @Bind(R.id.user_pwd)
    EditText userPwd;
    @Bind(R.id.layout_root)
    KeyboardLayout mRootView;
    @Bind(R.id.login_scroll_view)
    ScrollView mScrollView;
    @Bind(R.id.cb_auto_login)
    CheckBox mCbAutoLogin;
    @Bind(R.id.tv_find_pwd)
    TextView mTvFindPwd;
    private DoubleClickExitHelper doubleClickExitHelper;
    private int UPDATE_RECYCLERVIEW_POSITION = 11;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_RECYCLERVIEW_POSITION) {
                mScrollView.scrollTo(0, Utils.dip2px(200));
            }
        }
    };
    private boolean isAutoLogin = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //CXP添加,修复软键盘adjustResize属性的bug
        AndroidAdjustResizeBugFix.assistActivity(this);
        initView();
        setListenerForWidget(); //cxp添加
        setTranslucentStatus(this);
        doubleClickExitHelper = new DoubleClickExitHelper(this);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initView() {
        LogUtils.e("LoginActivity:initView");
        mCbAutoLogin.setChecked(true);
        AppConfig.getAppConfig(LoginActivity.this).setAutoLogin(true);
        userEmail.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE));
        mTvFindPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindPassWordActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * cxp添加
     * 给根view添加布局大小改变的监听
     */
    private void setListenerForWidget() {
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                mRootView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = mRootView.getRootView().getHeight();
                int heightDifference = screenHeight - rect.bottom;
                RelativeLayout.LayoutParams layoutParams = (
                        RelativeLayout.LayoutParams) mScrollView.getLayoutParams();

                layoutParams.setMargins(0, 0, 0, heightDifference);

                mScrollView.requestLayout();
                mHandler.sendEmptyMessage(UPDATE_RECYCLERVIEW_POSITION);
            }
        });

        mCbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO isChecked
                if (isChecked) {
                    isAutoLogin = true;
                } else {
                    isAutoLogin = false;

                }
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        if (check()) {
            //cxp添加，检验通过，隐藏软键盘
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(userEmail.getWindowToken(), 0);
            showLoadingView();
            mPresenter.login(userEmail.getText().toString(), userPwd.getText().toString());
        }
    }

    private boolean check() {
        if (userEmail.getText().toString().equals("")) {
            showToast("请输入您的邮箱帐号");
            return false;
        }
        if (!Utils.isRegex(Constants.Regex.REGEX_EMAIL, userEmail.getText().toString())) {

            //判断是不是工号
            if (!Utils.isRegex(Constants.Regex.REGEX_CODE, userEmail.getText().toString())) {
                showToast("请您输入正确的邮箱帐号或者工号");
                return false;
            }
        }
        if (userPwd.getText().toString().equals("")) {
            showToast("请输入您的密码");
            return false;
        }
        if (!Utils.isRegex(Constants.Regex.REGEX_PASSWORD, userPwd.getText().toString())) {
            userPwd.setText("");
            showToast("密码错误，请重试");
            return false;
        }
        if (0 == NetWorkUtils.getAPNType(this)) {
            Toast.makeText(this, "请检测网络，当前网络不可用!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        doubleClickExitHelper.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void loginSuccess(JSONObject userInfo) {
        showToast(LoginActivity.this, "登录成功");
        //登录成功后清空原来的信息
        AppConfig.getAppConfig(LoginActivity.this).clearLoginInfo();
        //保存登录用户信息
        User user = new User(userInfo);
        LogUtils.e("user->" + user);
        AppConfig.getAppConfig(LoginActivity.this).set(user, isAutoLogin);
        startActivity(new Intent(LoginActivity.this, MainController.class));
        finish();
    }

    @Override
    public void loginFailed(int errorCode) {
        catchWarningByCode(errorCode);
    }

    @Override
    public void accountOrPswError(String msg) {
        userPwd.setText("");
        showToast(msg);
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
    }
}