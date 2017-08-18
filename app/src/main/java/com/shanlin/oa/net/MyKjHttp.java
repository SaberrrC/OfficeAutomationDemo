package com.shanlin.oa.net;

import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.Request;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.utils.netutil
 * Author:Created by Tsui on Date:2016/11/17 15:52
 * Description:
 */
public class MyKjHttp extends KJHttp {
    private final String baseUrl;
    private final String uid;
    private final String token;
    private static MyKjHttp mInstance;

    public static MyKjHttp getInstance() {
        if (mInstance == null) {
            synchronized (MyKjHttp.class) {
                if (mInstance == null) {
                    mInstance = new MyKjHttp();
                }
            }
        }
        return mInstance;
    }

    public MyKjHttp() {
        baseUrl = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.BASE_URL);
        uid = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_UID);
        token = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN);
    }

    @Override
    public Request<byte[]> post(String url, HttpParams params, HttpCallBack callback) {
        return super.post(baseUrl + url, params, callback);
    }

    @Override
    public Request<byte[]> get(String url, HttpParams params, HttpCallBack callback) {
        params.put("uid",uid);
        params.put("token",token);
        return super.get(baseUrl + url, params, callback);
    }
}
