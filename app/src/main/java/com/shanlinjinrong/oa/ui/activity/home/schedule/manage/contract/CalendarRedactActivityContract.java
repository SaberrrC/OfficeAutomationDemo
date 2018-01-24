package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/24
 * 功能描述：
 */

public interface CalendarRedactActivityContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void addCalendarScheduleSuccess();

        void addCalendarScheduleFailure(int errorCode, String errorMsg);

        void deleteCalendarScheduleSuccess();

        void deleteCalendarScheduleFailure(int errorCode, String errorMsg);

        void updateCalendarScheduleSuccess();

        void updateCalendarScheduleFailure(int errorCode, String errorMsg);


    }

    interface Presenter extends BasePresenter<CalendarRedactActivityContract.View> {

        void addCalendarSchedule(String schedule);

        void deleteCalendarSchedule(int calendarId);

        void updateCalendarSchedule(String schedule);
    }
}
