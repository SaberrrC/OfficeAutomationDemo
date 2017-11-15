package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingBookItem;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

import java.util.List;

/**
 * 获取会议室占用时间段
 */
public interface MeetingPredetermineContract {

    interface View extends BaseView {

        void showLoading();

        void requestFinish();

        void requestNetworkError();

        void getMeetingPredetermineSuccess(List<MeetingBookItem.DataBean> dataBeen);

        void getMeetingPredetermineFailed(int errorCode, String msgStr);

        void modifyMeetingRoomsSuccess();

        void modifyMeetingRoomsFailed(int errorCode,String strMsg);
    }

    interface Presenter extends BasePresenter<MeetingPredetermineContract.View> {

        void getMeetingPredetermine(int meetingId); //获取会议室被预订过的时间段


        void modifyMeetingRooms(int id, HttpParams httpParams);
    }
}
