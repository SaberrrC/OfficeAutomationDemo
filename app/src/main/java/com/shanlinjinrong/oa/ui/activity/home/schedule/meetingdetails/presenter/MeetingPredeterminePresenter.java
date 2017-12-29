package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingBookItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingPredetermineContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 获取会议室占用时间段
 */

public class MeetingPredeterminePresenter extends HttpPresenter<MeetingPredetermineContract.View> implements MeetingPredetermineContract.Presenter {


    @Inject
    public MeetingPredeterminePresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void getMeetingPredetermine(int meetingId) {
        mKjHttp.cleanCache();
        mKjHttp.phpJsonGet(Api.NEW_MEETING_ALR_MEETING + "?room_id=" + meetingId, new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    MeetingBookItem recordBean = new Gson().fromJson(t, MeetingBookItem.class);
                    switch (recordBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.getMeetingPredetermineSuccess(recordBean.getData());
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                                mView.uidNull(recordBean.getCode());
                            break;
                        default:
                            mView.getMeetingPredetermineFailed(0, recordBean.getMessage());
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
                    mView.getMeetingPredetermineFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void modifyMeetingRooms(int id, HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.phpJsonPut(Api.MODIFY_NEW_MEETING + id, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                mView.requestFinish();
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getInt("code")) {
                        case Api.RESPONSES_CODE_OK:
                            mView.modifyMeetingRoomsSuccess();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
//                            mView.uidNull(jsonObject.getInt("code"));
                            break;
                        case Api.RESPONSES_CODE_NO_CONTENT:
                            mView.requestNetworkError();
                            break;
                        default:
                            mView.modifyMeetingRoomsFailed(jsonObject.getInt("code"), jsonObject.getString("info"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.modifyMeetingRoomsFailed(errorNo, strMsg);
                mView.requestFinish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
                mView.modifyMeetingRoomsFailed(-2, "");
            }
        });
    }
}
