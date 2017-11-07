package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract;

import android.content.Context;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.model.responsebody.MyAttendanceResponse;
import com.shanlinjinrong.oa.ui.activity.home.schedule.contract.CreateMeetingContract;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public interface MyAttendenceActivityContract {
    interface View extends BaseView {
        void sendDataSuccess(MyAttandanceResponse myAttandanceResponse);

        void sendDataFailed(String errCode, String msg);

        void sendDataFinish();
    }

    interface Presenter extends BasePresenter<MyAttendenceActivityContract.View> {


        void sendData(String userId, String year, String minth, Context context);
    }
}
