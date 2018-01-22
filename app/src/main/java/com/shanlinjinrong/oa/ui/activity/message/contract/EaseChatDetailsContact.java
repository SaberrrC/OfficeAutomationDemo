package com.shanlinjinrong.oa.ui.activity.message.contract;

import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public interface EaseChatDetailsContact {

    interface View extends BaseView {

        void showLoading();

        void hideLoading();

        void searchUserListInfoSuccess(List<GroupUserInfoResponse> userInfo);

        void searchUserListInfoFailed(int errorCode, String errorMsg);
    }

    interface Presenter extends BasePresenter<EaseChatDetailsContact.View> {

        void searchUserListInfo(String codeList);
    }
}
