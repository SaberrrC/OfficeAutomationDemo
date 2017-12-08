package com.shanlinjinrong.oa.ui.activity.message.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public interface EaseChatDetailsContact {

    interface View extends BaseView{
//       void searchUserListInfoSuccess();

    }

    interface Presenter extends BasePresenter<EaseChatDetailsContact.View>{

        void searchUserListInfo(String codeList);
    }
}
