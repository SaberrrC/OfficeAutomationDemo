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

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
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
                try {
                    if (e instanceof HttpException) {
                        if (((HttpException) e).code() > 400) {
                            mView.sendDataFailed(((HttpException) e).code(), "服务器异常，请稍后重试！");
                        }
                        mView.uidNull(((HttpException) e).code());
                    } else if (e instanceof SocketTimeoutException) {
                        mView.sendDataFailed(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof NullPointerException) {
                        mView.sendDataFailed(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof ConnectException || e instanceof SocketException) {
                        mView.sendDataFailed(-1, "网络不通，请检查网络连接！");
                    }
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onNext(MyAttandanceResponse myAttandanceResponse) {
                try {
                    mView.sendDataSuccess(myAttandanceResponse);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void queryDayAttendance(String userId, String date) {
        HashMap<String, String> map = new HashMap<>();
        map.put("begindate", date);
        map.put("userCode", userId);
        HttpMethods.getInstance().getMyAttendanceDayData(map, new Subscriber<ArrayList<MyAttendanceResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                try {
                    if (e instanceof HttpException) {
                        if (((HttpException) e).code() > 400) {
                            mView.sendDataFailed(((HttpException) e).code(), "服务器异常，请稍后重试！");
                        }
                        mView.uidNull(((HttpException) e).code());
                    } else if (e instanceof SocketTimeoutException) {
                        mView.sendDataFailed(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof NullPointerException) {
                        mView.sendDataFailed(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof ConnectException || e instanceof SocketException) {
                        mView.sendDataFailed(-1, "网络不通，请检查网络连接！");
                    }
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onNext(ArrayList<MyAttendanceResponse> myAttendanceResponses) {
                try {
                    if (myAttendanceResponses != null) {
                        mView.queryDayAttendanceSuccess(myAttendanceResponses);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
