package com.shanlinjinrong.oa.net.remote;


import com.facebook.stetho.common.LogUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.shanlinjinrong.oa.BuildConfig;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiModule {
    private OkHttpClient httpClient;
    private Retrofit retrofit;
    private String requestUrl;
    private static ApiModule apiModule;
    private HttpAPIWrapper httpAPIWrapper;
    private boolean urlChange;

    public static ApiModule getInstance() {
        if (apiModule == null) {
            apiModule = new ApiModule();
        }
        return apiModule;
    }

    private void createOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        //stetho抓包工具
        if (BuildConfig.DEBUG) {
            //拦截器
            StethoInterceptor stethoInterceptor = new StethoInterceptor();
            builder.addNetworkInterceptor(stethoInterceptor);
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> {
            //打印retrofit日志
            LogUtil.d("RetrofitLog", "retrofitBack = " + message);
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.connectTimeout(HttpConfig.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(HttpConfig.IO_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(HttpConfig.IO_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(interceptor)
                .build();

        httpClient = builder.build();
    }

    private JavaApi getApiUrl() {
        if (httpClient == null) {
            createOkHttp();
        }

        if (retrofit == null) {
            createRetrofit();
        }
        return retrofit.create(JavaApi.class);
    }


    public HttpAPIWrapper getHttpAPIWrapper() {
        if (urlChange) {
            httpAPIWrapper = null;
            httpClient = null;
            retrofit = null;
            urlChange = false;
        }

        if (httpAPIWrapper == null) {
            httpAPIWrapper = new HttpAPIWrapper(getApiUrl());
        }
        return httpAPIWrapper;
    }

    private void createRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder();
        if (BuildConfig.DEBUG) {
            requestUrl = HttpConfig.JAVA_TEST_HOST;
        } else {
            requestUrl = HttpConfig.JAVA_DEVELOP_HOST;
        }

        retrofit = builder
                .baseUrl(requestUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
