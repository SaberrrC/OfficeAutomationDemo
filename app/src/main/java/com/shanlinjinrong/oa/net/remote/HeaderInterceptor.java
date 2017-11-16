package com.shanlinjinrong.oa.net.remote;

import com.example.retrofit.net.RetrofitConfig;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * API请求拦截器
 */
public class HeaderInterceptor implements Interceptor {

    public HeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = addHeaders(chain.request());
        try {
            Response response = chain.proceed(request);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Request addHeaders(Request request) {
        return request.newBuilder()
                .addHeader("token", AppConfig.getAppConfig(AppManager.mContext).getPrivateToken())
                .addHeader("uid", AppConfig.getAppConfig(AppManager.mContext).getPrivateUid())
                .build();
    }


}
