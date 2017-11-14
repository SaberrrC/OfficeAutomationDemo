package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract;

import android.content.Context;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;


public interface AttandenceRecorderContract {
    interface View extends BaseView {

        void showLoading();

        void sendDataSuccess(MyAttandanceResponse myAttandanceResponse);

        void sendDataFailed(int errCode, String msg);

        void sendDataFinish();
    }

    interface Presenter extends BasePresenter<AttandenceRecorderContract.View> {

        void sendData(String userId, String year, String minth, Context context);
    }
}
