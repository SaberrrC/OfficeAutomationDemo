package com.shanlinjinrong.oa.ui.activity.login.presenter;

import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.login.contract.WriteJobNumberContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/10/09.
 * 获取验证码 presenter
 */
public class WriteJobNumberPresenter extends HttpPresenter<WriteJobNumberContract.View> implements WriteJobNumberContract.Presenter {

    @Inject
    public WriteJobNumberPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void getIdentifyingCode() {

    }
}
