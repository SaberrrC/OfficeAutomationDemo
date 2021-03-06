package com.shanlinjinrong.oa.ui.activity.login.contract;

import com.shanlinjinrong.oa.model.UserInfo;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.json.JSONObject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录相关接口
 */
public interface LoginActivityContract {
    interface View extends BaseView {
        void loginSuccess(JSONObject userInfo); // 登录成功

        void loginSuccess(UserInfo.DataBean user); // 登录成功

        void loginFailed(String errorCode); // 登录失败

        void loginOtherError(); // 登录出现的其他错误

        void accountOrPswError(String errorCode, String msg); //账号或密码错误

        void accountOrPswError(String msg); //账号或密码错误

        void requestFinish(); //登录请求结束

        void getIdentifyingCodeSuccess(String picUrl, String keyCode);

        void getIdentifyingCodeFailed(int error);

        void showVerifyCode(String msg);

        void refreshVerifyCode(String msg);

    }

    interface Presenter extends BasePresenter<View> {
        void login(String account, String psw); //登录

        void login(String account, String psw, String keyCode, String code);

        void QueryVerifyCode();
    }
}
