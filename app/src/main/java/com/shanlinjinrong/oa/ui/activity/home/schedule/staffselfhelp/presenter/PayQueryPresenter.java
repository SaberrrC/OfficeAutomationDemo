package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;


import com.example.retrofit.model.responsebody.QueryPayResponse;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.PayQueryContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class PayQueryPresenter extends HttpPresenter<PayQueryContract.View> implements PayQueryContract.Presenter {

    @Inject
    public PayQueryPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void payQueryInfo(String date) {
        HttpMethods.getInstance().getPayInfoData(date, new Subscriber<QueryPayResponse>() {
            @Override
            public void onStart() {
                super.onStart();
                try {
                    mView.showLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompleted() {
                try {
                    mView.payQueryInfoFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    mView.payQueryInfoFinish();
                    if (e instanceof HttpException) {
                        if (((HttpException) e).code() > 500) {
                            mView.payQueryInfoFailed(((HttpException) e).code(), "服务器异常，请稍后重试！");
                        }
                        mView.uidNull(((HttpException) e).code());
                    } else if (e instanceof SocketTimeoutException) {
                        mView.payQueryInfoFailed(-1, "服务器繁忙，请稍后再查询！");
                    } else if (e instanceof NullPointerException) {
                        mView.payQueryInfoFailed(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof ConnectException) {
                        mView.payQueryInfoFailed(-1, "网络不通，请检查网络连接！");
                    }
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onNext(QueryPayResponse queryPayBody) {
                try {
                    mView.payQueryInfoSuccess(queryPayBody);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
