package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.contract;


import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

public interface InitiateThingsRequestActivityContract {
    interface View extends BaseView {
        void getQueryMonoCodeSuccess();

        void getQueryMonoCodeFailure(int errorCode, String str);

        void initiateThingsRequestSuccess();

        void initiateThingsRequestFailure(int errorCode, String str);

    }

    interface Presenter extends BasePresenter<InitiateThingsRequestActivityContract.View> {

        //获取申请编码
        void getQueryMonoCode(int type);

        //发起申请
        void initiateThingsRequest(String date, int status, int type);
    }
}
