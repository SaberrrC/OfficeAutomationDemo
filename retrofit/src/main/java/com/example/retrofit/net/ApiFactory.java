package com.example.retrofit.net;


import com.example.retrofit.net.api.JavaApi;
import com.example.retrofit.net.api.UserApi;
import com.example.retrofit.retrofit.ClientBuilder;

import rx.android.BuildConfig;

public class ApiFactory {

    private static UserApi userApi;
    private static JavaApi javaApi;

    public static void init() {
        String userHost = "";
        String javaHost = "";
        if (BuildConfig.DEBUG) {
            userHost = ApiConstant.USERINFO_DEV_HOST;
            javaHost = ApiConstant.JAVA_TEST_HOST;

        } else {
            userHost = ApiConstant.USERINFO_DEV_HOST;
            javaHost = ApiConstant.JAVA_TEST_HOST;
        }
        userApi = ClientBuilder.build(userHost, UserApi.class);

        //TODO 开发要更换
        javaApi = ClientBuilder.build(javaHost, JavaApi.class);
    }

    public static UserApi getUserApi() {
        if (userApi == null) {
            init();
        }
        return userApi;
    }

    public static JavaApi getJavaApi() {
        if (javaApi == null) {
            init();
        }
        return javaApi;
    }


}
