package com.shanlinjinrong.oa.ui.activity.home.schedule.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/9/4.
 */

public interface CreateMeetingContract {

    interface View extends BaseView {
        void sendDataSuccess();

        void sendDataFailed(int errCode, String msg);

        void sendDataFinish();
    }

    interface Presenter extends BasePresenter<View> {

        void sendData(String beginTime, String endTime, String date, String theme, String attentees, String type, String roomId);
    }
}
