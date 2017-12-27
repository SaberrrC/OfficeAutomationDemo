package com.shanlinjinrong.oa.ui.activity.my.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

public interface ModificationEmailContract {

    interface View extends BaseView {

        void modificationEmailSuccess(); // 成功

        void modificationEmailFailed(int errorNo, String strMsg); //失败
    }

    interface Presenter extends BasePresenter<ModificationEmailContract.View> {
        void modificationEmail(String email);
    }
}
