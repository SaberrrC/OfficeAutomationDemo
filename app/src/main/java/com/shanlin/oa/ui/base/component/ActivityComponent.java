package com.shanlin.oa.ui.base.component;

import com.shanlin.oa.ui.activity.login.LoginActivity;
import com.shanlin.oa.ui.activity.main.MainController;
import com.shanlin.oa.ui.annotation.PerActivity;
import com.shanlin.oa.ui.base.module.ActivityModule;

import dagger.Component;

/**
 * Created by lvdinghao on 16/08/2017.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainController activity);
    void inject(LoginActivity activity);
}
