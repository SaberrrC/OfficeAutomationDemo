package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingInfoFillOutActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by tonny on 2017/10/12.
 */

public class MeetingInfoFillOutActivityPresenter extends HttpPresenter<MeetingInfoFillOutActivityContract.View> implements MeetingInfoFillOutActivityContract.Presenter{

    @Inject
    public MeetingInfoFillOutActivityPresenter(MyKjHttp mKjHttp)     {
        super(mKjHttp);
    }

    @Override
    public void addMeetingRooms(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.phpJsonPost(Api.ADD_NEW_MEETING, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);

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
