package com.example.retrofit.net.api;

import com.example.retrofit.model.HttpResult;
import com.example.retrofit.model.ScheduleBean;
import com.example.retrofit.model.UpLoadPortraitsBean;
import com.example.retrofit.model.responsebody.ApporveBodyItemBean;
import com.example.retrofit.model.responsebody.CountResponse1;
import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.example.retrofit.model.responsebody.HolidaySearchResponse;
import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.model.responsebody.MyAttendanceResponse;
import com.example.retrofit.model.responsebody.QueryPayResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;

//import com.example.retrofit.model.requestbody.AddWorkBody;

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

    @GET("myApply/queryApproveByAll;")
    Observable<HttpResult> getApproveData(@QueryMap Map<String, String> map);

    @GET("MyAplication/selectMyAplication")
    Observable<HttpResult> getSelectData(@QueryMap Map<String, String> map);

    @POST("Approve/allApprove")
    Observable<HttpResult> postAgreeDisagree(@Field("approveList[]") List<ApporveBodyItemBean> approveList);

    //----------------------聊天 群组-----------------------

    @POST("user/queryUserByCodes")
    Observable<HttpResult<ArrayList<GroupUserInfoResponse>>> queryUserListInfo(@QueryMap Map<String, String> map);


    //----------------------头像 上传-----------------------

    @Multipart
    @POST("user/upload")
    Observable<HttpResult<UpLoadPortraitsBean>> uploadPortraits(@Part MultipartBody.Part file);

    //提交出差申请
    //    @POST("nchrEvection/submitEvectionApply")
    //    Observable<HttpResult> submitEvectionApply(@Body EvectionBody data);


    //提交加班申请
    //    @POST("nchrEvection/submitEvectionApply")
    //    Observable<HttpResult> addWorkApply(@Body AddWorkBody data);

    // ----------------------日程 管理-----------------------

    @POST("schedule/insertSchedule")
    Observable<HttpResult> calendarSchedule(@Body ScheduleBean scheduleBean);

    //提交出差申请
    //    @POST("nchrEvection/submitEvectionApply")
    //    Observable<HttpResult> submitEvectionApply(@Body EvectionBody data);


    //提交加班申请
    //    @POST("nchrEvection/submitEvectionApply")
    //    Observable<HttpResult> addWorkApply(@Body AddWorkBody data);

}
