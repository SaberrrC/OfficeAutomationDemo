package com.example.retrofit.net;


import com.example.retrofit.net.api.JavaApi;
import com.example.retrofit.net.api.UserApi;
import com.example.retrofit.retrofit.ClientBuilder;

import rx.android.BuildConfig;

public class ApiFactory {

    private static JavaApi javaApi;

    public static void init() {
        String userHost = "";
        String javaHost = "";
        if (BuildConfig.DEBUG) {
            javaHost = ApiConstant.JAVA_TEST_HOST;

        } else {
            javaHost = ApiConstant.JAVA_TEST_HOST;
        }
        //TODO 开发要更换
        javaApi = ClientBuilder.build(javaHost, JavaApi.class);
    }

    public static JavaApi getJavaApi() {
        if (javaApi == null) {
            init();
        }
        return javaApi;
    }


}
