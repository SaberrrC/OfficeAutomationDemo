package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 新会议室记录详情
 */

public interface MeetingReservationRecordActivityContract {

    interface View extends BaseView {

        void getMeetingRecordSuccess(List<ReservationRecordBean.DataBean> bean);

        void getMeetingRecordFailed(String msgStr);

        void removeFooterView();

        void getMeetingRecordEmpty();
    }

    interface Presenter extends BasePresenter<MeetingReservationRecordActivityContract.View> {

        void getMeetingRecord(HttpParams httpParams, int page, int num, boolean isLoadMore,List<ReservationRecordBean.DataBean> data); //获取会议室信息
    }
}
