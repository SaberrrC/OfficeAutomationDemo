package com.shanlinjinrong.oa.ui.activity.main.contract;

import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.activity.main.bean.AppVersionBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

public interface TabMeGetVersionInfo {
    interface View extends BaseView {
        void getAppEditionSuccess(AppVersionBean mAppVersionBean);

        void getAppEdittionFailed(int errorCode,String errorMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void getAppEdition();
    }

}
