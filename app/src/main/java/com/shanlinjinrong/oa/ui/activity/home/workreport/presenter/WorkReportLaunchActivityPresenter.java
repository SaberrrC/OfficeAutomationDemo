package com.shanlinjinrong.oa.ui.activity.home.workreport.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.WorkReportLaunchActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONArray;
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
        mKjHttp.jsonPost("dailyreport", params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (jo.getString("code")) {
                        case "000000":
                            mView.reportSuccess(jo.getString("message"));
                            break;
                        default:
                            mView.reportFailed(0, jo.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.reportFailed(errorNo, strMsg);
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
        mKjHttp.jsonGet("user/getCurrentLeader", new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONArray data = jo.getJSONArray("data");
                            mView.getDefaultReceiverSuccess(data.getString(0), data.getString(1), data.getString(2));
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mView.getDefaultReceiverEmpty(Api.getInfo(jo));
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(Api.getCode(jo));
                            break;
                        default:
                            mView.getDefaultReceiverFailed(Api.getCode(jo), jo.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.getDefaultReceiverFailed(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });
    }
}
