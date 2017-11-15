package com.shanlinjinrong.oa.ui.activity.calendar.presenter;

import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.calendar.contract.MouthCalenderContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class WeekCalenderPresenter extends HttpPresenter<MouthCalenderContract.View> implements MouthCalenderContract.Presenter {

    @Inject
    public WeekCalenderPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

}
