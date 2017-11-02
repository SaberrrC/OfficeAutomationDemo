package com.example.retrofit.net.api;

import com.example.retrofit.model.HttpResult;
import com.example.retrofit.model.requestbody.AliCheckRequestBean;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author:Zou ChangCheng
 * @date:2017/8/25
 * @project:IntelligentSecretary
 * @detail:
 */

public interface UserApi {
    //短信验证码
    @POST("/user/userApi/getAuthCode")
    Observable<HttpResult<String>> getValidCode(@Body AliCheckRequestBean getValidCodeBody);

    //短信验证码
    @GET("/user/userApi/getAuthCode")
    Observable<HttpResult<String>> getValidCode1(@Body AliCheckRequestBean getValidCodeBody);

}
