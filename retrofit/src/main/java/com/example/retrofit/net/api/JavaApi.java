package com.example.retrofit.net.api;

import com.example.retrofit.model.HttpResult;
import com.example.retrofit.model.requestbody.AddWorkBody;
import com.example.retrofit.model.requestbody.EvectionBody;
import com.example.retrofit.model.responsebody.QueryPayResponse;
import com.example.retrofit.model.responsebody.CountResponse1;
import com.example.retrofit.model.responsebody.HolidaySearchResponse;
import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.model.responsebody.MyAttendanceResponse;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @Description:
 * @Auther: 华裕银
 * @Email: huayuyin@shanlinjinrong.com
 */
public interface JavaApi {

    //我的考勤
    @GET("WorkAttendance/getWorkAttendance ")
    Observable<HttpResult<MyAttandanceResponse>> getMyAddantanceData(@QueryMap Map<String, String> map);

    //我的考勤
    @GET("nchrCardRecord/getCardRecord")
    Observable<HttpResult<ArrayList<MyAttendanceResponse>>> getMyAttendanceDay(@QueryMap Map<String, String> map);

    //获取员工及其下属
    @GET("BranchStaff/getNCHRBranchStaff ")
    Observable<HttpResult<ArrayList<CountResponse1>>> getCountData();

    //年假查询
    @GET("nchrHoliday/getHoliday")
    Observable<HttpResult<ArrayList<HolidaySearchResponse>>> getHolidaySearchData(@QueryMap Map<String, String> map);

    //薪资查询
    @GET("nchrsalary/querySalary")
    Observable<HttpResult<QueryPayResponse>> getPayInfoData(@QueryMap Map<String, String> map);


    //----------------------发起审批-----------------------
    //获取单据编号接口
    @GET("nchrcommon/getBillCode")
    Observable<HttpResult> getQueryMonoCode(@QueryMap Map<String, String> map);

    //提交出差申请
    @POST("nchrEvection/submitEvectionApply")
    Observable<HttpResult> submitEvectionApply(@Body EvectionBody data);

    //提交加班申请
    @POST("nchrEvection/submitEvectionApply")
    Observable<HttpResult> addWorkApply(@Body AddWorkBody data);
}
