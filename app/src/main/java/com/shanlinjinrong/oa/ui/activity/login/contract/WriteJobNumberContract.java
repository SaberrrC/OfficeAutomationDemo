package com.shanlinjinrong.oa.ui.activity.login.contract;

import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 登录相关接口
 */
public interface WriteJobNumberContract {

    interface View extends BaseView {

        void getIdentifyingCodeSuccess(String picUrl,String keyCode); // 成功

        void getIdentifyingCodeFailed(int errorCode); // 失败


        void searchUserSuccess(User user); // 成功

        void searchUserFailed(int errorCode, String errMsg); // 失败

        void requestFinish();
    }

    interface Presenter extends BasePresenter<View> {
        void getIdentifyingCode(); //获取验证码

        void searchUser(String imgCode, String keyCode,String userCode);//通过工号查询用户信息
    }
}
