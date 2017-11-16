package com.shanlinjinrong.oa.utils;

import android.app.Application;
import android.os.Looper;
import android.widget.Toast;


import java.lang.ref.WeakReference;

public class ToastManager {

    private Toast mToast;
    private String message = null;
    private final WeakReference<Application> mApplicationWeakReference;

    public ToastManager(WeakReference<Application> applicationWeakReference) {
        mApplicationWeakReference = applicationWeakReference;
    }

    public Toast displayLongToast(final String str) {
        message = str;
        if (mApplicationWeakReference.get() != null) {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(mApplicationWeakReference.get(), str, Toast.LENGTH_LONG);
                    mToast.show();
                    Looper.loop();
                }
            }.start();
            return mToast;
        } else {
            return null;
        }
    }

    public Toast displayShortToast(final String str) {
        message = str;
        if (mApplicationWeakReference.get() != null) {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(mApplicationWeakReference.get(), str, Toast.LENGTH_SHORT);
                    mToast.show();
                    Looper.loop();
                }
            }.start();
            return mToast;
        } else {
            return null;
        }
    }

    public Toast displayDurationtToast(final String str, int duration) {
        message = str;
        if (mApplicationWeakReference.get() != null) {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(mApplicationWeakReference.get(), str, duration);
                    mToast.show();
                    Looper.loop();
                }
            }.start();
            return mToast;
        } else {
            return null;
        }
    }

    public String getMessage() {
        return message;
    }
}


