package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.presenter;


import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.LastWeekPlanBean;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WeekReportItemBean;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.contract.WriteWeeklyNewspaperActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

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
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null) {
                                mView.sendWeeklyReportSuccess(jsonObject.getString("message"));
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
                                mView.uidNull(jsonObject.getString("code"));
                            }
                            break;
                        default:
                            if (mView != null) {
                                mView.sendWeeklyReportFailure(jsonObject.getString("code"), jsonObject.getString("message"));
                            }
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    if (mView != null) {
                        mView.sendWeeklyReportFinish();
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.sendWeeklyReportFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.sendWeeklyReportFailure("" + errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
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
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    switch (jsonObject.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null) {
                                mView.updateWeekReportSuccess();
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
                                mView.uidNull(jsonObject.getString("code"));
                            }
                            break;
                        default:
                            if (mView != null) {
                                mView.updateWeekReportFailed(jsonObject.getString("code"), jsonObject.getString("message"));
                            }
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    if (mView != null) {
                        mView.requestFinish();
                    }
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

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.updateWeekReportFailed("" + errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getDefaultReceiver() {

        mKjHttp.get(ApiJava.GET_CURRENT_LEADER, new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (jo.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            JSONObject data = jo.getJSONObject("data");
                            if (mView != null) {
                                mView.getDefaultReceiverSuccess(data.getString("id"), data.getString("username"), data.getString("post"));
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
                                mView.uidNull(jo.getString("code"));
                            }
                            break;
                        default:
                            if (mView != null) {
                                mView.getDefaultReceiverFailed(jo.getString("message"));
                            }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.getDefaultReceiverFailed(strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void getLastWeek() {
        HttpParams httpParams = new HttpParams();
        mKjHttp.get(ApiJava.LOOK_LAST_WEEK, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);

                try {
                    JSONObject jsonObject = new JSONObject(t);
                    LastWeekPlanBean lastWeekPlanBean = new Gson().fromJson(jsonObject.toString(), LastWeekPlanBean.class);
                    switch (lastWeekPlanBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (lastWeekPlanBean.getData() != null) {
                                if (mView != null) {
                                    mView.getLastWeekPlanSuccess(lastWeekPlanBean.getData());
                                }
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
                                mView.uidNull(lastWeekPlanBean.getCode());
                            }
                            break;
                        default:
                            if (mView != null) {
                                mView.getLastWeekPlanFailure(Integer.parseInt(lastWeekPlanBean.getCode()), lastWeekPlanBean.getMessage());
                            }
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.getLastWeekPlanFailure(errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getWeReportData(int reportId) {
        HttpParams httpParams = new HttpParams();
        mKjHttp.get(ApiJava.WEEK_REPORT + "/" + reportId, httpParams, new HttpCallBack() {
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
                            if (mView != null) {
                                mView.getReportSuccess(weekReportItem);
                            }
                            break;

                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
                                mView.uidNull(jo.getString("code"));
                            }
                            break;
                        default:
                            if (mView != null) {
                                mView.getReportFailed(code, message);
                            }
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
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.getReportFailed("" + errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
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
                            if (mView != null) {
                                mView.evaluationReportSuccess();
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
                                mView.uidNull(jo.getString("code"));
                            }
                            break;

                        default:
                            if (mView != null) {
                                mView.evaluationReportFailed(code, message);
                            }
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
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.evaluationReportFailed("" + errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
