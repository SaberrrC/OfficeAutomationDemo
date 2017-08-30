package com.shanlinjinrong.oa.ui.base;

import com.shanlinjinrong.oa.net.MyKjHttp;

import java.lang.ref.WeakReference;

/**
 * Created by 丁 on 2017/8/18.
 */
public class HttpPresenter<T extends BaseView> implements BasePresenter<T> {

    protected T mView;

    protected MyKjHttp mKjHttp;

    public HttpPresenter(MyKjHttp mKjHttp) {
        this.mKjHttp = mKjHttp;
    }

    @Override
    public void attachView(T view) {
        WeakReference<T> tSoftReference = new WeakReference<>(view);
        this.mView = tSoftReference.get();
    }

    @Override
    public void detachView() {
        this.mView = null;
        mKjHttp.cancelAll();
        mKjHttp.cleanCache();
    }
}
