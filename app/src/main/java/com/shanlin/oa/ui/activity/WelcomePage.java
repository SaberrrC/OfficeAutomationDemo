package com.shanlin.oa.ui.activity;

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
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.thirdParty.huanxin.DemoHelper;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;
import com.shanlin.oa.ui.activity.login.LoginActivity;
import com.shanlin.oa.utils.LogUtils;
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
        getDomain();
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


        //环信和使用EaseUI 相关
        DemoHelper.getInstance().initHandler(this.getMainLooper());
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
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
        final String uid = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_USER_UID);
        final String token = AppConfig.getAppConfig(AppManager.mContext)
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
                        isTimeOut = !timeout.equals("1");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isTimeOut && !TextUtils.isEmpty(uid)) {//不超时
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
                if (!TextUtils.isEmpty(token)) { //token不为空，表明仍然是登录状态
                    startActivity(new Intent(AppManager.mContext, MainController.class));
                } else {
                    startActivity(new Intent(AppManager.mContext, LoginActivity.class));
                }
//                finish();
            }
        });
    }
    /**
     * 请求接口的域名
     */
    private void getDomain() {
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
                // TODO: 2017/8/15 域名请求失败，不需要给一个默认的域名吗？
                AppConfig.getAppConfig(AppManager.mContext).set(AppConfig.BASE_URL, "http://api.sl.s1.zhitongoa.com/");
                LogUtils.e("onFailure" + strMsg);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();


    }


}