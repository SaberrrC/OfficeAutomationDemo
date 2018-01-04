package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.CommonRequestBean;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRecordInfo;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingInfoFillOutActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

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
        mKjHttp.post(ApiJava.ADD_NEW_MEETING, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null)
                        mView.showLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
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
                                mView.addMeetingRoomsSuccess();
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                                mView.uidNull(requestStatus.getCode());
                            break;
                        default:
                            if (mView != null)
                                mView.addMeetingRoomsFailed(0, requestStatus.getMessage());
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
                        mView.requestFinish();
                        mView.addMeetingRoomsFailed(errorNo, strMsg);
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
                        mView.addMeetingRoomsFailed(-2, "");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
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
        mKjHttp.get(ApiJava.LOOK_NEW_MEETING_INFO + "?id=" + id, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null)
                        mView.showLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    if (mView != null) {
                        mView.requestFinish();
                    }
                    MeetingRecordInfo meetingRecordInfo = new Gson().fromJson(t, new TypeToken<MeetingRecordInfo>() {
                    }.getType());
                    switch (meetingRecordInfo.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.lookMeetingRoomsSuccess(meetingRecordInfo);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                                mView.uidNull(meetingRecordInfo.getCode());
                            break;
                        default:
                            mView.lookMeetingRoomsFailed(0, meetingRecordInfo.getMessage());
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
                        mView.lookMeetingRoomsFailed(errorNo, strMsg);
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
                    if (mView != null)
                        mView.requestFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //删除会议室
    @Override
    public void deleteMeetingRooms(int id) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        httpParams.put("id", id);
        httpParams.put("send_type", 1);
        mKjHttp.post(ApiJava.DELETE_NEW_MEETING, httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null)
                        mView.showLoading();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    if (mView != null)
                        mView.requestFinish();
                    CommonRequestBean requestStatus = new Gson().fromJson(t, new TypeToken<CommonRequestBean>() {
                    }.getType());
                    switch (requestStatus.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.deleteMeetingRoomsSuccess();
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                                mView.uidNull(requestStatus.getCode());
                            break;
                        default:
                            mView.deleteMeetingRoomsFailed(0, requestStatus.getMessage());
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
                        mView.deleteMeetingRoomsFailed(errorNo, strMsg);
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
                    if (mView != null)
                        mView.requestFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
