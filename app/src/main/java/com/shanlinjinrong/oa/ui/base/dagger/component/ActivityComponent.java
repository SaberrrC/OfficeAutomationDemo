package com.shanlinjinrong.oa.ui.base.dagger.component;

import com.shanlinjinrong.oa.ui.activity.contracts.ContactsActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.ApplyForOfficeSuppliesActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.ApprovalListActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.MeLaunchOfficesSuppliesActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.WaitApprovalReplyActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.CreateMeetingActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.CreateNoteActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.ScheduleActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.MyLaunchWorkReportActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.SelectContactActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportCheckActivity;
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
import com.shanlinjinrong.oa.ui.activity.push.SystemNoticesActivity;
import com.shanlinjinrong.oa.ui.base.dagger.annotation.PerActivity;
import com.shanlinjinrong.oa.ui.base.dagger.module.ActivityModule;

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

    void inject(SystemNoticesActivity activity);

    void inject(ApplyForOfficeSuppliesActivity activity);

    void inject(ApprovalListActivity activity);

    void inject(MeLaunchOfficesSuppliesActivity activity);

    void inject(WaitApprovalReplyActivity activity);

    void inject(CreateMeetingActivity activity);

    void inject(CreateNoteActivity activity);

    void inject(WorkReportCheckActivity activity);

    void inject(MyLaunchWorkReportActivity activity);

}
