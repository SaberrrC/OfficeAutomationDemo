package com.shanlinjinrong.oa.ui.activity.message.contract;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public interface SelectedGroupContactContract {

    interface View extends BaseView {

        void QueryGroupContactSuccess(List<Contacts> bean);

        void searchContactSuccess(List<Contacts> bean);

    }

    interface Presenter extends BasePresenter<SelectedGroupContactContract.View> {

        void QueryGroupContact(String orgId);

        void searchContact(String name);
    }
}