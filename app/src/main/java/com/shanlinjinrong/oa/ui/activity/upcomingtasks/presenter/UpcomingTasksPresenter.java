package com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.AgreeDisagreeResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.ApporveBodyItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingSearchResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class UpcomingTasksPresenter extends HttpPresenter<UpcomingTasksContract.View> implements UpcomingTasksContract.Presenter {

    @Inject
    public UpcomingTasksPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void getApproveData(String approveState, String billType, String pageNum, String pageSize, String time) {
        HttpParams httpParams = new HttpParams();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiJava.MYAPPLY_QUERY_APPROVE);
        if (!TextUtils.isEmpty(pageNum)) {
            stringBuilder.append("?" + "pageNum=" + pageNum);
        }
        if (!TextUtils.isEmpty(pageSize)) {
            stringBuilder.append("&" + "pageSize=" + pageSize);
        }
        if (!TextUtils.isEmpty(approveState)) {
            stringBuilder.append("&" + "approveState=" + approveState);
        }
        if (!TextUtils.isEmpty(billType)) {
            stringBuilder.append("&" + "billType=" + billType);
        }
        if (!TextUtils.isEmpty(time)) {
            stringBuilder.append("&" + "time=" + time);
        }
        mKjHttp.jsonGet(stringBuilder.toString(), httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    UpcomingTaskItemBean bean = new Gson().fromJson(t, UpcomingTaskItemBean.class);
                    if (TextUtils.equals(bean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                        mView.onGetApproveDataSuccess(bean);
                        return;
                    }
                    mView.onGetApproveDataFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.onGetApproveDataFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getSelectData(String privateCode, String noCheck, String pageNum, String pageSize, String time, String billType, String userName) {
        HttpParams httpParams = new HttpParams();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiJava.SEARCH_APPLICATION);
        if (!TextUtils.isEmpty(pageNum)) {
            stringBuilder.append("?" + "pageNum=" + pageNum);
        }
        if (!TextUtils.isEmpty(pageSize)) {
            stringBuilder.append("&" + "pageSize=" + pageSize);
        }
        if (!TextUtils.isEmpty(privateCode)) {
            stringBuilder.append("&" + "checkmanId=" + privateCode);
        }
        if (!TextUtils.isEmpty(noCheck)) {
            stringBuilder.append("&" + "isCheck=" + noCheck);
        }
        if (!TextUtils.isEmpty(billType)) {
            stringBuilder.append("&" + "pkBillType=" + billType);
        }
        if (!TextUtils.isEmpty(time)) {
            stringBuilder.append("&" + "time=" + time);
        }
        if (!TextUtils.isEmpty(userName)) {
            stringBuilder.append("&" + "userName=" + userName);
        }
        mKjHttp.jsonGet(stringBuilder.toString(), httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    UpcomingSearchResultBean bean = new Gson().fromJson(t, UpcomingSearchResultBean.class);
                    if (TextUtils.equals(bean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                        mView.onSearchSuccess(bean);
                        return;
                    }
                    mView.onGetApproveDataFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.onGetApproveDataFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void postAgreeDisagree(List<ApporveBodyItemBean> list) {
        String json = new Gson().toJson(list);
        HttpParams httpParams = new HttpParams();
        httpParams.putJsonParams(json);
        mKjHttp.jsonPost(ApiJava.ARGEE_DISAGREE_APPROVE, httpParams, new HttpCallBack() {
            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.onApproveFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
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
}