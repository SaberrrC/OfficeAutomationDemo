package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WeekReportItemBean;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.contract.WriteWeeklyNewspaperActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by tonny on 2017/9/21.
 */

public class WriteWeeklyNewspaperActivityPresenter extends HttpPresenter<WriteWeeklyNewspaperActivityContract.View> implements WriteWeeklyNewspaperActivityContract.Presenter {

    @Inject
    public WriteWeeklyNewspaperActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void addWeekReport(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.jsonPost(ApiJava.SEND_WEEK_REPORT, httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    Log.d("sendWeeklyReportData", "onSuccess" + t.toString());
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.sendWeeklyReportSuccess(jsonObject.getString("message"));
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            mView.uidNull(0);
                            break;
                        default:
                            mView.sendWeeklyReportFailure(jsonObject.getString("code"), jsonObject.getString("message"));
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    mView.sendWeeklyReportFinish();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.sendWeeklyReportFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.sendWeeklyReportFailure("" + errorNo, strMsg);
            }
        });
    }

    @Override
    public void updateWeekReport(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.jsonPut(ApiJava.UPDATE_WEEK_REPORT, httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.updateWeekReportSuccess();
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            mView.uidNull(0);
                            break;
                        default:
                            mView.updateWeekReportFailed(jsonObject.getString("code"), jsonObject.getString("message"));
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    mView.requestFinish();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.updateWeekReportFailed("" + errorNo, strMsg);
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

    @Override
    public void getLastWeek() {
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(ApiJava.LOOK_LAST_WEEK, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);

                String t1 = t;
                String t11 = t1;
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                int errorNo1 = errorNo;
                String strMsg1 = strMsg;
                String strMsg11 = strMsg1;

            }
        });
    }

    @Override
    public void getWeReportData(int reportId) {
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(ApiJava.WEEK_REPORT + "/" + reportId, httpParams, new HttpCallBack() {
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
                            WeekReportItemBean weekReportItem = new Gson().fromJson(data.toString(), WeekReportItemBean.class);
                            mView.getReportSuccess(weekReportItem);
                            break;

                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            mView.uidNull(0);
                            break;
                        default:
                            mView.getReportFailed(code, message);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.getReportFailed("" + errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });
    }

    @Override
    public void evaluationReport(int reportId, String content) {
        HttpParams httpParams = new HttpParams();
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("id", reportId);
            jsonData.put("checkManRating", content);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpParams.putJsonParams(jsonData.toString());
        mKjHttp.cleanCache();
        mKjHttp.jsonPut(ApiJava.WEEK_REPORT, httpParams, new HttpCallBack() {
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
                            mView.evaluationReportSuccess();
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            mView.uidNull(0);
                            break;

                        default:
                            mView.evaluationReportFailed(code, message);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.evaluationReportFailed("" + errorNo, strMsg);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });

    }
}
