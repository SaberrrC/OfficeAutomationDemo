package com.shanlinjinrong.oa.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.shanlinjinrong.oa.manager.AppManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    private static Handler  sHandler        = new Handler(Looper.getMainLooper());//获取主线程的Looper
    private static Executor sExecutorCached = Executors.newCachedThreadPool();
    private static Executor sExecutor       = new ThreadPoolExecutor(3, 15, 5, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Toast.makeText(AppManager.mContext, "操作太频繁，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    });

    private static Executor sExecutorMore = new ThreadPoolExecutor(5, 10, 5, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Toast.makeText(AppManager.mContext, "操作太频繁，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    });

    public static void runSub(Runnable runnable) {
        sExecutorCached.execute(runnable);
    }

    public static void runBigSub(Runnable runnable) {
        sExecutorCached.execute(runnable);
    }

    public static void runMain(Runnable runnable) {
        sHandler.post(runnable);
    }

    public static void runMainDelayed(Runnable runnable, long delay) {
        sHandler.postDelayed(runnable, delay);
    }
}
