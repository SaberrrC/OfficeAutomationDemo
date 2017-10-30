package com.example.retrofit.net;


import com.example.retrofit.BuildConfig;
import com.example.retrofit.net.api.UserApi;
import com.example.retrofit.retrofit.RestBuilder;

/**
 * Created by zhaojian on 2017/8/14.
 */
public class ApiFactory {

    private static UserApi userApi;

    public static void init() {
        String userHost = "";
        if (BuildConfig.DEBUG) {
            userHost = ApiConstant.USERINFO_DEV_HOST;
        } else {
            userHost = ApiConstant.USERINFO_DEV_HOST;
        }
        userApi = RestBuilder.build(userHost, UserApi.class);
    }

    public static UserApi getUserApi() {
        if (userApi == null) {
            init();
        }
        return userApi;
    }



}
