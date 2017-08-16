package com.shanlin.oa.net;

import android.content.Context;

import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cuieney on 14/08/2017.
 */

public class HeadInterceptor implements Interceptor {
    private Context context;

    public HeadInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("token", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_TOKEN))
                .build();
        return chain.proceed(request);
    }
}
