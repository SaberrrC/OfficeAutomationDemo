package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.CommonRequestBean;
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
    public void modifyMeetingRooms(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.post(Api.MODIFY_NEW_MEETING, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    CommonRequestBean requestStatus = new Gson().fromJson(t, new TypeToken<CommonRequestBean>() {
                    }.getType());
                    switch (requestStatus.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.modifyMeetingRoomsSuccess();
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                                mView.uidNull(requestStatus.getCode());
                        default:
                            if (mView != null)
                                mView.modifyMeetingRoomsFailed(0, requestStatus.getMessage());
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
                    if (mView != null) {
                        mView.modifyMeetingRoomsFailed(errorNo, strMsg);
                        mView.requestFinish();
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
                        mView.requestFinish();
                        mView.modifyMeetingRoomsFailed(-2, "");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
