package com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter;

import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksInfoContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class UpcomingTasksInfoPresenter extends HttpPresenter<UpcomingTasksInfoContract.View> implements UpcomingTasksInfoContract.Presenter {

    @Inject
    public UpcomingTasksInfoPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

}
