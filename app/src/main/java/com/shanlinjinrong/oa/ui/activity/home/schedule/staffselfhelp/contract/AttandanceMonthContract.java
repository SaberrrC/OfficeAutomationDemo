package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public interface AttandanceMonthContract {
    interface View extends BaseView {
        void sendDataSuccess();

        void sendDataFailed(int errCode, String msg);

        void sendDataFinish();
    }

    interface Presenter extends BasePresenter<AttandanceMonthContract.View> {
        void searchDayRecorder(String data,String id);
        void sendData(String str);
    }
}
