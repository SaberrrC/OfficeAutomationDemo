package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;


import com.example.retrofit.model.HttpResult;
import com.example.retrofit.model.responsebody.QueryPayResponse;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.net.remote.ApiModule;
import com.shanlinjinrong.oa.net.remote.HttpConfig;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.PayQueryContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

//薪资查询
public class PayQueryPresenter extends HttpPresenter<PayQueryContract.View> implements PayQueryContract.Presenter {

    private CompositeSubscription mSubscription;

    @Inject
    public PayQueryPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void payQueryInfo(String date) {

        Subscription subscribe = ApiModule.getInstance().getHttpAPIWrapper().getPayInfoData(date)
                .subscribe(new Subscriber<HttpResult<QueryPayResponse>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mView.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.payQueryInfoFinish();
                        mView.payQueryInfoFailed(HttpConfig.CONTENT_EMPETY, AppManager.sToastManager.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult<QueryPayResponse> queryPayResponseHttpResult) {
                        try {
                            mView.payQueryInfoFinish();
                            if (queryPayResponseHttpResult.getCode().equals(HttpConfig.REQUEST_CODE_OK)) {
                                if (queryPayResponseHttpResult.getData() != null) {
                                    QueryPayResponse data = queryPayResponseHttpResult.getData();
                                    mView.payQueryInfoSuccess(data);
                                } else {
                                    mView.payQueryInfoFailed(HttpConfig.CONTENT_EMPETY, "暂无当月薪资信息！");
                                }
                            } else {
                                mView.payQueryInfoFailed(HttpConfig.CONTENT_EMPETY, queryPayResponseHttpResult.getMessage());
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
        mSubscription.add(subscribe);
    }

    @Override
    public void unSubscribe() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
