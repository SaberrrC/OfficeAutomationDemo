package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingBookItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingPredetermineContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by tonny on 2017/10/11.
 */

public class MeetingPredeterminePresenter extends HttpPresenter<MeetingPredetermineContract.View> implements MeetingPredetermineContract.Presenter {


    @Inject
    public MeetingPredeterminePresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void getMeetingPredetermine(int meetingId) {

        mKjHttp.cleanCache();
        mKjHttp.phpJsonGet(Api.NEW_MEETING_ALR_MEETING + meetingId, new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                MeetingBookItem recordBean = new Gson().fromJson(t, MeetingBookItem.class);
                if (recordBean.getCode() == Api.RESPONSES_CODE_OK) {
                    mView.getMeetingPredetermineSuccess(recordBean.getData());
                } else {
                    mView.getMeetingPredetermineFailed(recordBean.getInfo());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.getMeetingPredetermineFailed(strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
}
