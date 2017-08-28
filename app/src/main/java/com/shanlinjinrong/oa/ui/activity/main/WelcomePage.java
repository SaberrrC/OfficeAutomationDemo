package com.shanlinjinrong.oa.ui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.login.LoginActivity;
import com.shanlinjinrong.oa.ui.activity.main.component.DaggerWelcomePageComponent;
import com.shanlinjinrong.oa.ui.activity.main.component.WelcomePageComponent;
import com.shanlinjinrong.oa.ui.activity.main.contract.WelcomePageContract;
import com.shanlinjinrong.oa.ui.activity.main.module.WelcomeModule;

import javax.inject.Inject;

/**
 * <h3>Description: 欢迎界面 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/22.<br />
 */
public class WelcomePage extends Activity implements WelcomePageContract.View {
    @Inject
    public WelcomePageContract.Presenter mPresenter;
    private String uid;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundResource(R.drawable.welcome_page);
        setContentView(view);
        startAnimation(view);
        getComponent().inject(this);
        uid = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_USER_UID);
        token = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_TOKEN);
        checkTimeOutSuccess();

        //TODO 2017年8月25日14:14:46  Welcome界面为什么要登录超时判断
        //  mPresenter.checkoutTimeOut(uid, token);
        //TODO BaseURL 废弃
        //  mPresenter.getDomain();

    }

    private WelcomePageComponent getComponent() {
        return DaggerWelcomePageComponent.builder().welcomeModule(new WelcomeModule(this)).build();
    }

    private void startAnimation(View view) {
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        view.startAnimation(aa);
        aa.setFillAfter(true);
        aa.setDuration(3000);
    }


    @Override
    public void checkTimeOutSuccess() {
        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(token)) {//不超时
            startActivity(new Intent(AppManager.mContext, MainController.class));
        } else {
            Log.e("ding", "checkTimeOutSuccess" + "login");
            startActivity(new Intent(AppManager.mContext, LoginActivity.class));
        }
        finish();
    }

    @Override
    public void checkTimeOutFailed() {
        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(token)) { //token不为空，表明仍然是登录状态            startActivity(new Intent(AppManager.mContext, MainController.class));
        } else {
            Log.e("ding", "checkTimeOutFailed" + "token" + ":null");
            startActivity(new Intent(AppManager.mContext, LoginActivity.class));
        }
    }

    @Override
    public void loadFinish() {
        finish();
    }
}