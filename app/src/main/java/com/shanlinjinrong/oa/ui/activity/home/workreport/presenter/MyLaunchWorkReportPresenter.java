package com.shanlinjinrong.oa.ui.activity.home.workreport.presenter;

import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.MyLaunchReportItem;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.MyLaunchWorkReportContract;
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
public class MyLaunchWorkReportPresenter extends HttpPresenter<MyLaunchWorkReportContract.View> implements MyLaunchWorkReportContract.Presenter {


    @Inject
    public MyLaunchWorkReportPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void loadDailyReport(int pageSize, int pageNum, int timeType, final boolean isLoadMore) {
        HttpParams params = new HttpParams();
        String url = ApiJava.DAILY_REPORT + "?pageSize=" + pageSize + "&pageNum=" + pageNum + "&time=" + timeType;
        mKjHttp.cleanCache();
        mKjHttp.get(url, params, new HttpCallBack() {

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    List<MyLaunchReportItem> items = new ArrayList<>();
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
                                items.add(new MyLaunchReportItem(item.getInt("id"), item.getString("userName"), item.getInt("type"), item.getString("reportingDate"), item.getString("releaseDate"), item.getInt("speedOfProgress")));
                            }
                            if (mView != null) {
                                mView.loadDataSuccess(items, pageNum, pageSize, hasNextPage, isLoadMore);
                            }
                            break;

                        case ApiJava.REQUEST_NO_RESULT:
                            if (mView != null)
                                mView.loadDataEmpty();
                            break;
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
//                                mView.uidNull(0);
                            break;
                        default:
                            if (mView != null)
                                mView.loadDataFailed(code, message);
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
                    if (mView != null)
                        mView.loadDataFailed("" + errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null)
                        mView.loadDataFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadWeekReportList(int pageSize, int pageNum, int timeType, final boolean isLoadMore) {
        HttpParams params = new HttpParams();
        String url = ApiJava.WEEK_REPORT_LIST + "?pageSize=" + pageSize + "&pageNum=" + pageNum + "&time=" + timeType;
        mKjHttp.cleanCache();
        mKjHttp.get(url, params, new HttpCallBack() {

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    List<MyLaunchReportItem> items = new ArrayList<>();
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
                                String reportDate = item.getString("reportDate");
                                if (reportDate.contains("--")) {
                                    reportDate = reportDate.replaceAll("--", "至");
                                }
                                items.add(new MyLaunchReportItem(item.getInt("id"), "", item.getInt("reportType"), reportDate, item.getString("createdAt"), item.getInt("ratingStatus")));
                            }
                            if (mView != null)
                                mView.loadDataSuccess(items, pageNum, pageSize, hasNextPage, isLoadMore);
                            break;

                        case ApiJava.REQUEST_NO_RESULT:
                            if (mView != null)
                                mView.loadDataEmpty();
                            break;
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.ERROR_TOKEN:
//                            if (mView != null)
//                                mView.uidNull(0);
                            break;
                        default:
                            if (mView != null)
                                mView.loadDataFailed(code, message);
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
                    if (mView != null)
                        mView.loadDataFailed("" + errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null)
                        mView.loadDataFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
