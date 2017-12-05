package com.shanlinjinrong.oa.ui.activity.contracts.contract;

import com.shanlinjinrong.oa.ui.activity.main.bean.UserDetailsBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * 作者：王凤旭
 * 时间：2017/11/30
 * 描述：
 */

public interface ContactDetailsContract {
    interface View extends BaseView {

        void searchUserDetailsSuccess(UserDetailsBean.DataBean userDetailsBean);

    }

    interface Presenter extends BasePresenter<ContactDetailsContract.View> {

        void searchUserDetails(String code);

    }
}
