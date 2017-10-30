package com.example.retrofit.subscribers;

/**
 * Created by zhaojian on 2017/8/14.
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
}
