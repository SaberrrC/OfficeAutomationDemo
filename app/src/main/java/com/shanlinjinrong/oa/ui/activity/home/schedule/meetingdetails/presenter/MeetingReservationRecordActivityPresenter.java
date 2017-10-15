package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingDetailsActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingReservationRecordActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by tonny on 2017/10/11.
 */

public class MeetingReservationRecordActivityPresenter extends HttpPresenter<MeetingReservationRecordActivityContract.View> implements MeetingReservationRecordActivityContract.Presenter {

    @Inject
    public MeetingReservationRecordActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    private ReservationRecordBean.DataBean dataBean;

    @Override
    public void getMeetingRecord() {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.phpJsonGet(Api.NEW_MEETING_RECORD, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    List<ReservationRecordBean.DataBean> data = new ArrayList<>();
                    ReservationRecordBean reservationRecordBean = new ReservationRecordBean();
                    JSONObject jsonObject = new JSONObject(t);
                    reservationRecordBean.setCode(jsonObject.getInt("code"));
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (reservationRecordBean.getCode() == Api.RESPONSES_CODE_OK) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            dataBean = new ReservationRecordBean.DataBean();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            dataBean.setContent(jsonObject1.getString("content"));
                            dataBean.setTitle(jsonObject1.getString("title"));
                            dataBean.setStart_time(jsonObject1.getString("start_time") + "");
                            data.add(dataBean);
                        }
                        mView.getMeetingRecordSuccess(data);
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
