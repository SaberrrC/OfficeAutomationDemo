package com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.AgreeDisagreeResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.ApporveBodyItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.DeleteBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.TackBackResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksInfoContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.List;

import javax.inject.Inject;

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
                mView.onGetApproveInfoFailure(String.valueOf(errorNo), strMsg);
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
                    mView.onGetApproveInfoFailure(String.valueOf(errorNo), strMsg);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    TackBackResultBean tackBackResultBean = new Gson().fromJson(t, TackBackResultBean.class);
                    if (TextUtils.equals(tackBackResultBean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                        mView.onTackBackSuccess(tackBackResultBean);
                        return;
                    }
                    mView.onGetApproveInfoFailure(tackBackResultBean.getCode(), tackBackResultBean.getMessage());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postApproval(List<ApporveBodyItemBean> list) {
        String json = new Gson().toJson(list);
        HttpParams httpParams = new HttpParams();
        httpParams.putJsonParams(json);
        mKjHttp.jsonPost(ApiJava.ARGEE_DISAGREE_APPROVE, httpParams, new HttpCallBack() {
            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.onApproveFailure(errorNo, strMsg);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                AgreeDisagreeResultBean resultBean = new Gson().fromJson(t, AgreeDisagreeResultBean.class);
                if (TextUtils.equals(resultBean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                    mView.onApproveSuccess(resultBean);
                    return;
                }
                mView.onApproveFailure(Integer.parseInt(resultBean.getCode()), resultBean.getMessage());
            }
        });
    }

    @Override
    public void getDelete(String billCode, String billType) {
        HttpParams httpParams = new HttpParams();
        try {
            mKjHttp.jsonGet(ApiJava.DELETE_APPROVEL +"?billCode="+billCode+"&billType="+billType, httpParams, new HttpCallBack() {
                @Override
                public void onFailure(int errorNo, String strMsg) {
                    super.onFailure(errorNo, strMsg);
                    mView.ondELETEFailure(String.valueOf(errorNo), strMsg);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    DeleteBean bean = new Gson().fromJson(t, DeleteBean.class);
                    if (TextUtils.equals(bean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                        mView.onDeleteSuccess(bean);
                        return;
                    }
                    mView.ondELETEFailure(bean.getCode(), bean.getMessage());
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}