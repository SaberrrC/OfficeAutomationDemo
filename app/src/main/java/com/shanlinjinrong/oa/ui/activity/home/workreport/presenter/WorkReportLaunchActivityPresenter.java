package com.shanlinjinrong.oa.ui.activity.home.workreport.presenter;

import android.util.Log;

import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.WorkReportLaunchActivityContract;
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
public class WorkReportLaunchActivityPresenter extends HttpPresenter<WorkReportLaunchActivityContract.View> implements WorkReportLaunchActivityContract.Presenter {

    @Inject
    public WorkReportLaunchActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }



    @Override
    public void launchWorkReport(HttpParams params) {
        mKjHttp.cleanCache();//清除缓存，否则换个日期请求的话，response来自缓存，会一直提示该天已填写日报
        mKjHttp.jsonPost(ApiJava.DAILY_REPORT, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Log.i("WorkReport", "WorkReport result: " + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (jo.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.reportSuccess(jo.getString("message"));
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            mView.uidNull(0);
                            break;
                        default:
                            mView.reportFailed(jo.getString("code"), jo.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.reportFailed("" + errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });
    }

    @Override
    public void getDefaultReceiver() {
        mKjHttp.jsonGet(ApiJava.GET_CURRENT_LEADER, new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (jo.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            JSONObject data = jo.getJSONObject("data");
                            mView.getDefaultReceiverSuccess(data.getString("id"), data.getString("username"), data.getString("post"));
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            mView.uidNull(0);
                            break;
                        default:
                            mView.getDefaultReceiverFailed(jo.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.getDefaultReceiverFailed(strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });
    }
}
