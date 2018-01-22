package com.shanlinjinrong.oa.ui.activity.message.presenter;


import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.message.contract.MessageSearchContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class MessageSearchPresenter extends HttpPresenter<MessageSearchContract.View> implements MessageSearchContract.Presenter {

    @Inject
    public MessageSearchPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }
}