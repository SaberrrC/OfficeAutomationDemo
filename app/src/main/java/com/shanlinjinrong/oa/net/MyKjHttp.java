package com.shanlinjinrong.oa.net;

import com.shanlinjinrong.oa.BuildConfig;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.Request;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.utils.netutil
 * Author:Created by Tsui on Date:2016/11/17 15:52
 * Description:
 */
public class MyKjHttp extends KJHttp {
    private String baseUrl;
    private String baseJavaUrl;


    public MyKjHttp() {
        //TODO 暂时废弃
        //baseUrl = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.BASE_URL);
        baseUrl = BuildConfig.BASE_URL;
        baseJavaUrl = BuildConfig.BASE_JAVA_URL;
        HttpConfig.TIMEOUT = 10000;
    }


    public Request<byte[]> post(String url, HttpParams params, HttpCallBack callback) {
        params.put("uid", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID));
        params.put("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN));
        return super.post(baseUrl + url, params, callback);
    }


    public Request<byte[]> get(String url, HttpParams params, HttpCallBack callback) {
        params.put("uid", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID));
        params.put("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN));
        return super.get(baseUrl + url, params, callback);
    }


    @Override
    public Request<byte[]> jsonPost(String url, HttpParams params, HttpCallBack callback) {
        params.putHeaders("uid", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID));
        params.putHeaders("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN));
        return super.jsonPost(baseJavaUrl + url, params, callback);

    }

    @Override
    public Request<byte[]> jsonGet(String url, HttpParams params, HttpCallBack callback) {
        params.putHeaders("uid", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID));
        params.putHeaders("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN));
        return super.jsonGet(baseJavaUrl + url, params, callback);
    }

}
