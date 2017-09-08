package com.shanlinjinrong.oa.helper;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppManager;


/**
 * 概述：双击返回键退出 <br />
 * Created by KevinMeng on 2016/5/6.
 */
public class DoubleClickExitHelper {

    private final Activity activity;

    private boolean isOnKeyBacking;
    private Handler handler;
    private Toast backToast;

    public DoubleClickExitHelper(Activity activity) {
        this.activity = activity;
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * Activity onBackPressed事件
     * */
    public void onBackPressed() {
        if(isOnKeyBacking) {
            handler.removeCallbacks(onBackTimeRunnable);
            if(backToast != null){
                backToast.cancel();
            }
            // 退出
            AppManager.sharedInstance().AppExit();
        } else {
            isOnKeyBacking = true;
            if(backToast == null) {
                backToast = Toast.makeText(activity,
                        activity.getResources().getString(R.string.double_click_exit_tips) +
                                activity.getResources().getString(R.string.app_name),Toast.LENGTH_LONG);
                backToast.setGravity(Gravity.CENTER, 0, 0);
            }
            backToast.show();
            handler.postDelayed(onBackTimeRunnable, 2000);
        }
    }

    private Runnable onBackTimeRunnable = new Runnable() {

        @Override
        public void run() {
            isOnKeyBacking = false;
            if(backToast != null){
                backToast.cancel();
            }
        }
    };
}