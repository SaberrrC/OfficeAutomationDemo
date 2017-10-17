package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRecordInfo;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

/**
 * Created by tonny on 2017/10/12.
 */

public interface MeetingInfoFillOutActivityContract {


    interface View extends BaseView {

        void showLoading();

        void requestFinish();

        void addMeetingRoomsSuccess();

        void addMeetingRoomsFailed(String strMsg);

        void lookMeetingRoomsSuccess(MeetingRecordInfo info);

        void lookMeetingRoomsFailed(String strMsg);

        void deleteMeetingRoomsSuccess();

        void deleteMeetingRoomsFailed(String strMsg);

        void modifyMeetingRoomsSuccess();

        void modifyMeetingRoomsFailed(String strMsg);
    }

    interface Presenter extends BasePresenter<MeetingInfoFillOutActivityContract.View> {

        void addMeetingRooms(HttpParams httpParams);

        void lookMeetingRooms(int id);

        void deleteMeetingRooms(int id);

        void modifyMeetingRooms(int id, HttpParams httpParams);
    }
}
