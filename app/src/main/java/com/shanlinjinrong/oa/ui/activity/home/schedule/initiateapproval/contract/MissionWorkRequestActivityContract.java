package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.contract;


import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

public interface MissionWorkRequestActivityContract {
    interface View extends BaseView {
        void getQueryMonoCodeSuccess();

        void getQueryMonoCodeFailure(int errorCode, String str);
    }

    interface Presenter extends BasePresenter<MissionWorkRequestActivityContract.View> {
        void getQueryMonoCode(int type);
    }
}
