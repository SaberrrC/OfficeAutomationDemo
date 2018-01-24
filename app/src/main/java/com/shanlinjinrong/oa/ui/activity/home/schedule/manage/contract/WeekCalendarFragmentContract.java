package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.CalendarScheduleContentBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/20
 * 功能描述：
 */

public interface WeekCalendarFragmentContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void QueryCalendarScheduleSuccess(CalendarScheduleContentBean bean);

        void QueryCalendarScheduleFailure(int errorCode, String errorMsg);


    }

    interface Presenter extends BasePresenter<WeekCalendarFragmentContract.View> {

        void QueryCalendarSchedule(String startDate, String endDate);
    }
}
