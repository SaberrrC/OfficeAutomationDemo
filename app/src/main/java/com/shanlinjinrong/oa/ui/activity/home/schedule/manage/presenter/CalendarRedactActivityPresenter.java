package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter;

import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract.CalendarRedactActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;


import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;


/**
 * 作者：王凤旭
 * 创建时间：2018/1/24
 * 功能描述：
 */

public class CalendarRedactActivityPresenter extends HttpPresenter<CalendarRedactActivityContract.View> implements CalendarRedactActivityContract.Presenter {

    @Inject
    public CalendarRedactActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void addCalendarSchedule(String schedule) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        httpParams.putJsonParams(schedule);
        mKjHttp.jsonPost("schedule/insertSchedule", httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    if (mView != null) {
                        mView.hideLoading();
                        mView.addCalendarScheduleSuccess();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.hideLoading();
                        mView.addCalendarScheduleFailure(errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.hideLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();

                }
            }
        });
    }
}
