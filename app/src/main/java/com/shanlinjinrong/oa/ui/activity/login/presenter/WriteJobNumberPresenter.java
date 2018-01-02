package com.shanlinjinrong.oa.ui.activity.login.presenter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.login.bean.QueryPhoneBean;
import com.shanlinjinrong.oa.ui.activity.login.bean.RequestCodeBean;
import com.shanlinjinrong.oa.ui.activity.login.contract.WriteJobNumberContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 获取验证码 presenter
 */
public class WriteJobNumberPresenter extends HttpPresenter<WriteJobNumberContract.View> implements WriteJobNumberContract.Presenter {

    @Inject
    public WriteJobNumberPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    //获取验证码
    @Override
    public void getIdentifyingCode() {
        mKjHttp.cleanCache();
        mKjHttp.get(Api.SENDS_CAPTCHA, new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    RequestCodeBean requestStatus = new Gson().fromJson(t, new TypeToken<RequestCodeBean>() {
                    }.getType());

                    switch (requestStatus.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.getIdentifyingCodeSuccess(requestStatus.getData().getImg(), requestStatus.getData().getKeyCode());
                            break;
                        default:
                            break;
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.getIdentifyingCodeFailed(errorNo);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void searchUser(String imgCode, String keyCode, String userCode) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("code", userCode);
        httpParams.put("imgCode", imgCode);
        httpParams.put("keyCode", keyCode);
        mKjHttp.post(Api.USERS_SEARCH, httpParams, new HttpCallBack() {
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
                    QueryPhoneBean queryPhoneBean = new Gson().fromJson(t, new TypeToken<QueryPhoneBean>() {
                    }.getType());
                    switch (queryPhoneBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.searchUserSuccess(queryPhoneBean.getData().getPhone());
                            break;
                        case ApiJava.RESPONSES_CODE_NO_CONTENT://查询无结果
                            mView.searchUserEmpty("查询无结果");
                            break;
                        default:
                            if (mView != null)
                                mView.searchUserFailed(0, queryPhoneBean.getMessage());
                            break;
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

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.hideLoading();
                        mView.searchUserFailed(errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
