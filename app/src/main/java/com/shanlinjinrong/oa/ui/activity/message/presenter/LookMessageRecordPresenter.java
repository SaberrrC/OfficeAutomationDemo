package com.shanlinjinrong.oa.ui.activity.message.presenter;


import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.message.contract.LookMessageRecordContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class LookMessageRecordPresenter extends HttpPresenter<LookMessageRecordContract.View> implements LookMessageRecordContract.Presenter {

    @Inject
    public LookMessageRecordPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }
}