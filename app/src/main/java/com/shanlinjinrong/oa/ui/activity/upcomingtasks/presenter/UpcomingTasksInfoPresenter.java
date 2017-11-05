package com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.TackBackResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksInfoContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class UpcomingTasksInfoPresenter extends HttpPresenter<UpcomingTasksInfoContract.View> implements UpcomingTasksInfoContract.Presenter {

    @Inject
    public UpcomingTasksInfoPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void getInfoData(String billType, String billCode) {
        HttpParams httpParams = new HttpParams();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiJava.MYAPPLY_QUERY_APPROVE_INFO);
        if (!TextUtils.isEmpty(billType)) {
            stringBuilder.append("?" + "billType=" + billType);
        }
        if (!TextUtils.isEmpty(billCode)) {
            stringBuilder.append("&" + "billCode=" + billCode);
        }
        mKjHttp.jsonGet(stringBuilder.toString(), httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                mView.onGetApproveInfoSuccess(json);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.onGetApproveInfoFailure(errorNo, strMsg);
            }
        });
    }

    @Override
    public void postTackBack(String billCode, String billType) {
        HttpParams httpParams = new HttpParams();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("monocode", billCode);
            jsonObject.put("type", billType);
            String bodyJson = jsonObject.toString();
            httpParams.putJsonParams(bodyJson);
            mKjHttp.jsonPost(ApiJava.TACK_BACK, httpParams, new HttpCallBack() {
                @Override
                public void onFailure(int errorNo, String strMsg) {
                    super.onFailure(errorNo, strMsg);
                    mView.onGetApproveInfoFailure(errorNo, strMsg);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    TackBackResultBean tackBackResultBean = new Gson().fromJson(t, TackBackResultBean.class);
                    mView.onTackBackSuccess(tackBackResultBean);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
