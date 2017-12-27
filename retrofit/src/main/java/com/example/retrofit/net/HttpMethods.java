package com.example.retrofit.net;

import com.example.retrofit.model.HttpResult;
import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.example.retrofit.model.responsebody.QueryPayResponse;
import com.example.retrofit.model.responsebody.CountResponse1;
import com.example.retrofit.model.responsebody.HolidaySearchResponse;
import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.model.responsebody.MyAttendanceResponse;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HttpMethods {


    //获取考勤数据-用于我的考勤-考勤月历-考勤异常三个界面
    public void getMyAttantanceData(Map<String, String> map, Subscriber<MyAttandanceResponse> subscriber) {
        Observable<MyAttandanceResponse> map1 = ApiFactory.getJavaApi().getMyAddantanceData(map).map(new HttpResultFuncTypeJava<MyAttandanceResponse>());
        toSubscribe(map1, subscriber);
    }

    //获取考勤日查询
    public void getMyAttendanceDayData(Map<String, String> map, Subscriber<ArrayList<MyAttendanceResponse>> subscriber) {
        Observable<ArrayList<MyAttendanceResponse>> map1 = ApiFactory.getJavaApi().getMyAttendanceDay(map).map(new HttpResultFuncTypeJava<ArrayList<MyAttendanceResponse>>());
        toSubscribe(map1, subscriber);
    }

    //查询统计人员
    public void getAcountData(Subscriber<ArrayList<CountResponse1>> subscriber) {
        Observable<ArrayList<CountResponse1>> map1 = ApiFactory.getJavaApi().getCountData().map(new HttpResultFuncTypeJava<ArrayList<CountResponse1>>());
        toSubscribe(map1, subscriber);
    }

    //年假查询
    public void getHolidaySearchData(String year, String type, Subscriber<ArrayList<HolidaySearchResponse>> subscriber) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("year", year);
        Observable<ArrayList<HolidaySearchResponse>> map1 = ApiFactory.getJavaApi().getHolidaySearchData(map).map(new HttpResultFuncTypeJava<ArrayList<HolidaySearchResponse>>());
        toSubscribe(map1, subscriber);
    }

    //薪资查询
    public void getPayInfoData(String date, Subscriber<QueryPayResponse> subscriber) {
        HashMap<String, String> map = new HashMap<>();
        map.put("date", date);
        Observable<QueryPayResponse> map1 = ApiFactory.getJavaApi().getPayInfoData(map).map(new HttpResultFuncTypeJava<QueryPayResponse>());
        toSubscribe(map1, subscriber);
    }

    //----------------------发起审批-----------------------

    //审批编码查询
    public void getQueryMonoCode(int type, Subscriber<String> subscriber) {
        HashMap<String, String> map = new HashMap<>();
        map.put("billType", type + "");
        Observable map1 = ApiFactory.getJavaApi().getQueryMonoCode(map).map(new HttpResultFuncTypeJava());
        toSubscribe(map1, subscriber);
    }

    public void getApproveData(HashMap<String, String> map, Subscriber<String> subscriber) {
        Observable map1 = ApiFactory.getJavaApi().getApproveData(map).map(new HttpResultFuncTypeJava());
        toSubscribe(map1, subscriber);
    }

    public void getSelectData(HashMap<String, String> map, Subscriber<String> subscriber) {
        Observable map1 = ApiFactory.getJavaApi().getApproveData(map).map(new HttpResultFuncTypeJava());
        toSubscribe(map1, subscriber);
    }

    public void postAgreeDisagree(HashMap<String, String> map, Subscriber<String> subscriber) {
        Observable map1 = ApiFactory.getJavaApi().getApproveData(map).map(new HttpResultFuncTypeJava());
        toSubscribe(map1, subscriber);
    }

    //----------------------群组 聊天-----------------------

    public void queryUserListInfo(Map<String, String> map, Subscriber<ArrayList<GroupUserInfoResponse>> subscriber) {
        Observable<ArrayList<GroupUserInfoResponse>> map1 = ApiFactory.getJavaApi().queryUserListInfo(map).map(new HttpResultFuncTypeJava<ArrayList<GroupUserInfoResponse>>());
        toSubscribe(map1, subscriber);
    }

    //    //出差申请
    //    public void submitEvectionApply(EvectionBody body, Subscriber<String> subscriber) {
    //        Observable map1 = ApiFactory.getJavaApi().submitEvectionApply(body).map(new HttpResultFuncTypeJava());
    //        toSubscribe(map1, subscriber);
    //    }


    //    //加班申请
    //    public void addWorkApply(AddWorkBody body, Subscriber<String> subscriber) {
    //        Observable map1 = ApiFactory.getJavaApi().addWorkApply(body).();
    //        toSubscribe(map1, subscriber);
    //    }


    //----------------------华丽的分界线-----------------------

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFuncTypeJava<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            HttpResult<T> httpResult1 = httpResult;
            if (!httpResult.getCode().equals("000000")) {
                try {
                    throw new ApiException(httpResult.getCode(), 1, httpResult.getMessage());
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            return httpResult.getData();
        }
    }

    private class HttpResultFuncType2<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCode().equals("10000")) {
                try {
                    throw new ApiException(httpResult.getCode(), 2, httpResult.getMessage());
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            return httpResult.getData();
        }
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpStringFunc<T> implements Func1<T, T> {

        @Override
        public T call(T httpResult) {
            return httpResult;
        }
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
