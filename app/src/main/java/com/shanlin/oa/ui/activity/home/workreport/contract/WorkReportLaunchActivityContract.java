package com.shanlin.oa.ui.activity.home.workreport.contract;

import com.shanlin.oa.ui.base.BasePresenter;
import com.shanlin.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

/**
 * Created by 丁 on 2017/8/21.
 * 发起日报页面接口
 */
public interface WorkReportLaunchActivityContract {
    interface View extends BaseView {
        void reportSuccess();

        void reportFailed(int errCode, String errMsg);

        void requestFinish();

        void getDefaultReceiverSuccess(String id, String name, String post);

        void getDefaultReceiverFailed(int errCode, String errMsg);

        void getDefaultReceiverEmpty(String msg);
    }

    interface Presenter extends BasePresenter<View> {
        void launchWorkReport(HttpParams params);

        void getDefaultReceiver();//获取默认接收人
    }
}
