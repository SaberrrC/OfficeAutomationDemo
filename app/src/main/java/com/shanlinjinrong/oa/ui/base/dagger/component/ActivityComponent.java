package com.shanlinjinrong.oa.ui.base.dagger.component;

import com.shanlinjinrong.oa.ui.activity.calendar.MouthCalenderActivity;
import com.shanlinjinrong.oa.ui.activity.contracts.Contact_Details_Activity;
import com.shanlinjinrong.oa.ui.activity.contracts.ContactsActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.ApplyForOfficeSuppliesActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.ApprovalListActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.MeLaunchOfficesSuppliesActivity;
import com.shanlinjinrong.oa.ui.activity.home.approval.WaitApprovalReplyActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.CreateMeetingActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.CreateNoteActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.ScheduleActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.InitiateThingsRequestActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingDetailsActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingInfoFillOutActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingPredetermineRecordActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingReservationRecordActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.AttandenceMonthActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.AttandenceRecorderActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.HolidaySearchActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.MyAttendenceActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.PayQueryActivity;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.WriteWeeklyNewspaperActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.CheckDailyReportActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.MyLaunchWorkReportActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.SelectContactActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportCheckActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportUpdateActivity;
import com.shanlinjinrong.oa.ui.activity.login.ConfirmCompanyEmailActivity;
import com.shanlinjinrong.oa.ui.activity.login.LoginActivity;
import com.shanlinjinrong.oa.ui.activity.login.WriteJobNumberActivity;
import com.shanlinjinrong.oa.ui.activity.main.MainActivity;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatDetailsActivity;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.message.GroupCommonControlActivity;
import com.shanlinjinrong.oa.ui.activity.message.LookMessageRecordActivity;
import com.shanlinjinrong.oa.ui.activity.message.MessageSearchActivity;
import com.shanlinjinrong.oa.ui.activity.message.SelectedGroupContactActivity;
import com.shanlinjinrong.oa.ui.activity.message.CallActivity;
import com.shanlinjinrong.oa.ui.activity.my.FeedbackActivity;
import com.shanlinjinrong.oa.ui.activity.my.ModificationEmailActivity;
import com.shanlinjinrong.oa.ui.activity.my.ModifyPhoneActivity;
import com.shanlinjinrong.oa.ui.activity.my.ModifyPwdActivity;
import com.shanlinjinrong.oa.ui.activity.my.UserInfoActivity;
import com.shanlinjinrong.oa.ui.activity.notice.NoticeListActivity;
import com.shanlinjinrong.oa.ui.activity.notice.NoticesSingleInfoActivity;
import com.shanlinjinrong.oa.ui.activity.push.PushListActivity;
import com.shanlinjinrong.oa.ui.activity.push.SystemNoticesActivity;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.MyUpcomingTasksActivity;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.UpcomingTasksInfoActivity;
import com.shanlinjinrong.oa.ui.base.dagger.annotation.PerActivity;
import com.shanlinjinrong.oa.ui.base.dagger.module.ActivityModule;

import dagger.Component;

/**
 * Created by lvdinghao on 16/08/2017.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);

    void inject(LoginActivity activity);

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

    void inject(CheckDailyReportActivity activity);

    void inject(WorkReportUpdateActivity activity);

    void inject(WriteWeeklyNewspaperActivity activity);

    void inject(WriteJobNumberActivity activity);

    void inject(MeetingDetailsActivity activity);

    void inject(ConfirmCompanyEmailActivity activity);

    void inject(MeetingReservationRecordActivity activity);

    void inject(MeetingInfoFillOutActivity activity);

    void inject(MeetingPredetermineRecordActivity activity);

    void inject(UpcomingTasksInfoActivity upcomingTasksInfoActivity);

    void inject(InitiateThingsRequestActivity activity);

    void inject(MyAttendenceActivity activity);

    void inject(AttandenceMonthActivity activity);

    void inject(MouthCalenderActivity mouthCalenderActivity);

    void inject(MyUpcomingTasksActivity myUpcomingTasksActivity);

    void inject(PayQueryActivity doneTasksActivity);

    void inject(HolidaySearchActivity holidaySearchActivity);

    void inject(ModificationEmailActivity modificationEmailActivity);

    void inject(AttandenceRecorderActivity attandenceRecorderActivity);

    void inject(EaseChatMessageActivity easeChatMessageActivity);

    void inject(LookMessageRecordActivity lookMessageRecordActivity);

    void inject(SelectedGroupContactActivity selectedGroupContactActivity);

    void inject(MessageSearchActivity messageSearchActivity);

    void inject(EaseChatDetailsActivity easeChatDetailsActivity);

    void inject(GroupCommonControlActivity groupCommonControlActivity);

    void inject(Contact_Details_Activity contactDetailsActivity);

    void inject(CallActivity callActivity);
}
