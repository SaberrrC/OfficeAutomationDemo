package com.shanlinjinrong.oa.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
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

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.helper.DoubleClickExitHelper;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.activity.login.contract.LoginActivityContract;
import com.shanlinjinrong.oa.ui.activity.login.presenter.LoginActivityPresenter;
import com.shanlinjinrong.oa.ui.activity.main.MainActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.AndroidAdjustResizeBugFix;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.NetWorkUtils;
import com.shanlinjinrong.oa.utils.Utils;
import com.shanlinjinrong.oa.views.KeyboardLayout;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.common.Api.RESPONSES_CODE_ACCOUNT_PASSWORD_ERROR;

/**
 * <h3>Description: 登录界面 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/30.<br />
 */
public class LoginActivity extends HttpBaseActivity<LoginActivityPresenter> implements LoginActivityContract.View, View.OnKeyListener {
    @BindView(R.id.user_email)
    EditText       userEmail;
    @BindView(R.id.user_pwd)
    EditText       userPwd;
    @BindView(R.id.layout_root)
    KeyboardLayout mRootView;
    @BindView(R.id.login_scroll_view)
    ScrollView     mScrollView;
    @BindView(R.id.cb_auto_login)
    CheckBox       mCbAutoLogin;
    @BindView(R.id.tv_find_pwd)
    TextView       mTvFindPwd;
    private DoubleClickExitHelper doubleClickExitHelper;
    private int     UPDATE_RECYCLERVIEW_POSITION = 11;
    private Handler mHandler                     = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_RECYCLERVIEW_POSITION) {
                mScrollView.scrollTo(0, Utils.dip2px(200));
            }
        }
    };
    private boolean isAutoLogin                  = true;
    private long    lastClickTime                = 0;

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
        userPwd.setOnKeyListener(this);
        mCbAutoLogin.setChecked(AppConfig.getAppConfig(LoginActivity.this).get(AppConfig.PREF_KEY_PASSWORD_FLAG, false));
        AppConfig.getAppConfig(LoginActivity.this).setAutoLogin(true);
        try {
            userEmail.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTvFindPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime < 1000) {
                    lastClickTime = currentTime;
                    return;
                }
                startActivity(new Intent(LoginActivity.this, WriteJobNumberActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCbAutoLogin.isChecked()) {
            userPwd.setText(AppConfig.getAppConfig(LoginActivity.this).get(AppConfig.PREF_KEY_LOGIN_PASSWORD));
        } else {
            userPwd.setText("");
        }
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
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mScrollView.getLayoutParams();

                layoutParams.setMargins(0, 0, 0, heightDifference);

                mScrollView.requestLayout();
                Message msg = new Message();
                msg.arg1 = heightDifference;
                mHandler.sendEmptyMessage(UPDATE_RECYCLERVIEW_POSITION);
            }
        });

        mCbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO isChecked
                if (isChecked) {
                    AppConfig.getAppConfig(LoginActivity.this).set(AppConfig.PREF_KEY_CODE, true);
                    AppConfig.getAppConfig(LoginActivity.this).set(AppConfig.PREF_KEY_PASSWORD_FLAG, true);
                    isAutoLogin = true;
                } else {
                    AppConfig.getAppConfig(LoginActivity.this).set(AppConfig.PREF_KEY_PASSWORD_FLAG, false);
                    isAutoLogin = false;

                }
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        SubmitLogin();
    }

    private void SubmitLogin() {
        if (mCbAutoLogin.isChecked()) {
            AppConfig.getAppConfig(LoginActivity.this).set(AppConfig.PREF_KEY_LOGIN_PASSWORD, userPwd.getText().toString());
        } else {
            AppConfig.getAppConfig(LoginActivity.this).set(AppConfig.PREF_KEY_LOGIN_PASSWORD, "");
        }

        if (check()) {
            //cxp添加，检验通过，隐藏软键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(userEmail.getWindowToken(), 0);
            showLoadingView();
            mPresenter.login(userEmail.getText().toString(), userPwd.getText().toString());
        }
    }

    private boolean check() {
        if (userEmail.getText().toString().equals("")) {
            showToast("请输入您的邮箱帐号或员工号");
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
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void loginSuccess(JSONObject userInfo) {
        //登录成功后清空原来的信息
        AppConfig.getAppConfig(LoginActivity.this).clearLoginInfo();
        //保存登录用户信息
        User user = new User(userInfo);
        LogUtils.e("user->" + user);
        AppConfig.getAppConfig(LoginActivity.this).set(user, isAutoLogin);
        goToLogin();
    }

    @Override
    public void loginFailed(int errorCode) {
        hideLoadingView();
        catchWarningByCode(errorCode);
    }

    @Override
    public void loginOtherError() {
        hideLoadingView();
        showToast("请检查网络！");
    }

    @Override
    public void accountOrPswError(int errorCode, String msg) {
        hideLoadingView();
        if (errorCode == RESPONSES_CODE_ACCOUNT_PASSWORD_ERROR) {
            userPwd.setText("");
        }
        showToast(msg);
    }

    @Override
    public void requestFinish() {
    }


    @Override
    public void uidNull(int code) {
        hideLoadingView();
    }


    private void goToLogin() {
        showToast("登录成功");
        hideLoadingView();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public static void goLoginContextActivity(Context context) {
        //不能进行页面动画
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    //监听 回车登陆
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            SubmitLogin();
            return true;
        }
        return false;
    }
}