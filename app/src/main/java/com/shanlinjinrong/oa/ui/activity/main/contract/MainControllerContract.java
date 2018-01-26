package com.shanlinjinrong.oa.ui.activity.main.contract;

import android.app.Activity;

import com.shanlinjinrong.oa.ui.activity.main.bean.AppVersionBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;


/**
 * Created by 丁 on 2017/8/19.
 * 首页接口
 */
public interface MainControllerContract {
    interface View extends BaseView {
        void loadUnReadMsgOk(String num); //加载未读消息成功

        void loadUnReadMsgEmpty(); //加载未读消息条数为0

        void startAppSetting();

        void getAppEditionSuccess(AppVersionBean mAppVersionBean);
    }

    interface Presenter extends BasePresenter<View> {

        void applyPermission(Activity activity); // 申请权限

    }
}
