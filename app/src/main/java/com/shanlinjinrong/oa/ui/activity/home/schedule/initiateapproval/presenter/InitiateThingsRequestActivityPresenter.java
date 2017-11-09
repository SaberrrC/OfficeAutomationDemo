package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.presenter;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.CommonTypeBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.QueryMonoBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SingReasonBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.contract.InitiateThingsRequestActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.net.SocketTimeoutException;

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
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.getQueryMonoCodeSuccess(queryMonoBean);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(0);
                            break;
                        default:
                            mView.getQueryMonoCodeFailure(Integer.parseInt(queryMonoBean.getCode()), queryMonoBean.getMessage());
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
                    mView.getQueryMonoCodeFailure(errorNo, strMsg);
                    mView.requestFinish();
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
                    CommonTypeBean commonTypeBean = new Gson().fromJson(t, CommonTypeBean.class);
                    if (commonTypeBean != null) {
                        switch (commonTypeBean.getCode()) {
                            case ApiJava.REQUEST_CODE_OK:
                                mView.queryEvectionTypeSuccess(commonTypeBean);
                                break;
                            case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                            case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            case ApiJava.ERROR_TOKEN:
                                mView.uidNull(0);
                                break;
                            default:
                                mView.queryEvectionTypeFailure(Integer.parseInt(commonTypeBean.getCode()), commonTypeBean.getMessage());
                                break;
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
                    mView.requestFinish();
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
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(0);
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
                    mView.requestFinish();
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
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(0);
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
                    mView.requestFinish();
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
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(0);
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
                    mView.requestFinish();
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
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(0);
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
                    mView.requestFinish();
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

    @Override
    public void findSignReason() {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(ApiJava.SIGNREASON, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    SingReasonBean singReasonBean = new Gson().fromJson(t, SingReasonBean.class);
                    switch (singReasonBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.findSignReasonSuccess(singReasonBean);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(0);
                            break;
                        default:
                            mView.findSignReasonFailure(Integer.parseInt(singReasonBean.getCode()), singReasonBean.getMessage());
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
                    mView.findSignReasonFailure(errorNo, strMsg);
                    mView.requestFinish();
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

    @Override
    public void submitRegistrationCard(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.jsonPost(ApiJava.SAVESIGN, httpParams, new HttpCallBack() {

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
                            mView.registrationCardSuccess(queryMonoBean.getData());
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            mView.uidNull(0);
                            break;
                        default:
                            mView.registrationCardFailure(Integer.parseInt(queryMonoBean.getCode()), queryMonoBean.getMessage());
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
                    mView.registrationCardFailure(errorNo, strMsg);
                    mView.requestFinish();
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
