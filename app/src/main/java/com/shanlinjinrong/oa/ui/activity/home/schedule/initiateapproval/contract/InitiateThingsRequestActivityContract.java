package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.contract;


import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.BusinessTypeBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.QueryMonoBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

public interface InitiateThingsRequestActivityContract {
    interface View extends BaseView {

        void showLoading();

        void requestFinish();

        void requestNetworkError();

        void getQueryMonoCodeSuccess(QueryMonoBean bean);

        void getQueryMonoCodeFailure(int errorCode, String str);

        void initiateThingsRequestSuccess();

        void initiateThingsRequestFailure(int errorCode, String str);

        void queryEvectionTypeSuccess(BusinessTypeBean bean);

        void queryEvectionTypeFailure(int errorCode, String str);

        void queryDurationSuccess(QueryMonoBean bean);

        void queryDurationFailure(int errorCode, String str);

        void submitEvectionApplySuccess(String bean);

        void submitEvectionApplyFailure(int errorCode, String str);

        void addWorkApplySuccess(String bean);

        void addWorkApplyFailure(int errorCode, String str);

        void submitFurloughSuccess(String bean);

        void submitFurloughFailure(int errorCode, String str);

    }

    interface Presenter extends BasePresenter<InitiateThingsRequestActivityContract.View> {

        //获取申请编码
        void getQueryMonoCode(int type);

        //发起类别
        void queryEvectionType(int type);

        //申请时长
        void queryDuration(String beginTime, String endTime, int type, String billCode);

        //申请出差
        void submitEvectionApply(HttpParams httpParams);

        //加班申请
        void addWorkApply(HttpParams httpParams);

        //休假申请
        void submitFurlough(HttpParams httpParams);
    }
}
