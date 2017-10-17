package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRecordInfo;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingInfoFillOutActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 选择会议室
 */

public class MeetingInfoFillOutActivityPresenter extends HttpPresenter<MeetingInfoFillOutActivityContract.View> implements MeetingInfoFillOutActivityContract.Presenter {

    @Inject
    public MeetingInfoFillOutActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    /**
     * 添加会议
     *
     * @param httpParams
     */
    @Override
    public void addMeetingRooms(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.phpJsonPost(Api.ADD_NEW_MEETING, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getInt("code")) {
                        case Api.RESPONSES_CODE_OK:
                            mView.addMeetingRoomsSuccess();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(jsonObject.getInt("code"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.addMeetingRoomsFailed(strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /**
     * 获取会议记录
     */
    @Override
    public void lookMeetingRooms(int id) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.phpJsonGet(Api.LOOK_NEW_MEETING_INFO + "?id=" + id, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    MeetingRecordInfo meetingRecordInfo = new Gson().fromJson(t, MeetingRecordInfo.class);
                    switch (meetingRecordInfo.getCode()) {
                        case Api.RESPONSES_CODE_OK:
                            mView.lookMeetingRoomsSuccess(meetingRecordInfo);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(meetingRecordInfo.getCode());
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.lookMeetingRoomsFailed(strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    public void deleteMeetingRooms(int id) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonDelete(Api.DELETE_NEW_MEETING + "/" + id, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getInt("code")) {
                        case Api.RESPONSES_CODE_OK:
                            mView.deleteMeetingRoomsSuccess();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(jsonObject.getInt("code"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.deleteMeetingRoomsFailed(strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    public void modifyMeetingRooms(int id, HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.phpJsonPut(Api.MODIFY_NEW_MEETING + id, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getInt("code")) {
                        case Api.RESPONSES_CODE_OK:
                            mView.modifyMeetingRoomsSuccess();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(jsonObject.getInt("code"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.modifyMeetingRoomsFailed(strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


}
