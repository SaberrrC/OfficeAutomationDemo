package com.example.retrofit.net.api;

import com.example.retrofit.model.HttpResult;
import com.example.retrofit.model.responsebody.CountResponse1;
import com.example.retrofit.model.responsebody.HolidaySearchResponse;
import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.model.responsebody.MyAttendanceResponse;

import java.util.ArrayList;
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

    //我的考勤
    @GET("WorkAttendance/getWorkAttendance ")
    Observable<HttpResult<MyAttandanceResponse>> getMyAddantanceData(@QueryMap Map<String, String> map);

    //我的考勤
    @GET("nchrCardRecord/getCardRecord")
    Observable<HttpResult<MyAttendanceResponse>> getMyAttendanceDay(@QueryMap Map<String, String> map);

    //获取员工及其下属
    @GET("BranchStaff/getNCHRBranchStaff ")
    Observable<HttpResult<ArrayList<CountResponse1>>> getCountData();

    //年假查询
    @GET("nchrHoliday/getHoliday")
    Observable<HttpResult<ArrayList<HolidaySearchResponse>>> getHolidaySearchData(@QueryMap Map<String, String> map);

}
