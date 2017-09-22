package com.shanlinjinrong.oa.ui.activity.home.workreport.presenter;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.WorkReportBean;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.CheckDailyReportContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/21.
 * 发起日报 Presenter
 */
public class CheckDailyReportPresenter extends HttpPresenter<CheckDailyReportContract.View> implements CheckDailyReportContract.Presenter {


    @Inject
    public CheckDailyReportPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void loadDailyData(int dailyid) {
        String url = ApiJava.DAILY_REPORT + "/" + dailyid;
        mKjHttp.jsonGet(url, new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                JSONObject jo;
                try {
                    jo = new JSONObject(t);
                    String code = jo.getString("code");
                    String message = jo.getString("message");
                    switch (code) {
                        case ApiJava.REQUEST_CODE_OK:
                            JSONObject data = jo.getJSONObject("data");
                            WorkReportBean workReport = new Gson().fromJson(data.toString(), WorkReportBean.class);
                            mView.loadDataSuccess(workReport);
                            break;

                        case ApiJava.REQUEST_NO_RESULT:
                            mView.loadDataEmpty();
                            break;
                        default:
                            mView.uidNull(0);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.loadDataFailed(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.loadDataFinish();
            }
        });
    }

    @Override
    public void commitDailyEvaluation() {

    }


}
