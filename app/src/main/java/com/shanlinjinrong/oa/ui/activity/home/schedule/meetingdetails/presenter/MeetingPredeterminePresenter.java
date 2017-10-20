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
        mKjHttp.phpJsonGet(Api.NEW_MEETING_ALR_MEETING + meetingId, new HttpParams(), new HttpCallBack() {

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    MeetingBookItem recordBean = new Gson().fromJson(t, MeetingBookItem.class);
                    switch (recordBean.getCode()) {
                        case Api.RESPONSES_CODE_OK:
                            mView.getMeetingPredetermineSuccess(recordBean.getData());
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(recordBean.getCode());
                            break;
                        default:
                            mView.getMeetingPredetermineFailed(recordBean.getCode(), recordBean.getInfo());
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.getMeetingPredetermineFailed(errorNo, strMsg);
            }
        });
    }
}
