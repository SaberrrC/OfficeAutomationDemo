package com.example.retrofit.retrofit;
import com.example.retrofit.BuildConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gaobin on 2017/8/14.
 */
public class ClientBuilder {

    private static final String DEBUG = "debug";

    private static final int DEFAULT_TIMEOUT = 10;

    public static <T> T build(String hostAddress, Class<T> clzz) {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addInterceptor(new HeaderInterceptor());
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.BUILD_TYPE.equals(DEBUG)) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        OkHttpClient okHttpClient = builder.build();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(hostAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        return retrofitBuilder.build().create(clzz);
    }

}
