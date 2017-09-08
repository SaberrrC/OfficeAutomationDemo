package com.shanlinjinrong.oa.ui.activity.main.component;

import com.shanlinjinrong.oa.ui.activity.main.WelcomePage;
import com.shanlinjinrong.oa.ui.activity.main.module.WelcomeModule;
import com.shanlinjinrong.oa.ui.base.dagger.annotation.PerActivity;

import dagger.Component;

/**
 * Created by 丁 on 2017/8/18.
 * WelcomePageComponent
 */
@PerActivity
@Component(modules = WelcomeModule.class)
public interface WelcomePageComponent {
    void inject(WelcomePage welcomePage);
}
