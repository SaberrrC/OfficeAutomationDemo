package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;

import android.content.Context;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.MyAttendenceActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;


//我的考勤接口
public class MyAttendenceActivityPresenter extends HttpPresenter<MyAttendenceActivityContract.View> implements MyAttendenceActivityContract.Presenter {

    @Inject
    public MyAttendenceActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void sendData(String userId, String year, String month, Context context) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userCode", userId);
        map.put("date", year + "-" + month);
        HttpMethods.getInstance().getMyAttantanceData(map, new Subscriber<MyAttandanceResponse>() {

            @Override
            public void onStart() {
                super.onStart();
                try {
                    mView.showLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompleted() {
                try {
                    mView.sendDataFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    mView.sendDataFinish();
                    if (e instanceof HttpException) {
                        mView.uidNull(((HttpException) e).code());
                    } else if (e instanceof SocketTimeoutException) {
                        mView.sendDataFailed("-1", "网络不通，请检查网络连接！");
                    } else if (e instanceof NullPointerException) {
                        mView.sendDataFailed("-1", "网络不通，请检查网络连接！");
                    } else if (e instanceof ConnectException) {
                        mView.sendDataFailed("-1", "网络不通，请检查网络连接！");
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
}
