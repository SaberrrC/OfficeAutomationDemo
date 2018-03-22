package com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter;

import android.text.TextUtils;
import android.util.JsonToken;

import com.alibaba.fastjson.JSON;
import com.example.retrofit.model.responsebody.ApporveBodyItemBean;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.CommonRequestBean;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.AgreeDisagreeResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.OfficeSuppliesListBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingSearchResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONObject;
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
        mKjHttp.cleanCache();
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
        mKjHttp.get(stringBuilder.toString(), httpParams, new HttpCallBack() {
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
                        if (mView != null) {
                            mView.onGetApproveDataSuccess(bean);
                        }
                        return;
                    }
                    if (mView != null) {
                        mView.onGetApproveDataFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
                    }
                } catch (Exception e) {
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
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.onGetApproveDataFailure(errorNo, strMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getSelectData(String privateCode, String noCheck, String pageNum, String pageSize, String time, String billType, String userName) {
        mKjHttp.cleanCache();
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
        mKjHttp.get(stringBuilder.toString(), httpParams, new HttpCallBack() {
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
                        if (mView != null) {
                            mView.onSearchSuccess(bean);
                        }
                        return;
                    }
                    if (mView != null) {
                        mView.onSearchFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    if (t.contains("\"code\":\"020000\"")) {
                        if (mView != null) {
                            mView.onSearchFailure(020000, "暂无内容");
                        }
                        return;
                    }
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
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.onGetApproveDataFailure(errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // 批量审批 行政 加 工作流
    @Override
    public void postAgreeDisagree(List<ApporveBodyItemBean> list, boolean isOfficeSupplies) {
        mKjHttp.cleanCache();
        String json = new Gson().toJson(list);
        HttpParams httpParams = new HttpParams();
        httpParams.putJsonParams(json);
        if (isOfficeSupplies) {
            mKjHttp.jsonPost(ApiJava.OFFICE_SUPPLIES_APPROVE, httpParams, new HttpCallBack() {
                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    try {
                        AgreeDisagreeResultBean resultBean = new Gson().fromJson(t, AgreeDisagreeResultBean.class);
                        if (TextUtils.equals(resultBean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                            if (mView != null) {
                                mView.onApproveSuccess(resultBean, list);
                            }
                            return;
                        }
                        if (mView != null) {
                            mView.onApproveFailure(Integer.parseInt(resultBean.getCode()), resultBean.getMessage());
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    super.onFailure(errorNo, strMsg);
                    try {
                        if (mView != null) {
                            mView.uidNull(strMsg);
                            mView.onApproveFailure(errorNo, strMsg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } else {
            mKjHttp.jsonPost(ApiJava.ARGEE_DISAGREE_APPROVE, httpParams, new HttpCallBack() {
                @Override
                public void onFailure(int errorNo, String strMsg) {
                    super.onFailure(errorNo, strMsg);
                    try {
                        if (mView != null) {
                            mView.uidNull(strMsg);
                            mView.onApproveFailure(errorNo, strMsg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    try {
                        AgreeDisagreeResultBean resultBean = new Gson().fromJson(t, AgreeDisagreeResultBean.class);
                        if (TextUtils.equals(resultBean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                            if (mView != null) {
                                mView.onApproveSuccess(resultBean, list);
                            }
                            return;
                        }
                        if (mView != null) {
                            mView.onApproveFailure(Integer.parseInt(resultBean.getCode()), resultBean.getMessage());
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void getOfficeSuppliesApproveData(String timeCode, String gloableStatus, String pageNum, String pageSize) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?timeCode=").append(timeCode).append("&gloableStatus=").append(gloableStatus).append("&pageNum=" + pageNum).append("&pageSize=" + pageSize);
        mKjHttp.get(ApiJava.REQUEST_OFFICE_PPLIE + stringBuilder, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    OfficeSuppliesListBean bean = new Gson().fromJson(t, new TypeToken<OfficeSuppliesListBean>() {
                    }.getType());
                    if (TextUtils.equals(bean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                        if (mView != null) {
                            mView.onGetApproveDataSuccess(bean);
                        }
                        return;
                    }
                    if (mView != null) {
                        mView.onGetApproveDataFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.onGetApproveDataFailure(errorNo, strMsg);
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

    @Override
    public void getOfficeSuppliesManage(String finished, String processInstanceBegin, String pageNum, String pageSize) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("?finished=").append(finished).append("&processInstanceBegin=").append(processInstanceBegin).append("&pageNum=").append(pageNum).append("&pageSize=").append(pageSize);
        mKjHttp.get(ApiJava.REQUEST_OFFICE_MANAGE + stringBuffer, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    OfficeSuppliesListBean bean = new Gson().fromJson(t, new TypeToken<OfficeSuppliesListBean>() {
                    }.getType());
                    if (TextUtils.equals(bean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                        if (mView != null) {
                            mView.onGetApproveDataSuccess(bean);
                        }
                        return;
                    }
                    if (mView != null) {
                        mView.onGetApproveDataFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.onGetApproveDataFailure(errorNo, strMsg);
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

    @Override
    public void getOfficeSuppliesManage(String finished, String processInstanceBegin, String pageNum, String pageSize, String name) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("?finished=").append(finished).append("&processInstanceBegin=").append(processInstanceBegin).append("&pageNum=").append(pageNum).append("&pageSize=").append(pageSize);
        if (!TextUtils.isEmpty(name)) {
            stringBuffer.append("&startedByLike=").append(name);
        }
        mKjHttp.get(ApiJava.REQUEST_OFFICE_MANAGE + stringBuffer, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    OfficeSuppliesListBean bean = new Gson().fromJson(t, new TypeToken<OfficeSuppliesListBean>() {
                    }.getType());
                    if (TextUtils.equals(bean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                        if (mView != null) {
                            mView.onGetApproveDataSuccess(bean);
                        }
                        return;
                    }
                    if (mView != null) {
                        mView.onGetApproveDataFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.onGetApproveDataFailure(errorNo, strMsg);
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
}