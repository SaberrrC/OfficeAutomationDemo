package com.example.retrofit.subscribers;

/**
 * Created by zhaojian on 2017/8/14.
 */
public interface SubscriberOnAllListener<T> {
    void onNext(T t);

    void onError(Throwable t);

    void onStart();

    void onCompleted();
}
