package com.itcrm.GroupInformationPlatform;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.itcrm.GroupInformationPlatform.common.Api;
import com.itcrm.GroupInformationPlatform.common.Constants;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.manager.AppManager;
import com.itcrm.GroupInformationPlatform.ui.activity.LoginActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.MainController;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * <h3>Description: 欢迎界面 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/22.<br />
 */
public class WelcomePage extends Activity {

    private KJHttp kjHttp;
    private AlphaAnimation aa;
    private boolean isTimeOut = false;//默认超时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundResource(R.drawable.welcome_page);
        setContentView(view);

        checkTimeOut();
        //渐变展示启动屏
        aa = new AlphaAnimation(0.1f, 1.0f);
        view.startAnimation(aa);
        aa.setFillAfter(true);
        aa.setDuration(3000);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //initThirdParty();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

//                boolean autoLogin = AppConfig.getAppConfig(AppManager.mContext).isAutoLogin();
//                if (autoLogin && !AppConfig.getAppConfig(AppManager.mContext)
//                        .get(AppConfig.PREF_KEY_USERNAME).equals(AppConfig.DEFAULT_ARGUMENTS_VALUE)) {
//                    startActivity(new Intent(AppManager.mContext, MainController.class));
//                } else {
//                    startActivity(new Intent(AppManager.mContext, LoginActivity.class));
//                }
//                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getCompanyId();
    }

    /**
     * 在这初始化第三方的框架，防止开启应用黑屏
     */
    private void initThirdParty() {
        Fresco.initialize(AppManager.mContext);

        //初始化讯飞语音
        SpeechUtility.createUtility(AppManager.mContext, SpeechConstant.APPID + "=5819a625");

        //极光初始化
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(AppManager.mContext);
        builder.statusBarDrawable = R.drawable.login_logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS;
        JPushInterface.setPushNotificationBuilder(1, builder);
        // 设置为铃声与震动都要
        JPushInterface.setDebugMode(true);
        JPushInterface.init(AppManager.mContext);


        //使用EaseUI 相关
        EMOptions options = new EMOptions();
        options.setAutoLogin(true);
        EaseUI.getInstance().init(AppManager.mContext, options);

        //腾讯bugly初始化，第二个参数为AppId,第三个参数为log：建议在测试阶段建议设置成true，发布时设置为false。
        //CrashReportInfo是日志过滤的
        String packageName = AppManager.mContext.getPackageName();
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(AppManager.mContext);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(AppManager.mContext, "defaadd6c6", false, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 登录超时判断
     */
    public void checkTimeOut() {
        HttpParams params = new HttpParams();

        if (kjHttp == null) {
            kjHttp = new KJHttp();
        }
        String uid = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_USER_UID);
        String token = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_TOKEN);
        params.put("uid", uid);
        params.put("token", token);

        kjHttp.post(AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.BASE_URL)+Api.SITE_TIMEOUT, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                finish();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("---------------成功"+t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        JSONObject data = jo.getJSONObject("data");
                        String timeout = data.getString("timeout");
                        Log.e("","---------timeout："+timeout);
                        LogUtils.e("---------timeout"+timeout);
                        if (timeout.equals("1")) {//超时
                            isTimeOut = false;
                        } else {
                            isTimeOut = true;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isTimeOut) {//不超时
                    startActivity(new Intent(AppManager.mContext, MainController.class));
                } else {
                    startActivity(new Intent(AppManager.mContext, LoginActivity.class));
                }
//                finish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e("-------onFailure" + strMsg);
                if (isTimeOut) {//不超时
                    startActivity(new Intent(AppManager.mContext, MainController.class));
                } else {
                    startActivity(new Intent(AppManager.mContext, LoginActivity.class));
                }
//                finish();
            }
        });
    }
    /**
     * 请求公司ID
     */
    private void getCompanyId() {
        HttpParams params = new HttpParams();

        if (kjHttp == null) {
            kjHttp = new KJHttp();
        }
        params.put("cid", Constants.GETBASEURLCID);
        kjHttp.post(Api.ORIGINAL_URL, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                LogUtils.e("onFinish");
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        JSONObject data = Api.getDataToJSONObject(jo);
                        String url = data.getString("domain");
                        AppConfig.getAppConfig(AppManager.mContext).set(AppConfig.BASE_URL, url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e("onFailure" + strMsg);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();


    }


}