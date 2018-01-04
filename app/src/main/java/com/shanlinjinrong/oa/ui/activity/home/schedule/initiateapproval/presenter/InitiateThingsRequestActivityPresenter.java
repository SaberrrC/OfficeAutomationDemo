package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.presenter;

//import com.example.retrofit.model.requestbody.AddWorkBody;

import com.example.retrofit.net.HttpMethods;
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

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 出差想申请
 */
public class InitiateThingsRequestActivityPresenter extends HttpPresenter<InitiateThingsRequestActivityContract.View> implements InitiateThingsRequestActivityContract.Presenter {

    private String tips;

    @Inject
    public InitiateThingsRequestActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    //申请编码
    @Override
    public void getQueryMonoCode(int type) {

        HttpMethods.getInstance().getQueryMonoCode(type, new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
                try {
                    if (mView != null)
                        mView.showLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCompleted() {
                try {
                    if (mView != null)
                        mView.requestFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                try {
                    if (mView != null)
                        mView.requestFinish();
                    if (e instanceof HttpException) {
                        if (((HttpException) e).code() > 400) {
                            if (mView != null)
                                mView.getQueryMonoCodeFailure(((HttpException) e).code(), "服务器异常，请稍后重试！");
                        }
//                        if (mView != null)
//                            mView.uidNull(((HttpException) e).code());
                    } else if (e instanceof SocketTimeoutException) {
                        if (mView != null)
                            mView.getQueryMonoCodeFailure(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof NullPointerException) {
                        if (mView != null)
                            mView.getQueryMonoCodeFailure(-1, "网络不通，请检查网络连接！");
                    } else if (e instanceof ConnectException) {
                        if (mView != null)
                            mView.getQueryMonoCodeFailure(-1, "网络不通，请检查网络连接！");
                    }
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onNext(String s) {
                try {
                    if (mView != null)
                        mView.getQueryMonoCodeSuccess(s);
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
        mKjHttp.get(ApiJava.EVENCTIONTYPE + "?itemtype=" + type, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    CommonTypeBean commonTypeBean = new Gson().fromJson(t, CommonTypeBean.class);
                    if (commonTypeBean != null) {
                        switch (commonTypeBean.getCode()) {
                            case ApiJava.REQUEST_CODE_OK:
                                if (mView != null)
                                    mView.queryEvectionTypeSuccess(commonTypeBean);
                                break;
                            case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                            case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            case ApiJava.ERROR_TOKEN:
//                                if (mView != null)
//                                    mView.uidNull(0);
                                break;
                            default:
                                if (mView != null)
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
                    if (mView != null) {
                        mView.queryEvectionTypeFailure(errorNo, strMsg);
                        if (mView != null)
                            mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null)
                        mView.requestFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //申请时长
    @Override
    public void queryDuration(String beginTime, String endTime, int type, String billCode, String applyType) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.get(ApiJava.QUERYDURATION + "?endTime=" + endTime + ":00" + "&startTime=" + beginTime + ":00" + "&type=" + type + "&billCode=" + billCode + "&applyType=" + applyType, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    QueryMonoBean queryMonoBean = new Gson().fromJson(t, QueryMonoBean.class);
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.queryDurationSuccess(queryMonoBean);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            if (mView != null)
//                                mView.uidNull(0);
                            break;
                        default:
                            if (mView != null)
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
                    if (mView != null) {
                        mView.queryEvectionTypeFailure(errorNo, strMsg);
                        if (mView != null)
                            mView.requestFinish();
                    }
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
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    QueryMonoBean queryMonoBean = new Gson().fromJson(t, QueryMonoBean.class);
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.submitEvectionApplySuccess(queryMonoBean.getData());
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            if (mView != null)
//                                mView.uidNull(0);
                            break;
                        default:
                            submitFailureTips(queryMonoBean);
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
                    if (mView != null) {
                        mView.submitEvectionApplyFailure(errorNo, strMsg);
                        if (mView != null)
                            mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mView != null)
                    mView.requestFinish();
            }
        });

        //        HttpMethods.getInstance().submitEvectionApply(body, new Subscriber<String>() {
        //            @Override
        //            public void onStart() {
        //                super.onStart();
        //                try {
        //                    mView.showLoading();
        //                } catch (Throwable e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //
        //            @Override
        //            public void onCompleted() {
        //                try {
        //                    mView.requestFinish();
        //                } catch (Throwable e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //                try {
        //                    mView.requestFinish();
        //                    if (e instanceof ApiException) {
        //                        ApiException baseException = (ApiException) e;
        //                        String code = baseException.getCode();
        //                        String message = baseException.getMessage();
        //                    } else if (e instanceof HttpException) {
        //                        mView.uidNull(((HttpException) e).code());
        //                    } else if (e instanceof SocketTimeoutException) {
        //                        mView.submitEvectionApplyFailure(-2, "网络不通，请检查网络连接！");
        //                    } else if (e instanceof NullPointerException) {
        //                        mView.submitEvectionApplyFailure(-2, "网络不通，请检查网络连接！");
        //                    } else if (e instanceof ConnectException) {
        //                        mView.submitEvectionApplyFailure(-2, "网络不通，请检查网络连接！");
        //                    }
        //                } catch (Throwable e1) {
        //                    e1.printStackTrace();
        //                }
        //            }
        //
        //            @Override
        //            public void onNext(String s) {
        //                try {
        //                    mView.submitEvectionApplySuccess(s);
        //                } catch (Throwable e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        });
    }

    //申请加班
    @Override
    public void addWorkApply(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.jsonPost(ApiJava.ADDWEORKAPPLY, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    QueryMonoBean queryMonoBean = new Gson().fromJson(t, QueryMonoBean.class);
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.addWorkApplySuccess(queryMonoBean.getData());
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            if (mView != null)
//                                mView.uidNull(0);
                            break;
                        default:
                            submitFailureTips(queryMonoBean);
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
                    if (mView != null) {
                        mView.addWorkApplyFailure(errorNo, strMsg);
                        if (mView != null)
                            mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mView != null)
                    mView.requestFinish();
            }
        });

        //                HttpMethods.getInstance().addWorkApply(body, new Subscriber<String>() {
        //
        //            @Override
        //            public void onStart() {
        //                super.onStart();
        //                try {
        //                    mView.showLoading();
        //                } catch (Throwable e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //
        //            @Override
        //            public void onCompleted() {
        //                try {
        //                    mView.requestFinish();
        //                } catch (Throwable e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //                try {
        //                    mView.requestFinish();
        //                    if (e instanceof ApiException) {
        //                        ApiException baseException = (ApiException) e;
        //                        String code = baseException.getCode();
        //                        String message = baseException.getMessage();
        //                    } else if (e instanceof HttpException) {
        //                        mView.uidNull(((HttpException) e).code());
        //                    } else if (e instanceof SocketTimeoutException) {
        //                        mView.submitEvectionApplyFailure(-2, "网络不通，请检查网络连接！");
        //                    } else if (e instanceof NullPointerException) {
        //                        mView.submitEvectionApplyFailure(-2, "网络不通，请检查网络连接！");
        //                    } else if (e instanceof ConnectException) {
        //                        mView.submitEvectionApplyFailure(-2, "网络不通，请检查网络连接！");
        //                    }
        //                } catch (Throwable e1) {
        //                    e1.printStackTrace();
        //                }
        //            }
        //
        //            @Override
        //            public void onNext(String s) {
        //                mView.submitEvectionApplySuccess(s);
        //            }
        //        });
    }

    //申请休假
    @Override
    public void submitFurlough(HttpParams httpParams) {
        mKjHttp.cleanCache();
        mKjHttp.jsonPost(ApiJava.SUBMITFURLOUGH, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    QueryMonoBean queryMonoBean = new Gson().fromJson(t, QueryMonoBean.class);
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.submitFurloughSuccess(queryMonoBean.getData());
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            if (mView != null)
//                                mView.uidNull(0);
                            break;
                        default:
                            submitFailureTips(queryMonoBean);
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
                    if (mView != null) {
                        mView.submitFurloughFailure(errorNo, strMsg);
                        if (mView != null)
                            mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mView != null)
                    mView.requestFinish();
            }
        });
    }

    @Override
    public void findSignReason() {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        mKjHttp.get(ApiJava.SIGNREASON, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    SingReasonBean singReasonBean = new Gson().fromJson(t, SingReasonBean.class);
                    switch (singReasonBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.findSignReasonSuccess(singReasonBean);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            if (mView != null)
//                                mView.uidNull(0);
                            break;
                        default:
                            if (mView != null)
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
                    if (mView != null)
                        mView.findSignReasonFailure(errorNo, strMsg);
                    if (mView != null)
                        mView.requestFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mView != null)
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
                if (mView != null)
                    mView.showLoading();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    QueryMonoBean queryMonoBean = new Gson().fromJson(t, QueryMonoBean.class);
                    switch (queryMonoBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.registrationCardSuccess(queryMonoBean.getData());
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
//                            if (mView != null)
//                                mView.uidNull(0);
                            break;
                        default:
                            submitFailureTips(queryMonoBean);
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
                    if (mView != null) {
                        mView.registrationCardFailure(errorNo, strMsg);
                        if (mView != null)
                            mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mView != null)
                    mView.requestFinish();
            }
        });
    }

    private void submitFailureTips(QueryMonoBean queryMonoBean) {
        try {
            if (queryMonoBean.getMessage() != null && queryMonoBean.getData() != null) {
                tips = queryMonoBean.getMessage() + queryMonoBean.getData();
            } else if (queryMonoBean.getMessage() != null) {
                tips = queryMonoBean.getMessage();
            } else if (queryMonoBean.getData() != null) {
                tips = queryMonoBean.getData();
            }
            if (tips != null) {
                String tip = tips.replace("NCHR系统错误", "");
                if (mView != null)
                    mView.submitFailureTips(tip);
            } else {
                if (mView != null)
                    mView.submitFailureTips("提交失败，请稍后重试！");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
