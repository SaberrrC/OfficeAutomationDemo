package com.shanlinjinrong.oa.ui.activity.message.presenter;


import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.main.bean.UserDetailsBean;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatMessageContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

public class EaseChatMessagePresenter extends HttpPresenter<EaseChatMessageContract.View> implements EaseChatMessageContract.Presenter {

    @Inject
    public EaseChatMessagePresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void searchUserDetails(String code) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        httpParams.put("codeList", code);
        mKjHttp.get(ApiJava.CODE_SEARCH_USER_DETAILS, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    UserDetailsBean userDetailsBean = new Gson().fromJson(t, UserDetailsBean.class);
                    if (userDetailsBean != null) {
                        switch (userDetailsBean.getCode()) {
                            case ApiJava.REQUEST_CODE_OK:
                                for (int i = 0; i < userDetailsBean.getData().size(); i++) {
                                    if (mView != null)
                                        mView.searchUserDetailsSuccess(userDetailsBean.getData().get(i));
                                }
                                break;
                            default:
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
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
}