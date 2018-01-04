package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingReservationRecordActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 新会议室记录详情
 */

public class MeetingReservationRecordActivityPresenter extends HttpPresenter<MeetingReservationRecordActivityContract.View> implements MeetingReservationRecordActivityContract.Presenter {

    @Inject
    public MeetingReservationRecordActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void getMeetingRecord(HttpParams httpParams, int page, int num, final boolean isLoadMore) {
        httpParams.put("currentPage", page);
        httpParams.put("pageSize", num);

        mKjHttp.post(ApiJava.NEW_MEETING_RECORD, httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    ReservationRecordBean recordBean = new Gson().fromJson(t, new TypeToken<ReservationRecordBean>() {
                    }.getType());
                    switch (recordBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.getMeetingRecordSuccess(recordBean.getData());
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                                mView.uidNull(recordBean.getCode());
                        default:
                            if (mView != null)
                                mView.getMeetingRecordFailed(0, recordBean.getMessage());
                            break;
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.getMeetingRecordFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
