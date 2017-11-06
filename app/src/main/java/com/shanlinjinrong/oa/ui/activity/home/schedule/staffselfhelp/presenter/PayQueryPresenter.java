package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;


import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.bean.PayQueryDataBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.PayQueryContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

public class PayQueryPresenter extends HttpPresenter<PayQueryContract.View> implements PayQueryContract.Presenter {

    @Inject
    public PayQueryPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void payQueryInfo(String date) {
        mKjHttp.cleanCache();
        mKjHttp.jsonGet(ApiJava.QUERYSALART + "?date=" + date, new HttpParams(), new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    PayQueryDataBean payQueryDataBean = new Gson().fromJson(t, PayQueryDataBean.class);
                    switch (payQueryDataBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.payQueryInfoSuccess(payQueryDataBean.getData());
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(0);
                            break;
                        default:
                            mView.payQueryInfoFailed(Integer.parseInt(payQueryDataBean.getCode()), payQueryDataBean.getMessage());
                            break;
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.payQueryInfoFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    mView.payQueryInfoFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
