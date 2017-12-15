package com.shanlinjinrong.oa.ui.activity.contracts.presenter;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.contracts.contract.ContactDetailsContract;
import com.shanlinjinrong.oa.ui.activity.main.bean.UserDetailsBean;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 作者：王凤旭
 * 时间：2017/11/30
 * 描述：
 */

public class ContactDetailsPresenter extends HttpPresenter<ContactDetailsContract.View> implements ContactDetailsContract.Presenter {
    @Inject
    public ContactDetailsPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void searchUserDetails(String code) {
        mKjHttp.cleanCache();
        mKjHttp.phpJsonGet(Api.SEARCH_USER_DETAILS + "?code=" + code, new HttpParams(), new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null)
                        mView.showLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    UserDetailsBean userDetailsBean = new Gson().fromJson(t, UserDetailsBean.class);
                    if (userDetailsBean != null) {
                        switch (userDetailsBean.getCode()) {
                            case Api.RESPONSES_CODE_OK:
                                for (int i = 0; i < userDetailsBean.getData().size(); i++) {
                                    if (mView != null)
                                        mView.searchUserDetailsSuccess(userDetailsBean.getData().get(i));
                                }
                                break;
                            default:
                                if (mView != null) {
                                    mView.searchUserDetailsFailed(userDetailsBean.getCode(), userDetailsBean.getInfo());
                                    mView.hideLoading();
                                }
                                break;
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.hideLoading();
                        mView.searchUserDetailsFailed(errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null)
                        mView.hideLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
