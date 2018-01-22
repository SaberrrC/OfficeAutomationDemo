package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contact;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/20
 * 功能描述：
 */

public interface WeekCalendarFragmentContact {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

    }

    interface Presenter extends BasePresenter<WeekCalendarFragmentContact.View> {

    }
}
