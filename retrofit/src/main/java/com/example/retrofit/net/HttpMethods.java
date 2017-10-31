package com.example.retrofit.net;

import com.example.retrofit.model.HttpResult;
import com.example.retrofit.model.requestbody.AliCheckRequestBean;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by gaobin on 2017/8/14.
 */
public class HttpMethods {

    public void getQueryUserSetting(AliCheckRequestBean body, Subscriber<String> subscriber) {
        Observable<String> observable = ApiFactory.getUserApi().getValidCode(body)
                .map(new HttpResultFuncType1<String>());
        toSubscribe(observable, subscriber);
    }


    //----------------------华丽的分界线-----------------------

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFuncType1<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCode() > 1000) {
                throw new ApiException(httpResult.getCode(),1);
            }
            return httpResult.getData();
        }
    }

    private class HttpResultFuncType2<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCode() > 10000) {
                throw new ApiException(httpResult.getCode(),2);
            }
            return httpResult.getData();
        }
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpStringFunc<T> implements Func1<T, T> {

        @Override
        public T call(T httpResult) {
            return httpResult;
        }
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
