package com.shanlinjinrong.oa.ui.activity.message.contract;

import com.shanlinjinrong.oa.ui.activity.main.bean.UserDetailsBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by SaberrrC on 2017-11-22.
 */

public interface EaseChatMessageContract {
    interface View extends BaseView {

        void searchUserDetailsSuccess(UserDetailsBean.DataBean userDetailsBean);

        void searchUserDetailsFailed();
    }

    interface Presenter extends BasePresenter<View> {
        void searchUserDetails(String code);
    }
}
