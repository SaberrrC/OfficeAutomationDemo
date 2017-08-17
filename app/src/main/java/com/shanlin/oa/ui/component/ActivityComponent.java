package com.shanlin.oa.ui.component;

import android.app.Activity;

import com.shanlin.oa.ui.activity.WelcomePage;
import com.shanlin.oa.ui.annotation.PerActivity;
import com.shanlin.oa.ui.module.ActivityModule;

import dagger.Component;

/**
 * Created by lvdinghao on 16/08/2017.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();

    void inject(WelcomePage activity);
}
