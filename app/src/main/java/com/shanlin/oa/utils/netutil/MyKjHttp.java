package com.shanlin.oa.utils.netutil;

import com.shanlin.oa.BuildConfig;
import com.shanlin.oa.utils.LogUtils;

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

    public MyKjHttp() {
        //TODO 暂时废弃
        //baseUrl = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.BASE_URL);
        baseUrl = BuildConfig.BASE_URL;
    }

    @Override
    public Request<byte[]> post(String url, HttpParams params, HttpCallBack callback) {
        LogUtils.e("login->"+baseUrl + url);
        return super.post(baseUrl + url, params, callback);
    }

    @Override
    public Request<byte[]> get(String url, HttpParams params, HttpCallBack callback) {
        return super.get(baseUrl + url, params, callback);
    }
}
