package com.shanlinjinrong.oa.ui.activity.main.contract;

/**
 * Created by 丁 on 2017/8/18.
 */
public interface WelcomePageContract {
    interface View {
        void checkTimeOutSuccess();

        void checkTimeOutFailed();

        void loadFinish();
    }

    interface Presenter {
        void checkoutTimeOut(String uid, String token); // 登录超时判断

        void getDomain(); //获取域名
    }
}
