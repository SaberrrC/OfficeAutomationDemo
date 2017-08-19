package com.shanlin.oa.ui.activity.main.contract;

/**
 * Created by 丁 on 2017/8/18.
 */
public interface WelcomePageContract {
    interface View {
        void checkTimeOutSuccess(boolean isTimeOut,String uid);

        void checkTimeOutFailed(String token);

        void loadFinish();
    }

    interface Presenter {
        void checkoutTimeOut(); // 登录超时判断

        void getDomain(); //获取域名
    }
}
