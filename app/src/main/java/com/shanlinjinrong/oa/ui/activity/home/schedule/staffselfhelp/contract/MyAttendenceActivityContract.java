package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract;

import com.shanlinjinrong.oa.ui.activity.home.schedule.contract.CreateMeetingContract;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public interface MyAttendenceActivityContract {
    interface View extends BaseView {
        void sendDataSuccess();

        void sendDataFailed(int errCode, String msg);

        void sendDataFinish();
    }

    interface Presenter extends BasePresenter<MyAttendenceActivityContract.View> {


        void sendData(String beginTime, String endTime, String date, String theme, String attentees, String type, String roomId);
    }
}
