package com.shanlinjinrong.oa.ui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.shanlinjinrong.oa.BuildConfig;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.login.LoginActivity;

/**
 * <h3>Description: 欢迎界面 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/22.<br />
 */
public class WelcomePage extends Activity {

    private String uid;
    private String token;
    private HuaweiApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundResource(R.drawable.welcome);
        setContentView(view);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        startAnimation(view);
        uid = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID);
        token = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN);
        //创建华为移动服务client实例用以使用华为push服务
        //需要指定api为HuaweiPush.PUSH_API
        //连接回调以及连接失败监听
        client = new HuaweiApiClient.Builder(this)
                .addApi(HuaweiPush.PUSH_API)
                .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected() {
                        Log.d("TAG", "HuaweiApiClient 连接成功");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .build();

        //建议在oncreate的时候连接华为移动服务
        //业务可以根据自己业务的形态来确定client的连接和断开的时机，但是确保connect和disconnect必须成对出现
        client.connect();
    }


    private void startAnimation(View view) {
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        view.startAnimation(aa);
        aa.setFillAfter(true);
        if (BuildConfig.DEBUG) {
            aa.setDuration(0);
        } else {
            aa.setDuration(3000);
        }
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void startNext() {
        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(token)) {//登陆过
            startActivity(new Intent(AppManager.mContext, MainActivity.class));
        } else {
            //未登录
            startActivity(new Intent(AppManager.mContext, LoginActivity.class));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //建议在onDestroy的时候停止连接华为移动服务
        //业务可以根据自己业务的形态来确定client的连接和断开的时机，但是确保connect和disconnect必须成对出现
        client.disconnect();
    }
}