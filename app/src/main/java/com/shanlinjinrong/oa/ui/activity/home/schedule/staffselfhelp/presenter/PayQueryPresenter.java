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
        //
        //        Subscription subscribe = ApiModule.getInstance().getHttpAPIWrapper().getPayInfoData(date)
        //                .subscribe(new Subscriber<HttpResult<QueryPayResponse>>() {
        //               @Override
        //               public void onStart() {
        //                   super.onStart();
        //                   mView.showLoading();
        //               }
        //
        //               @Override
        //               public void onCompleted() {
        //                   mView.payQueryInfoFinish();
        //               }
        //
        //               @Override
        //               public void onError(Throwable e) {
        //                   Throwable e11 = e;
        //                   mView.payQueryInfoFinish();
        //
        //               }
        //
        //               @Override
        //               public void onNext(HttpResult<QueryPayResponse> queryPayResponseHttpResult) {
        //                   try {
        //                       mView.payQueryInfoFinish();
        //                       if (queryPayResponseHttpResult.getData() != null) {
        //                           QueryPayResponse data = queryPayResponseHttpResult.getData();
        //                           mView.payQueryInfoSuccess(data);
        //                       }
        //                   } catch (Throwable e) {
        //                       e.printStackTrace();
        //                   }
        //               }
        //           });
        //        mSubscription.add(subscribe);

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
                        if (((HttpException) e).code() > 400) {
                            mView.payQueryInfoFailed(((HttpException) e).code(), "服务器异常，请稍后重试！");
                        }
//                        mView.uidNull(((HttpException) e).code());
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

    //    @Override
    //    public void unSubscribe() {
    //        if (!mSubscription.isUnsubscribed()) {
    //            mSubscription.unsubscribe();
    //        }
    //    }
}
