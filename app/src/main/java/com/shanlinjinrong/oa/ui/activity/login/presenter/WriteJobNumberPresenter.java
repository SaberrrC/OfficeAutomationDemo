package com.shanlinjinrong.oa.ui.activity.login.presenter;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.utils.AESUtils;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.login.bean.QueryPhoneBean;
import com.shanlinjinrong.oa.ui.activity.login.bean.RequestCodeBean;
import com.shanlinjinrong.oa.ui.activity.login.contract.WriteJobNumberContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.DateUtils;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 获取验证码 presenter
 */
public class WriteJobNumberPresenter extends HttpPresenter<WriteJobNumberContract.View> implements WriteJobNumberContract.Presenter {

    @Inject
    public WriteJobNumberPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    //获取验证码
    @Override
    public void getIdentifyingCode() {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        try {
            JSONObject jsonObject = new JSONObject();
            String pattern = "yyyy-MM-dd HH:mm:ss";
            String time = String.valueOf(DateUtils.dateToLong(DateUtils.getCurrentDate(pattern), pattern)).substring(0, 13);
            jsonObject.put("time", time);
            httpParams.putHeaders("sign", AESUtils.Encrypt(jsonObject.toString()));
        }catch (Throwable e){
            e.printStackTrace();
        }
        mKjHttp.get(ApiJava.SENDS_CAPTCHA, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    RequestCodeBean requestStatus = new Gson().fromJson(t, new TypeToken<RequestCodeBean>() {
                    }.getType());

                    switch (requestStatus.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null) {
                                mView.getIdentifyingCodeSuccess(requestStatus.getData().getImg(), requestStatus.getData().getKeyCode());
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.getIdentifyingCodeFailed(errorNo);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void searchUser(String imgCode, String keyCode, String userCode) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("code", userCode);
        httpParams.put("imgCode", imgCode);
        httpParams.put("keyCode", keyCode);
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", userCode);
            jsonObject.put("imgCode", imgCode);
            jsonObject.put("keyCode", keyCode);
            String time = String.valueOf(DateUtils.dateToLong(DateUtils.getCurrentDate(pattern), pattern)).substring(0, 13);
            jsonObject.put("time", time);
            httpParams.putHeaders("sign", AESUtils.Encrypt(jsonObject.toString()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mKjHttp.post(ApiJava.USERS_SEARCH, httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    QueryPhoneBean queryPhoneBean = new Gson().fromJson(t, new TypeToken<QueryPhoneBean>() {
                    }.getType());
                    switch (queryPhoneBean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.searchUserSuccess(queryPhoneBean.getData().getPhone());
                            break;
                        case ApiJava.REQUEST_NO_RESULT:
                            mView.searchUserEmpty("查询无结果");
                            break;
                        default:
                            if (mView != null) {
                                mView.searchUserFailed(0, queryPhoneBean.getMessage());
                            }
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

                try {
                    if (mView != null) {
                        mView.hideLoading();
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
                        mView.uidNull(strMsg);
                        mView.hideLoading();
                        mView.searchUserFailed(errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
