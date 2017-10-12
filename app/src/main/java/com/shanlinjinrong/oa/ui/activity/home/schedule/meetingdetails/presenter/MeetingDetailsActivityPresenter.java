package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingDetailsActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by tonny on 2017/10/11.
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

        mKjHttp.phpJsonGet(Api.NEW_MEETINGROOMS, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    MeetingRoomsBean meetingRoomsBean = new Gson().fromJson(t, MeetingRoomsBean.class);
                    if (meetingRoomsBean.getCode() == Api.RESPONSES_CODE_OK) {
                        mView.getMeetingRoomsSuccess(meetingRoomsBean.getData());
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.getMeetingRoomsFailed(strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }
}