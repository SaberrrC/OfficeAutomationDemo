package com.shanlinjinrong.oa.ui.activity.home.workreport.presenter;

import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.CheckReportItem;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.WorkReportCheckContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/21.
 * 发起日报 Presenter
 */
public class WorkReportCheckPresenter extends HttpPresenter<WorkReportCheckContract.View> implements WorkReportCheckContract.Presenter {


    @Inject
    public WorkReportCheckPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void loadData(int reportType, int pageSize, int pageNum, int timeType, int reportStatus, final boolean isLoadMore) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", reportType);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("pageNum", pageNum);
            jsonObject.put("time", timeType);
            jsonObject.put("mobileRatingStatus", reportStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpParams params = new HttpParams();
        params.putJsonParams(jsonObject.toString());
        mKjHttp.cleanCache();
        String url = reportType == 1 ? ApiJava.LEADER_READ_DAILY_REPORT : ApiJava.LEADER_READ_WEEK_REPORT;
        mKjHttp.jsonPost(url, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    List<CheckReportItem> items = new ArrayList<>();
                    String code = jo.getString("code");
                    String message = jo.getString("message");
                    switch (code) {
                        case ApiJava.REQUEST_CODE_OK:
                            JSONObject data = jo.getJSONObject("data");
                            int pageNum = data.getInt("pageNum");
                            int pageSize = data.getInt("pageSize");
                            boolean hasNextPage = data.getBoolean("hasNext");
                            JSONArray array = data.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject item = new JSONObject(array.get(i).toString());
                                //{"dailyid":74,"name":"张俊玉","releaseDate":"2017-09-21 14:37:55","reportingDate":"2017-09-18","speedOfProgress":1,"type":1}
                                items.add(new CheckReportItem(item.getInt("dailyid"),
                                        item.getInt("type"),
                                        item.getString("name"),
                                        item.getString("reportingDate"),
                                        item.getString("releaseDate"),
                                        item.getInt("speedOfProgress")));
                            }
                            if (mView != null) {
                                mView.loadDataSuccess(items, pageNum, hasNextPage, isLoadMore);
                            }
                            break;

                        case ApiJava.REQUEST_NO_RESULT:
                            if (mView != null) {
                                mView.loadDataEmpty();
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
//                                mView.uidNull(0);
                            }
                            break;
                        default:
                            if (mView != null) {
                                mView.loadDataFailed(0, message);
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
                if (mView != null) {
                    mView.loadDataFailed(errorNo, strMsg);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mView != null) {
                    mView.loadDataFinish();
                }
            }
        });
    }

    @Override
    public void rejectReport(int reportType, int dailyId, final int position) {
        String url = reportType == 1 ? ApiJava.REJECT_DAILY_REPORT : ApiJava.REJECT_WEEK_REPORT;
        url += dailyId;
        mKjHttp.cleanCache();
        mKjHttp.jsonGet(url, new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    String code = jo.getString("code");
                    String message = jo.getString("message");
                    switch (code) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null) {
                                mView.rejectReportSuccess(position);
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
//                                mView.uidNull(0);
                            }
                            break;
                        default:
                            if (mView != null) {
                                mView.rejectReportFailed(0, message);
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
                if (mView != null) {
                    mView.rejectReportFailed(errorNo, strMsg);
                }
            }
        });
    }
}
