package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingDetailsActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 获取会议详情
 */

public class MeetingDetailsActivityPresenter extends HttpPresenter<MeetingDetailsActivityContract.View> implements MeetingDetailsActivityContract.Presenter {


    @Inject
    public MeetingDetailsActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void getMeetingRooms() {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();

        mKjHttp.get(Api.NEW_MEETINGROOMS, httpParams, new HttpCallBack() {

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                System.out.println(t);
                try {
                    MeetingRoomsBean meetingRoomsBean = new Gson().fromJson(t, MeetingRoomsBean.class);
                    switch (meetingRoomsBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.getMeetingRoomsSuccess(meetingRoomsBean.getData());
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(meetingRoomsBean.getCode());
                            break;
                        case ApiJava.RESPONSES_CODE_NO_CONTENT:
                            mView.getMeetingRoomsEmpty();
                            break;
                        default:
                            mView.getMeetingRoomsFailed(0, meetingRoomsBean.getMessage());
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
                    mView.getMeetingRoomsFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
