package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRecordInfo;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

/**
 * 选择会议室
 */
public interface MeetingInfoFillOutActivityContract {


    interface View extends BaseView {

        void showLoading();

        void requestFinish();

        void addMeetingRoomsSuccess();

        void addMeetingRoomsFailed(int errorCode, String strMsg);

        void lookMeetingRoomsSuccess(MeetingRecordInfo info);

        void lookMeetingRoomsFailed(int errorCode, String strMsg);

        void deleteMeetingRoomsSuccess();

        void deleteMeetingRoomsFailed(int errorCode, String strMsg);
    }

    interface Presenter extends BasePresenter<MeetingInfoFillOutActivityContract.View> {

        void addMeetingRooms(HttpParams httpParams);

        void lookMeetingRooms(int id);

        void deleteMeetingRooms(int id);
    }
}
