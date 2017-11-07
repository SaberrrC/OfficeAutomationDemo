package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.model.responsebody.MyAttendanceResponse;
import com.example.retrofit.net.ApiException;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.contract.CreateNoteContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.AttandanceMonthContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.MyAttendenceActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;


public class AttandanceMonthPresenter extends HttpPresenter<AttandanceMonthContract.View> implements AttandanceMonthContract.Presenter {

    @Inject
    public AttandanceMonthPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void sendData(String userId, String year, String month, Context context) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userCode", userId);
        map.put("date", year + "-" + month);
        HttpMethods.getInstance().getMyAttantanceData(map, new Subscriber<MyAttandanceResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                if (e instanceof ApiException) {
                    ApiException baseException = (ApiException) e;
                    String code = baseException.getCode();
                    String message = baseException.getMessage();
                    mView.sendDataFailed(code, message);
                } else {
                    mView.sendDataFailed("555", "请检查网络！");
                }
            }

            @Override
            public void onNext(MyAttandanceResponse myAttandanceResponse) {
                mView.sendDataSuccess(myAttandanceResponse);
            }
        });
    }

    @Override
    public void queryDayAttendance(String date) {
        HashMap<String, String> map = new HashMap<>();
        map.put("begindate", date);
        HttpMethods.getInstance().getMyAttendanceDayData(map, new Subscriber<ArrayList<MyAttendanceResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ApiException) {
                    ApiException baseException = (ApiException) e;
                    String code = baseException.getCode();
                    String message = baseException.getMessage();
                    mView.queryDayAttendanceFailed(code, message);
                } else {
                    mView.queryDayAttendanceFailed("555", "请检查网络！");
                }
            }

            @Override
            public void onNext(ArrayList<MyAttendanceResponse> myAttendanceResponses) {
                mView.queryDayAttendanceSuccess(myAttendanceResponses);
            }
        });
    }
}
