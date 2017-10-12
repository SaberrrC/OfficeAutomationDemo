package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingDetailsActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingReservationRecordActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by tonny on 2017/10/11.
 */

public class MeetingReservationRecordActivityPresenter extends HttpPresenter<MeetingReservationRecordActivityContract.View> implements MeetingReservationRecordActivityContract.Presenter {

    @Inject
    public MeetingReservationRecordActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void getMeetingRecord() {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.phpJsonGet(Api.NEW_MEETING_RECORD, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    ReservationRecordBean reservationRecordBean = new Gson().fromJson(t, ReservationRecordBean.class);
                    if (reservationRecordBean.getCode() == Api.RESPONSES_CODE_OK) {
                        mView.getMeetingRecordSuccess(reservationRecordBean.getData());
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);

            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }
}