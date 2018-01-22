package com.shanlinjinrong.oa.ui.activity.home.workreport.presenter;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.WorkReportBean;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.WorkReportUpdateContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/21.
 * 发起日报 Presenter
 */
public class WorkReportUpdatePresenter extends HttpPresenter<WorkReportUpdateContract.View> implements WorkReportUpdateContract.Presenter {

    @Inject
    public WorkReportUpdatePresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void getReportData(int dailyid) {
        String url = ApiJava.DAILY_REPORT + "/" + dailyid;
        mKjHttp.get(url, new HttpParams(), new HttpCallBack() {
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
                            mView.getReportSuccess(workReport);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            mView.uidNull(0);
                            break;
                        default:
                            mView.getReportFailed("", "");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.uidNull(strMsg);
                    mView.getReportFailed("", "");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    mView.requestFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void updateReport(HttpParams params) {
        mKjHttp.cleanCache();//清除缓存，否则换个日期请求的话，response来自缓存，会一直提示该天已填写日报
        mKjHttp.jsonPut(ApiJava.UPDATE_DAILY_REPORT, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (jo.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.updateReportSuccess();
                            break;
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.ERROR_TOKEN:
//                            mView.uidNull(0);
                            break;
                        default:
                            mView.updateReportFailed(jo.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.uidNull(strMsg);
                    mView.updateReportFailed(strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    mView.requestFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
