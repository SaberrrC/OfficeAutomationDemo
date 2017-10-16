package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingBookItem;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by tonny on 2017/10/11.
 */

public interface MeetingPredetermineContract {

    interface View extends BaseView {

        void getMeetingPredetermineSuccess(List<MeetingBookItem.DataBean> dataBeen);

        void getMeetingPredetermineFailed(String msgStr);
    }

    interface Presenter extends BasePresenter<MeetingPredetermineContract.View> {

        void getMeetingPredetermine(int meetingId); //获取会议室被预订过的时间段
    }
}
