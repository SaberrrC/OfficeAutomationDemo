package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by tonny on 2017/10/11.
 */

public interface MeetingDetailsActivityContract {

    interface View extends BaseView {

        void getMeetingRoomsEmpty();

        void getMeetingRoomsSuccess(List<MeetingRoomsBean.DataBean> data);

        void getMeetingRoomsFailed(int errorCode,String data);
    }

    interface Presenter extends BasePresenter<MeetingDetailsActivityContract.View> {

        void getMeetingRooms(); //获取会议室信息
    }
}
