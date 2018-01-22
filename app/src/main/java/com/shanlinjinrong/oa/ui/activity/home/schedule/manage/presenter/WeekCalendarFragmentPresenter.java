package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter;

import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contact.WeekCalendarFragmentContact;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import javax.inject.Inject;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/20
 * 功能描述：
 */

public class WeekCalendarFragmentPresenter extends HttpPresenter<WeekCalendarFragmentContact.View> implements WeekCalendarFragmentContact.Presenter {

    @Inject
    public WeekCalendarFragmentPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }
}
