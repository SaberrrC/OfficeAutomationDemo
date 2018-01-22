package com.shanlinjinrong.oa.ui.activity.message.contract;

import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public interface SelectedGroupContactContract {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void QueryGroupContactSuccess(List<Contacts> bean);

        void QueryGroupContactFailed(int errorCode, String errorStr);

        void searchContactSuccess(List<Contacts> bean);

        void searchContactFailed(int errorCode, String errorStr);

    }

    interface Presenter extends BasePresenter<SelectedGroupContactContract.View> {

        void QueryGroupContact(String orgId, ArrayList<String> account);

        void searchContact(String name, ArrayList<String> account);

        void QueryGroupContact(String name, String account);

        void QueryGroupContact(String orgId);

        void searchContact(String name);


    }
}
