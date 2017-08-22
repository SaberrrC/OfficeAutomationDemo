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

        void reportFinish();
    }

    interface Presenter extends BasePresenter<View> {
        void launchWorkReport(HttpParams params);
    }
}
