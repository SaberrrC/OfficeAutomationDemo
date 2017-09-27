package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.contract;

import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.WorkReportLaunchActivityContract;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

/**
 * Created by tonny on 2017/9/21.
 */

public interface WriteWeeklyNewspaperActivityContract {
    interface View extends BaseView {

        void showLoading();

        void requestFinish();

        void getDefaultReceiverSuccess(String id, String name, String post);

        void getDefaultReceiverFailed(String errMsg);

        void getDefaultReceiverEmpty(String msg);

        void sendWeeklyReportSuccess(String msg);

        void sendWeeklyReportFailure(String code,String msg);

        void sendWeeklyReportFinish();
    }

    interface Presenter extends BasePresenter<View> {

        void addWeekReport(HttpParams httpParams);

        //获取默认接收人
        void getDefaultReceiver();

        void getLastWeek(); //获取上周工作计划
    }
}
