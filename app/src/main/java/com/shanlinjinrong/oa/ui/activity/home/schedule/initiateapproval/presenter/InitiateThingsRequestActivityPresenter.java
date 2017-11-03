package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.presenter;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.BusinessTypeBean;
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
                    if (queryMonoBean.getCode().equals(ApiJava.REQUEST_CODE_OK)) {
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

    //申请类型
    @Override
    public void queryEvectionType(int type) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(ApiJava.EVENCTIONTYPE + "?itemtype=" + type, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    BusinessTypeBean businessTypeBean = new Gson().fromJson(t, BusinessTypeBean.class);
                    if (businessTypeBean != null) {
                        if (businessTypeBean.getCode().equals(ApiJava.REQUEST_CODE_OK)) {
                            mView.queryEvectionTypeSuccess(businessTypeBean);
                        } else {
                            mView.queryEvectionTypeFailure(Integer.parseInt(businessTypeBean.getCode()), businessTypeBean.getMessage());
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.queryEvectionTypeFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });

    }

    //申请时长
    @Override
    public void queryDuration(String beginTime, String endTime, int type, String billCode) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(ApiJava.QUERYDURATION + "?endTime=" + endTime + ":00" + "&startTime=" + beginTime + ":00" + "&type=" + type + "&billCode=" + billCode, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    QueryMonoBean queryMonoBean = new Gson().fromJson(t, QueryMonoBean.class);
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.queryDurationSuccess(queryMonoBean);
                            break;
                        default:
                            mView.queryEvectionTypeFailure(Integer.parseInt(queryMonoBean.getCode()), queryMonoBean.getMessage());
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.queryEvectionTypeFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    //申请出差
    @Override
    public void submitEvectionApply(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.jsonPost(ApiJava.EVECTIONAPPLY, httpParams, new HttpCallBack() {

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
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.submitEvectionApplySuccess(queryMonoBean.getData());
                            break;
                        default:
                            mView.submitEvectionApplyFailure(Integer.parseInt(queryMonoBean.getCode()), queryMonoBean.getMessage());
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.submitEvectionApplyFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });
    }

    //申请加班
    @Override
    public void addWorkApply(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.jsonPost(ApiJava.ADDWEORKAPPLY, httpParams, new HttpCallBack() {

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
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.addWorkApplySuccess(queryMonoBean.getData());
                            break;

                        default:
                            mView.addWorkApplyFailure(Integer.parseInt(queryMonoBean.getCode()), queryMonoBean.getMessage());
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.addWorkApplyFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });
    }

    //申请休假
    @Override
    public void submitFurlough(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.jsonPost(ApiJava.SUBMITFURLOUGH, httpParams, new HttpCallBack() {

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
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.submitFurloughSuccess(queryMonoBean.getData());
                            break;

                        default:
                            mView.submitFurloughFailure(Integer.parseInt(queryMonoBean.getCode()), queryMonoBean.getMessage());
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.submitFurloughFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });
    }
}
