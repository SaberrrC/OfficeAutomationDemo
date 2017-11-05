package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract;

import android.content.Context;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public interface AttandanceMonthContract {
    interface View extends BaseView {
        void sendDataSuccess(MyAttandanceResponse myAttandanceResponse);

        void sendDataFailed(String errCode, String msg);

        void sendDataFinish();
    }

    interface Presenter extends BasePresenter<AttandanceMonthContract.View> {
        void sendData(String userId,String year,String minth ,Context context);
    }
}
