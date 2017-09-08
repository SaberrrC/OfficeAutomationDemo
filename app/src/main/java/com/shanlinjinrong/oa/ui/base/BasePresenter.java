package com.shanlinjinrong.oa.ui.base;

/**
 * Created by 丁 on 2017/8/18.
 */

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();
}
