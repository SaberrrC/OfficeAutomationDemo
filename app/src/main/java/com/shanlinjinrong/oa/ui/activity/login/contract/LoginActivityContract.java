package com.shanlinjinrong.oa.ui.activity.login.contract;

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

        void loginFailed(int errorCode); // 登录失败

        void loginOtherError(); // 登录出现的其他错误

        void accountOrPswError(int errorCode, String msg); //账号或密码错误

        void requestFinish(); //登录请求结束

    }

    interface Presenter extends BasePresenter<View> {
        void login(String account, String psw); //登录
    }
}
