package com.shanlinjinrong.oa.ui.base;

import com.shanlinjinrong.oa.net.MyKjHttp;

import java.lang.ref.WeakReference;

public class HttpPresenter<T extends BaseView> implements BasePresenter<T> {

    protected T mView;

    protected MyKjHttp mKjHttp;

    public HttpPresenter(MyKjHttp mKjHttp) {
        this.mKjHttp = mKjHttp;
    }

    @Override
    public void subscribe(T view) {
        WeakReference<T> tSoftReference = new WeakReference<>(view);
        this.mView = tSoftReference.get();
    }

    @Override
    public void unSubscribe() {
        this.mView = null;
    }
}
