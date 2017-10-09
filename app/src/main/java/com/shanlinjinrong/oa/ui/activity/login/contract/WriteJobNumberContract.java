package com.shanlinjinrong.oa.ui.activity.login.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 登录相关接口
 */
public interface WriteJobNumberContract {

    interface View extends BaseView {

        void getIdentifyingCodeSuccess(String picUrl); // 成功

        void getIdentifyingCodeFailed(int errorCode); // 失败
    }

    interface Presenter extends BasePresenter<View> {
        void getIdentifyingCode(); //获取验证码

    }
}
