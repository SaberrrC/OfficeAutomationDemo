package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * 获取会议详情
 *
 */

public interface MeetingDetailsActivityContract {

    interface View extends BaseView {

        void getMeetingRoomsSuccess(List<MeetingRoomsBean.DataBean> data);

        void getMeetingRoomsFailed(String data);
    }

    interface Presenter extends BasePresenter<MeetingDetailsActivityContract.View> {

        void getMeetingRooms(); //获取会议室信息
    }
}
