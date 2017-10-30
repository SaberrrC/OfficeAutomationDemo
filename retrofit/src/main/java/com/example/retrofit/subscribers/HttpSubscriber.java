package com.example.retrofit.subscribers;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * Created by zhaojian on 2017/8/14.
 */
public class HttpSubscriber<T> extends Subscriber<T> {

    private SubscriberOnAllListener mSubscriberOnAllListener;

    private Context context;

    public HttpSubscriber(SubscriberOnAllListener mSubscriberOnAllListener, Context context) {
        this.mSubscriberOnAllListener = mSubscriberOnAllListener;
        this.context = context;
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (mSubscriberOnAllListener != null) {
            mSubscriberOnAllListener.onStart();
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        if (mSubscriberOnAllListener != null) {
            mSubscriberOnAllListener.onCompleted();
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
//            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
//            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof NullPointerException) {
//            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
//            CrashReport.postCatchedException(e);
            if (!TextUtils.isEmpty(e.getMessage()) && e.getMessage().length() < 25) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (mSubscriberOnAllListener != null) {
            mSubscriberOnAllListener.onError(e);
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnAllListener != null) {
            mSubscriberOnAllListener.onNext(t);
        }
    }
}