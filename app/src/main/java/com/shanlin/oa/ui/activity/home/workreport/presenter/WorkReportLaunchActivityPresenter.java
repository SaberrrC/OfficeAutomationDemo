package com.shanlin.oa.ui.activity.home.workreport.presenter;

import com.shanlin.oa.net.MyKjHttp;
import com.shanlin.oa.ui.activity.home.workreport.contract.WorkReportLaunchActivityContract;
import com.shanlin.oa.ui.base.HttpPresenter;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/21.
 */

public class WorkReportLaunchActivityPresenter extends HttpPresenter<WorkReportLaunchActivityContract.View> implements WorkReportLaunchActivityContract.Presenter {

    @Inject
    public WorkReportLaunchActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void launchWorkReport() {

    }
}
