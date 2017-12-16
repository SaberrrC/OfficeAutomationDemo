package com.shanlinjinrong.oa.ui.activity.login.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 登录相关接口
 */
public interface ConfirmEmailContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void sendEmailSuccess(); // 成功

        void sendEmailFailed(int errorCode, String errMsg); // 失败
    }

    interface Presenter extends BasePresenter<View> {
        void sendEmail(String code, String emailAddress); //发送邮件
    }
}
