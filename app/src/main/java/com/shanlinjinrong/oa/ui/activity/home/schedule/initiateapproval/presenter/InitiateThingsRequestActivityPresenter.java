package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.presenter;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.QueryMonoBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.contract.InitiateThingsRequestActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 出差想申请
 */
public class InitiateThingsRequestActivityPresenter extends HttpPresenter<InitiateThingsRequestActivityContract.View> implements InitiateThingsRequestActivityContract.Presenter {

    @Inject
    public InitiateThingsRequestActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    //申请编码
    @Override
    public void getQueryMonoCode(int type) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(ApiJava.GET_MONOCODE + "?billType=" + type, httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    QueryMonoBean queryMonoBean = new Gson().fromJson(t, QueryMonoBean.class);
                    if (queryMonoBean.equals(ApiJava.REQUEST_CODE_OK)) {
                        mView.getQueryMonoCodeSuccess(queryMonoBean);
                    } else {
                        mView.getQueryMonoCodeFailure(Integer.parseInt(queryMonoBean.getCode()), queryMonoBean.getMessage());
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.getQueryMonoCodeFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initiateThingsRequest(String date, int status, int type) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(Api.INITIARE_REQUEST + "?date=" + date + "&status=" + status + "&type=" + type, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    //申请类型
    @Override
    public void queryEvectionType(int type) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(ApiJava.EVENCTIONTYPE + "?itemtype=" + type, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }
}
