package com.shanlinjinrong.oa.ui.activity.home.workreport.contract;

import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.WorkReportBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.kymjs.kjframe.http.HttpParams;

/**
 * Created by 丁 on 2017/8/21.
 * 审核日报页面接口
 */
public interface CheckDailyReportContract {
    interface View extends BaseView {

        void loadDataSuccess(WorkReportBean report);

        void loadDataFailed(String errCode, String errMsg);

        void loadDataFinish();

        void commitSuccess();

        void commitFailed(String errCode, String errMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void loadDailyData(int dailyid);

        void commitDailyEvaluation(HttpParams params);
    }
}
