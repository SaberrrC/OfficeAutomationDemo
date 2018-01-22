package com.shanlinjinrong.oa.ui.activity.message.contract;

import com.shanlinjinrong.oa.ui.activity.main.bean.UserDetailsBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * 作者：王凤旭
 * 时间：2017/11/30
 * 描述：
 */

public interface CallActivityContract {
    interface View extends BaseView {
        void searchUserDetailsSuccess(UserDetailsBean.DataBean userDetailsBean);

        void searchUserDetailsFailed();
    }

    interface Presenter extends BasePresenter<CallActivityContract.View> {
        void searchUserDetails(String code);
    }
}
