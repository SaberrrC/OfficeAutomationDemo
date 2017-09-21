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
    public void loadData(int reportType, int pageSize, int pageNum, int timeType, final boolean isLoadMore) {
        HttpParams params = new HttpParams();
        params.put("reportType", reportType);
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum);
        params.put("time", timeType);
        mKjHttp.jsonGet(ApiJava.DAILY_REPORT, params, new HttpCallBack() {

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
                                items.add(new MyLaunchReportItem(item.getInt("type"),
                                        item.getString("reportingDate"),
                                        item.getString("releaseDate"),
                                        item.getInt("speedOfProgress")));
                            }
                            mView.loadDataSuccess(items, pageNum, pageSize, hasNextPage, isLoadMore);
                            break;

                        case ApiJava.REQUEST_NO_RESULT:
                            mView.loadDataEmpty();
                            break;
                        default:
                            mView.uidNull(0);
                            break;
                    }

                } catch (JSONException e) {
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
}
