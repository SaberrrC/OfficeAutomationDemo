package com.shanlin.oa.ui.activity.home.workreport.contract;

import com.shanlin.oa.ui.base.BasePresenter;
import com.shanlin.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/21.
 * 发起日报页面接口
 */
public interface WorkReportLaunchActivityContract {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {
        void launchWorkReport();
    }
}
