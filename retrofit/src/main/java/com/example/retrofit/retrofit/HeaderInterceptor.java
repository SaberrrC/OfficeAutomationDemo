package com.example.retrofit.retrofit;
import com.example.retrofit.net.RetrofitConfig;

import java.nio.charset.Charset;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by gaobin on 2017/8/14.
 * API请求拦截器
 */
public class HeaderInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String TAG = "Retrofit";


    public HeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) {
        Request request = addHeaders(chain.request());
        try {
            Response response = chain.proceed(request);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO 测试个人
    private Request addHeaders(Request request) {
        return request.newBuilder()
                .addHeader("token", RetrofitConfig.getInstance().getAuthToken())
                .addHeader("uid",RetrofitConfig.getInstance().getUserId())
                .build();
    }



}
