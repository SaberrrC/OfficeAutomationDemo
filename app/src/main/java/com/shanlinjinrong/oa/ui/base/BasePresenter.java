package com.shanlinjinrong.oa.ui.base;

public interface BasePresenter<T extends BaseView> {

    void subscribe(T view);

    void unSubscribe();
}
