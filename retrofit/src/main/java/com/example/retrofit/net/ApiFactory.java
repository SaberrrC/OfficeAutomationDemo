package com.example.retrofit.net;


import com.example.retrofit.BuildConfig;
import com.example.retrofit.net.api.JavaApi;
import com.example.retrofit.net.api.UserApi;
import com.example.retrofit.retrofit.ClientBuilder;

/**
 * Created by gaobin on 2017/8/14.
 */
public class ApiFactory {

    private static UserApi userApi;
    private static JavaApi javaApi;

    public static void init() {
        String userHost = "";
        String javaHost = "";
        if (BuildConfig.DEBUG) {
            userHost = ApiConstant.USERINFO_DEV_HOST;
            javaHost = "";

        } else {
            userHost = ApiConstant.USERINFO_DEV_HOST;
            javaHost = "";
        }
        userApi = ClientBuilder.build(userHost, UserApi.class);
        javaApi=ClientBuilder.build(userHost, JavaApi.class);
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
