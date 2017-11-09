package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;

import android.content.Context;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.net.ApiException;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.MyAttendenceActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

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
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    mView.uidNull(((HttpException) e).code());
                }
            }

            @Override
            public void onNext(MyAttandanceResponse myAttandanceResponse) {
                mView.sendDataSuccess(myAttandanceResponse);
            }
        });
    }
}
