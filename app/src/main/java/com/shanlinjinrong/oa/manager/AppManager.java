package com.shanlinjinrong.oa.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.pgyersdk.crash.PgyCrashManager;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.thirdParty.huanxin.DemoHelper;
import com.shanlinjinrong.oa.ui.activity.home.schedule.SelectJoinPeopleActivity;
import com.shanlinjinrong.oa.ui.activity.main.WelcomePage;
import com.shanlinjinrong.oa.ui.base.dagger.component.AppComponent;
import com.shanlinjinrong.oa.ui.base.dagger.component.DaggerAppComponent;
import com.shanlinjinrong.oa.ui.base.dagger.module.AppManagerModule;
import com.shanlinjinrong.oa.ui.base.dagger.module.KjHttpModule;
import com.shanlinjinrong.oa.utils.ScreenUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 概述：
 * 1、应用程序Activity管理：用于Activity管理和应用程序退出
 * <p/>
 * Created by KevinMeng on 2016/5/6.
 */

public class AppManager extends MultiDexApplication {
    // 共享变量
    private SelectJoinPeopleActivity.MyJoinHandler joinhandler = null;

    // set方法
    public void setJoinhandler(SelectJoinPeopleActivity.MyJoinHandler joinhandler) {
        this.joinhandler = joinhandler;
    }

    // get方法
    public SelectJoinPeopleActivity.MyJoinHandler getJoinhandler() {
        return joinhandler;
    }

    private static Stack<Activity> activityStack;
    private static AppManager instance;
    public static Context mContext;

    private AppComponent appComponent;

    //内存检测start
    public static RefWatcher getRefWatcher(Context context) {
        AppManager application = (AppManager) context
                .getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;
//    内存检测end

    public AppManager() {


    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.mContext = this;

        //注册Crash接口（必选）
        PgyCrashManager.register(this);
        //查看数据库插件
        Stetho.initializeWithDefaults(this);

        //初始化网易云信SDK
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        //如果还没有登录用户，第二个参数传入空
        NIMClient.init(this, null, options());

        Fresco.initialize(AppManager.mContext);

        //初始化讯飞语音
        SpeechUtility.createUtility(AppManager.mContext, SpeechConstant.APPID + "=59ae7651");

        //极光初始化+
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(
                AppManager.mContext);
        builder.statusBarDrawable = R.drawable.login_logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE |
                Notification.DEFAULT_LIGHTS;
        JPushInterface.setPushNotificationBuilder(1, builder);
        // 设置为铃声与震动都要
        JPushInterface.setDebugMode(true);
        JPushInterface.init(AppManager.mContext);


        //环信初始化,init demo helper
        MultiDex.install(this);
        DemoHelper.getInstance().init(mContext);


        //腾讯bugly初始化，第二个参数为AppId,第三个参数为log：建议在测试阶段建议设置成true，发布时设置为false。
        //CrashReportInfo是日志过滤的
        String packageName = AppManager.mContext.getPackageName();
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(AppManager.mContext);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(AppManager.mContext, "071245370e", false, strategy);

        //解决7.0拍照文件uri闪退问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }

        //leakCanary
//        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
//        }

        //blockCanary
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();

        initAppComponent();
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = WelcomePage.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.oa_logo;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = ScreenUtils.getScreenWidth(this) / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.mipmap.oa_logo;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
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
     * 单一实例
     */
    public static AppManager sharedInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appManagerModule(new AppManagerModule(this))
                .kjHttpModule(new KjHttpModule())
                .build();
    }

    /**
     * @return 获取应用名称
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                Activity activity = activityStack.get(i);
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            //杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
        } catch (Exception e) {
        }
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}