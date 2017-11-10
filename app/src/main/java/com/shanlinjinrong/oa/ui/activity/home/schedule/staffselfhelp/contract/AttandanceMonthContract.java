package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract;

import android.content.Context;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.model.responsebody.MyAttendanceResponse;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.ArrayList;
import java.util.List;

public interface AttandanceMonthContract {
    interface View extends BaseView {
        void sendDataSuccess(MyAttandanceResponse myAttandanceResponse);

        void sendDataFailed(int errCode, String msg);

        void queryDayAttendanceSuccess(List<MyAttendanceResponse> myAttandanceResponse);
    }

    interface Presenter extends BasePresenter<AttandanceMonthContract.View> {

        void sendData(String userId, String year, String minth, Context context);

        void queryDayAttendance(String codeId, String date);
    }
}
