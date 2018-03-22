package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.CalendarScheduleContentBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/20
 * 功能描述：
 */

public interface MonthlyCalendarFragmentContract {

    interface View extends BaseView{

        void showLoading();

        void hideLoading();

        void GetScheduleByDateSuccess(CalendarScheduleContentBean bean);

        void GetScheduleByDateFailure(int errorCode, String errorMsg);
    }

    interface Presenter extends BasePresenter<MonthlyCalendarFragmentContract.View>{
        void   getScheduleByDate(String startDate, String endDate);
    }
}
