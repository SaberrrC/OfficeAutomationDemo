package com.example.retrofit.net.api;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @Description:
 * @Auther: 华裕银
 * @Email: huayuyin@shanlinjinrong.com
 */
public interface JavaApi {

    @GET("myApply/queryApproveByAll")
    Observable<String> getUpcomingData(@QueryMap Map<String, String> map);
}
