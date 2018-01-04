package com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter;

import android.text.TextUtils;

import com.example.retrofit.model.responsebody.ApporveBodyItemBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.AgreeDisagreeResultBean;
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
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiJava.MYAPPLY_QUERY_APPROVE_INFO);
        if (!TextUtils.isEmpty(billType)) {
            stringBuilder.append("?" + "billType=" + billType);
        }
        if (!TextUtils.isEmpty(billCode)) {
            stringBuilder.append("&" + "billCode=" + billCode);
        }
        mKjHttp.get(stringBuilder.toString(), httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String json) {
                super.onSuccess(json);
                try {
                     if (mView != null) {
                         mView.onGetApproveInfoSuccess(json);
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
                         mView.onGetApproveInfoFailure(String.valueOf(errorNo), strMsg);
                     }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void postTackBack(String billCode, String billType) {
        mKjHttp.cleanCache();
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
                    try {
                         if (mView != null) {
                             mView.uidNull(strMsg);
                             mView.onGetApproveInfoFailure(String.valueOf(errorNo), strMsg);
                         }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    try {
                        TackBackResultBean tackBackResultBean = new Gson().fromJson(t, TackBackResultBean.class);
                        if (TextUtils.equals(tackBackResultBean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                             if (mView != null) {
                                 mView.onTackBackSuccess(tackBackResultBean);
                             }
                            return;
                        }
                         if (mView != null) {
                             mView.onGetApproveInfoFailure(tackBackResultBean.getCode(), tackBackResultBean.getMessage());
                         }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postApproval(List<ApporveBodyItemBean> list) {
        mKjHttp.cleanCache();
        String json = new Gson().toJson(list);
        HttpParams httpParams = new HttpParams();
        httpParams.putJsonParams(json);
        mKjHttp.post(ApiJava.ARGEE_DISAGREE_APPROVE, httpParams, new HttpCallBack() {
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
                             mView.onApproveSuccess(resultBean);
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

    @Override
    public void getDelete(String billCode, String billType) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        try {
            mKjHttp.get(ApiJava.DELETE_APPROVEL +"?billCode="+billCode+"&billType="+billType, httpParams, new HttpCallBack() {
                @Override
                public void onFailure(int errorNo, String strMsg) {
                    super.onFailure(errorNo, strMsg);
                    try {
                         if (mView != null) {
                             mView.uidNull(strMsg);
                             mView.onDeleteFailure(String.valueOf(errorNo), strMsg);
                         }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    try {
                        DeleteBean bean = new Gson().fromJson(t, DeleteBean.class);
                        if (TextUtils.equals(bean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                             if (mView != null) {
                                 mView.onDeleteSuccess(bean);
                             }
                            return;
                        }
                         if (mView != null) {
                             mView.onDeleteFailure(bean.getCode(), bean.getMessage());
                         }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        if (t.contains("\"code\":\"000000\"")) {
                            try {
                                 if (mView != null) {
                                     mView.onDeleteFailure("500", "ok");
                                 }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}