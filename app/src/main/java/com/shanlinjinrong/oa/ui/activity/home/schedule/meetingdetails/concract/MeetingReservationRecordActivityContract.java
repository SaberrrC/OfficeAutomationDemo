package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by tonny on 2017/10/11.
 */

public interface MeetingReservationRecordActivityContract {

    interface View extends BaseView {




    }

    interface Presenter extends BasePresenter<MeetingReservationRecordActivityContract.View> {

        void getMeetingRecord(); //获取会议室信息

    }
}
