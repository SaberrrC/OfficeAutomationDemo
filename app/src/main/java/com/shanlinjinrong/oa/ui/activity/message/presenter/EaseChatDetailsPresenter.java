package com.shanlinjinrong.oa.ui.activity.message.presenter;

import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatDetailsContact;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;


import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * @Description：
 * @author：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class EaseChatDetailsPresenter extends HttpPresenter<EaseChatDetailsContact.View> implements EaseChatDetailsContact.Presenter {

    @Inject
    public EaseChatDetailsPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void searchUserListInfo(String codeList) {
        HashMap<String, String> map = new HashMap<>(16);
        map.put("codeList", codeList);
        HttpMethods.getInstance().queryUserListInfo(map, new Subscriber<ArrayList<GroupUserInfoResponse>>() {

            @Override
            public void onStart() {
                super.onStart();
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompleted() {
                try {
                    if (mView != null) {
                        mView.hideLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    if (e instanceof HttpException) {
                        if (((HttpException) e).code() == 401) {
                            mView.uidNull(ApiJava.REQUEST_TOKEN_NOT_EXIST);
                        }else {
                            mView.searchUserListInfoFailed(((HttpException) e).code(), "服务器异常，请稍后重试！");
                        }
                    } else if (e instanceof SocketTimeoutException) {
                        mView.searchUserListInfoFailed(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof NullPointerException) {
                        mView.searchUserListInfoFailed(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof ConnectException || e instanceof SocketException) {
                        mView.searchUserListInfoFailed(-1, "网络不通，请检查网络连接！");
                    }
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @SuppressWarnings("SpellCheckingInspection")
            @Override
            public void onNext(ArrayList<GroupUserInfoResponse> groupUserInfo) {
                try {
                    mView.searchUserListInfoSuccess(groupUserInfo);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
