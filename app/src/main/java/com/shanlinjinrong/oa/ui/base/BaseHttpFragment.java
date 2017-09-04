package com.shanlinjinrong.oa.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.base.dagger.component.DaggerFragmentComponent;
import com.shanlinjinrong.oa.ui.base.dagger.component.FragmentComponent;
import com.shanlinjinrong.oa.ui.base.dagger.module.FragmentModule;

import javax.inject.Inject;

/**
 * 基础的，附带网络请求的Fragment
 * Created by lvdinghao on 2016/9/04
 */
public abstract class BaseHttpFragment<T extends BasePresenter> extends BaseFragment implements BaseView {

    @Inject
    protected T mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInject();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }


    protected abstract void initInject();


    protected FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .appComponent(AppManager.sharedInstance().getAppComponent())
                .fragmentModule(getFragmentModule()).build();
    }

    protected FragmentModule getFragmentModule() {
        return new FragmentModule(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }
}