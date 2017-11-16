package com.shanlinjinrong.oa.net.remote;

import com.example.retrofit.model.HttpResult;
import com.example.retrofit.model.responsebody.QueryPayResponse;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.login.LoginActivity;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HttpAPIWrapper {

    private JavaApi mJavaApi;

    public HttpAPIWrapper(JavaApi javaApi) {
        mJavaApi = javaApi;
    }
    //-------------------------考勤---------------------------

    //薪资查询
    @SuppressWarnings("unchecked")
    public Observable<HttpResult<QueryPayResponse>> getPayInfoData(String date) {
        HashMap<String, String> map = new HashMap<>();
        map.put("date", date);
        return wrapperJava(mJavaApi.getPayInfoData(map)).compose(SCHEDULERS_TRANSFORMER);
    }


    //-----------------------发起审批-------------------------


    //----------------------华丽的分界线-----------------------
    private <T extends HttpResult> Observable<T> wrapperJava(Observable<T> resourceObservable) {
        return resourceObservable.flatMap(baseResponse -> {
            Observable<T> tObservable = Observable.create(subscriber -> {
                if (baseResponse == null) {
                    subscriber.onCompleted();
                } else {
                    //success
                    switch (baseResponse.getCode()) {
                        case HttpConfig.TOKEN_PAST:
                        case HttpConfig.ERROR_TOKEN:
                        case HttpConfig.REQUEST_TOKEN_NOT_EXIST:
                            //Token 失效
                            AppConfig.getAppConfig(AppManager.mContext).clearLoginInfo();
                            LoginActivity.goLoginContextActivity(AppManager.mContext);
                            AppManager.sToastManager.displayLongToast("登陆已失效,请重新登陆！");
                            break;
                        default:
                            subscriber.onNext(baseResponse);
                            break;
                    }
                }
            });
            return tObservable;
        }).doOnError(throwable -> {
            //网络异常超时处理
            if (throwable instanceof ConnectException) {
                AppManager.sToastManager.displayShortToast("网络不通，请检查网络连接！");
            } else if (throwable instanceof retrofit2.HttpException) {
                if (((HttpException) throwable).code() == Api.RESPONSES_JAVA_TOKEN_NO_MATCH) {
                    //Token 失效
                    AppConfig.getAppConfig(AppManager.mContext).clearLoginInfo();
                    LoginActivity.goLoginContextActivity(AppManager.mContext);
                    AppManager.sToastManager.displayLongToast("登陆已失效,请重新登陆！");
                } else if (((HttpException) throwable).code() == 404 || ((HttpException) throwable).code() > 500) {
                    AppManager.sToastManager.displayShortToast(" 服务器异常，请稍后重试！");
                }
            } else if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) {
                AppManager.sToastManager.displayShortToast("连接超时，请稍后重试！");
            } else if (throwable instanceof NullPointerException) {
                AppManager.sToastManager.displayShortToast("网络不通，请检查网络连接！");
            }
        });
    }

    private <T extends HttpResult> Observable<T> wrapperPHP(Observable<T> resourceObservable) {
        return resourceObservable.flatMap(baseResponse -> {
            Observable<T> tObservable = Observable.create(subscriber -> {
                if (baseResponse == null) {
                    subscriber.onCompleted();
                } else {
                    //success
                    switch (baseResponse.getCode()) {
                        case HttpConfig.TOKEN_PAST:
                            //Token 失效
                            AppConfig.getAppConfig(AppManager.mContext).clearLoginInfo();
                            LoginActivity.goLoginContextActivity(AppManager.mContext);
                            AppManager.sToastManager.displayLongToast("登陆已失效,请重新登陆！");
                            break;
                        default:
                            subscriber.onNext(baseResponse);
                            break;
                    }
                }
            });
            return tObservable;
        }).doOnError(throwable -> {
            //网络异常超时处理
            if (throwable instanceof ConnectException) {
                AppManager.sToastManager.displayShortToast("网络不通，请检查网络连接！");
            } else if (throwable instanceof retrofit2.HttpException) {
                if (((HttpException) throwable).code() == Api.RESPONSES_JAVA_TOKEN_NO_MATCH) {
                    //Token 失效
                    AppConfig.getAppConfig(AppManager.mContext).clearLoginInfo();
                    LoginActivity.goLoginContextActivity(AppManager.mContext);
                    AppManager.sToastManager.displayLongToast("登陆已失效,请重新登陆！");
                } else if (((HttpException) throwable).code() == 404 || ((HttpException) throwable).code() > 500) {
                    AppManager.sToastManager.displayShortToast(" 服务器异常，请稍后重试！");
                }
            } else if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) {
                AppManager.sToastManager.displayShortToast("连接超时，请稍后重试！");
            }
        });
    }

    /**
     * 给任何Http的Observable加上在Activity中运行的线程调度器
     */
    public static final Observable.Transformer SCHEDULERS_TRANSFORMER = observable -> {
        return ((Observable) observable).subscribeOn(Schedulers.io())//子线程执行
                .observeOn(AndroidSchedulers.mainThread());//主线程更新
    };
}
