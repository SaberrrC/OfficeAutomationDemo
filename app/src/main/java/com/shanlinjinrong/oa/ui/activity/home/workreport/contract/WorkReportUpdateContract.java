package com.shanlinjinrong.oa.ui.activity.home.workreport.contract;

import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.WorkReportBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

/**
 * Created by 丁 on 2017/8/21.
 * 发起日报页面接口
 */
public interface WorkReportUpdateContract {
    interface View extends BaseView {
        void getReportSuccess(WorkReportBean workReport);

        void getReportFailed(String errCode, String errMsg);

        void requestFinish();

        void updateReportSuccess();

        void updateReportFailed(String errMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void getReportData(int dailyid);//获取已填写的日报信息

        void updateReport(HttpParams params); //更新日报
    }
}
