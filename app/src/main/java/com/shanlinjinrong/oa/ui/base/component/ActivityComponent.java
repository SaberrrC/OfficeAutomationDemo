package com.shanlinjinrong.oa.ui.base.component;

import com.shanlinjinrong.oa.ui.activity.contracts.ContactsActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.ScheduleActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.SelectContactActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity;
import com.shanlinjinrong.oa.ui.activity.login.FindPassWordActivity;
import com.shanlinjinrong.oa.ui.activity.login.LoginActivity;
import com.shanlinjinrong.oa.ui.activity.main.MainController;
import com.shanlinjinrong.oa.ui.activity.my.FeedbackActivity;
import com.shanlinjinrong.oa.ui.activity.my.ModifyPhoneActivity;
import com.shanlinjinrong.oa.ui.activity.my.ModifyPwdActivity;
import com.shanlinjinrong.oa.ui.activity.my.UserInfoActivity;
import com.shanlinjinrong.oa.ui.activity.notice.NoticeListActivity;
import com.shanlinjinrong.oa.ui.activity.notice.NoticesSingleInfoActivity;
import com.shanlinjinrong.oa.ui.activity.push.PushListActivity;
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

    void inject(ScheduleActivity activity);

    void inject(FeedbackActivity activity);

    void inject(ModifyPhoneActivity activity);

    void inject(ModifyPwdActivity activity);

    void inject(UserInfoActivity activity);

    void inject(NoticeListActivity activity);

    void inject(NoticesSingleInfoActivity activity);

    void inject(PushListActivity activity);
}
