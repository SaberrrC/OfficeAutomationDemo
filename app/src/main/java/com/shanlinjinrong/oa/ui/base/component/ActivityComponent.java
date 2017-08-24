package com.shanlinjinrong.oa.ui.base.component;

import com.shanlinjinrong.oa.ui.activity.contracts.ContactsActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.SelectContactActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity;
import com.shanlinjinrong.oa.ui.activity.login.FindPassWordActivity;
import com.shanlinjinrong.oa.ui.activity.login.LoginActivity;
import com.shanlinjinrong.oa.ui.activity.main.MainController;
import com.shanlinjinrong.oa.ui.annotation.PerActivity;
import com.shanlinjinrong.oa.ui.base.module.ActivityModule;

import dagger.Component;

/**
 * Created by lvdinghao on 16/08/2017.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainController activity);

    void inject(LoginActivity activity);

    void inject(FindPassWordActivity activity);

    void inject(ContactsActivity activity);

    void inject(WorkReportLaunchActivity activity);

    void inject(SelectContactActivity activity);
}
