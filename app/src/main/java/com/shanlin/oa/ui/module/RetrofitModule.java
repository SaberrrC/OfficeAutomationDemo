package com.shanlin.oa.ui.module;

import com.hyphenate.easeui.Constant;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;
import com.shanlin.oa.net.Api;
import com.shanlin.oa.net.CacheInterceptor;
import com.shanlin.oa.net.HeadInterceptor;

import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cuieney on 14/08/2017.
 */

@Module
public class RetrofitModule {
    private AppManager context;

    public RetrofitModule(AppManager context) {
        this.context = context;
    }

    @Provides
    public OkHttpClient providesClient(CacheInterceptor cacheInterceptor
            , HeadInterceptor headInterceptor
            , Cache cache
            , SSLContext sslContext
            , HostnameVerifier DO_NOT_VERIFY) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置统一的请求头部参数
        builder.addInterceptor(headInterceptor);

        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //设置https
        builder.sslSocketFactory(sslContext.getSocketFactory());
        builder.hostnameVerifier(DO_NOT_VERIFY);
        return builder.build();
    }


    @Provides
    public CacheInterceptor providesCacheInterceptor() {
        return new CacheInterceptor(context);
    }

    @Provides
    public HeadInterceptor providesHeadInterceptor() {
        return new HeadInterceptor(context);
    }

    @Provides
    public SSLContext providesSSLContext() {
        //https
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext;
    }

    @Provides
    public HostnameVerifier providesHostVef() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                return true;
            }
        };
    }


    @Provides
    @Singleton
    public Retrofit providesRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    public Api providesApi(Retrofit retrofit){
        return retrofit.create(Api.class);
    }
}
