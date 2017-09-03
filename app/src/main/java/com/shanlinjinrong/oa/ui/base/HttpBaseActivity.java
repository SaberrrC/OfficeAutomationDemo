package com.shanlinjinrong.oa.ui.base;

import android.os.Bundle;

import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.base.dagger.component.ActivityComponent;
import com.shanlinjinrong.oa.ui.base.dagger.component.DaggerActivityComponent;
import com.shanlinjinrong.oa.ui.base.dagger.module.ActivityModule;

import javax.inject.Inject;

/**
 * <h3>Description: 基础Activity</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public abstract class HttpBaseActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    @Inject
    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initInject();

        if (mPresenter != null) {
            mPresenter.attachView(this);
        }

    }


    protected abstract void initInject();


    protected ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(AppManager.sharedInstance().getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.sharedInstance().removeActivity(this);
        if (mPresenter != null)
            mPresenter.detachView();
    }
}