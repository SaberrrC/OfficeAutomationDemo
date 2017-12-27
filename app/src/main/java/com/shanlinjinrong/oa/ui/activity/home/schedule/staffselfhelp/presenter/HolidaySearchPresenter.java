package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;

import android.content.Context;

import com.example.retrofit.model.responsebody.HolidaySearchResponse;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.HolidaySearchContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class HolidaySearchPresenter extends HttpPresenter<HolidaySearchContract.View> implements HolidaySearchContract.Presenter {

    @Inject
    public HolidaySearchPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void getData(String year, String type) {
        HttpMethods.getInstance().getHolidaySearchData(year, type, new Subscriber<ArrayList<HolidaySearchResponse>>() {

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
                    mView.getDataFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    mView.getDataFinish();
                    if (e instanceof HttpException) {
                        if (((HttpException) e).code() > 400) {
                            mView.getDataFailed(((HttpException) e).code(), "服务器异常，请稍后重试！");
                        }
//                        mView.uidNull(((HttpException) e).code());
                    } else if (e instanceof SocketTimeoutException) {
                        mView.getDataFailed(-1, "服务器繁忙，请稍后再查询！");
                    } else if (e instanceof NullPointerException) {
                        mView.getDataFailed(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof ConnectException) {
                        mView.getDataFailed(-1, "网络不通，请检查网络连接！");
                    }
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onNext(ArrayList<HolidaySearchResponse> holidaySearchResponses) {
                if (holidaySearchResponses != null) {
                    mView.getDataSuccess(holidaySearchResponses);
                }
            }
        });
    }
}
