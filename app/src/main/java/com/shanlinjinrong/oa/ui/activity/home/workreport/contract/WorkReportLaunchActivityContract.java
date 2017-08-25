package com.shanlinjinrong.oa.ui.activity.home.workreport.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

/**
 * Created by 丁 on 2017/8/21.
 * 发起日报页面接口
 */
public interface WorkReportLaunchActivityContract {
    interface View extends BaseView {
        void reportSuccess(String msg);

        void reportFailed(String errMsg);

        void requestFinish();

        void getDefaultReceiverSuccess(String id, String name, String post);

        void getDefaultReceiverFailed(String errMsg);

        void getDefaultReceiverEmpty(String msg);
    }

    interface Presenter extends BasePresenter<View> {
        void launchWorkReport(HttpParams params);

        void getDefaultReceiver();//获取默认接收人
    }
}
