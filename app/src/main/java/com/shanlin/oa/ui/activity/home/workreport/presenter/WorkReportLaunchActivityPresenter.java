package com.shanlin.oa.ui.activity.home.workreport.presenter;

import com.shanlin.oa.net.MyKjHttp;
import com.shanlin.oa.ui.activity.home.workreport.contract.WorkReportLaunchActivityContract;
import com.shanlin.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/21.
 * 发起日报 Presenter
 */
public class WorkReportLaunchActivityPresenter extends HttpPresenter<WorkReportLaunchActivityContract.View> implements WorkReportLaunchActivityContract.Presenter {

    @Inject
    public WorkReportLaunchActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void launchWorkReport(HttpParams params) {

        mKjHttp.post("dailyreport", params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                mView.reportSuccess();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.reportFailed(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.reportFinish();
            }
        });
    }
}
