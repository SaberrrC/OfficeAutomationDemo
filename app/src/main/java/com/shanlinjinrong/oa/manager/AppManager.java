package com.shanlinjinrong.oa.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.hyphenate.easeui.crash.Cockroach;
import com.shanlinjinrong.oa.BuildConfig;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.thirdParty.huanxin.DemoHelper;
import com.shanlinjinrong.oa.ui.activity.home.schedule.SelectJoinPeopleActivity;
import com.shanlinjinrong.oa.ui.base.dagger.component.AppComponent;
import com.shanlinjinrong.oa.ui.base.dagger.component.DaggerAppComponent;
import com.shanlinjinrong.oa.ui.base.dagger.module.AppManagerModule;
import com.shanlinjinrong.oa.ui.base.dagger.module.KjHttpModule;
import com.shanlinjinrong.oa.utils.ToastManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import ren.yale.android.cachewebviewlib.CacheWebView;

/**
 * 概述：
 * 1、应用程序Activity管理：用于Activity管理和应用程序退出
 * <p/>
 * Created by KevinMeng on 2016/5/6.
 */

public class AppManager extends MultiDexApplication {
    // 共享变量
    private SelectJoinPeopleActivity.MyJoinHandler joinhandler = null;

    private WebView mWebView;

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
    public static ToastManager sToastManager;

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
        if (sToastManager == null) {
            sToastManager = new ToastManager(new WeakReference<Application>(this));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.mContext = this;

        //注册Crash接口（必选）
        // PgyCrashManager.register(this);
        //查看数据库插件
        Stetho.initializeWithDefaults(this);


        Fresco.initialize(AppManager.mContext);

        //初始化讯飞语音
        // SpeechUtility.createUtility(AppManager.mContext, SpeechConstant.APPID + "=59ae7651");

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
        if (BuildConfig.DEBUG) {
            CrashReport.initCrashReport(AppManager.mContext, "d93608e360", true, strategy);
        } else {
            CrashReport.initCrashReport(AppManager.mContext, "72d31f8f77", false, strategy);
            initCrash();
        }

        //解决7.0拍照文件uri闪退问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }

        //  LeakCanary.install(this);

        //性能魔方
        //Mmtrix.withApplicationToken("b6e5828c2a8defbee75d5b0a9473d115").withCrashReportingEnabled(true).start(this);
        //}

        //blockCanary
        //install(this, new AppBlockCanaryContext()).start();

        initAppComponent();
        File cacheFile = new File(this.getCacheDir(), "cache_path_name");
        CacheWebView.getCacheConfig().init(this, cacheFile.getAbsolutePath(), 1024 * 1024 * 20, 1024 * 1024 * 5)
                .enableDebug(true);//20M 磁盘缓存空间,5M 内存缓存空间
    }

    private void initCrash() {

        Cockroach.install(new Cockroach.ExceptionHandler() {

            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log
                            Log.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
                            //                        throw new RuntimeException("..."+(i++));
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });
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