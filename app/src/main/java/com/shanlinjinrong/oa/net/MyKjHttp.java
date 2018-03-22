package com.shanlinjinrong.oa.net;

import com.example.retrofit.net.ApiConstant;
import com.shanlinjinrong.oa.BuildConfig;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.JsonRequest;
import org.kymjs.kjframe.http.Request;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.utils.netutil
 * Author:Created by Tsui on Date:2016/11/17 15:52
 * Description:
 */
public class MyKjHttp extends KJHttp {
    private String baseJavaUrl;

    public MyKjHttp() {
        HttpConfig.TIMEOUT = 30000;
        if (BuildConfig.DEBUG) {
            baseJavaUrl = ApiConstant.JAVA_TEST_HOST;
        } else {
            baseJavaUrl = ApiConstant.JAVA_TEST_HOST;
        }
    }


    @Override
    public Request<byte[]> post(String url, HttpParams params, HttpCallBack callback) {
        params.putHeaders("uid", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID));
        params.putHeaders("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN));
        params.putHeaders("X-Source", "android");
        return super.post(baseJavaUrl + url, params, callback);
    }

    @Override
    public Request<byte[]> get(String url, HttpParams params, HttpCallBack callback) {
        params.putHeaders("uid", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID));
        params.putHeaders("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN));
        params.putHeaders("X-Source", "android");
        return super.get(baseJavaUrl + url, params, callback);
    }

    @Override
    public Request<byte[]> jsonPost(String url, HttpParams params, HttpCallBack callback) {
        params.putHeaders("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN));
        params.putHeaders("uid", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID));
        params.putHeaders("X-Source", "android");
        return super.jsonPost(baseJavaUrl + url, params, callback);
    }

    /**
     * 使用JSON传参的put请求
     *
     * @param url      地址
     * @param params   参数集
     * @param callback 请求中的回调方法
     * @param useCache 是否缓存本条请求
     */
    public Request<byte[]> jsonPut(String url, HttpParams params,
                                   boolean useCache, HttpCallBack callback) {
        params.putHeaders("uid", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID));
        params.putHeaders("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN));
        params.putHeaders("X-Source", "android");
        Request<byte[]> request = new JsonRequest(Request.HttpMethod.PUT, baseJavaUrl + url, params,
                callback);
        request.setShouldCache(useCache);
        doRequest(request);
        return request;
    }

    public Request<byte[]> jsonPut(String url, HttpParams params, HttpCallBack callback) {
        return jsonPut(url, params, true, callback);
    }

    /**
     * 使用JSON传参的Delete请求
     *
     * @param url      地址
     * @param params   参数集
     * @param callback 请求中的回调方法
     * @param useCache 是否缓存本条请求
     */

    public Request<byte[]> jsonDelete(String url, HttpParams params, boolean useCache, HttpCallBack callback) {
        params.putHeaders("uid", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID));
        params.putHeaders("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN));
        params.putHeaders("X-Source", "android");
        url = baseJavaUrl + url;
        Request<byte[]> request = new JsonRequest(Request.HttpMethod.DELETE, url, params,
                callback);
        request.setShouldCache(useCache);
        doRequest(request);
        return request;
    }

    public Request<byte[]> jsonDelete(String url, HttpParams params, HttpCallBack callback) {
        return jsonDelete(url, params, false, callback);
    }


    @Override
    public void finish(Request<?> request) {
        super.finish(request);
    }
}
