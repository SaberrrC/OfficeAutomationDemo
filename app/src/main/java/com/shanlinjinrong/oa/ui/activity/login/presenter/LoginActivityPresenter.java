package com.shanlinjinrong.oa.ui.activity.login.presenter;

import com.alibaba.fastjson.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.utils.AESUtils;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.UserInfo;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.login.bean.RequestCodeBean;
import com.shanlinjinrong.oa.ui.activity.login.contract.LoginActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class LoginActivityPresenter extends HttpPresenter<LoginActivityContract.View> implements LoginActivityContract.Presenter {

    @Inject
    public LoginActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void login(String account, String psw) {
        mKjHttp.cleanCache();
        HttpParams params = new HttpParams();
        params.put("email", account);
        params.put("pwd", psw);
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("email", account);
//            jsonObject.put("pwd", psw);
//            String currentDate = DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss");
//            long l = DateUtils.dateToLong(currentDate, "yyyy-MM-dd HH:mm:ss");
//            String s = String.valueOf(l);
//            String substring = s.substring(0, 13);
//            jsonObject.put("time", substring);
//
//            params.putHeaders("sign",  AESUtils.Encrypt(jsonObject.toString()));
//            params.put("email", account);
//            params.put("pwd", psw);
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
        mKjHttp.post(ApiJava.LOGIN, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                System.out.println(t);
                try {
                    UserInfo user = new Gson().fromJson(t, new TypeToken<UserInfo>() {
                    }.getType());

                    switch (user.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null) {
                                mView.loginSuccess(user.getData());
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
                                mView.loginFailed(user.getCode());
                            }
                            break;
                        //用户名 密码不存在
                        case ApiJava.NOT_EXIST_USER:
                            if (mView != null) {
                                mView.accountOrPswError(user.getMessage());
                            }
                            break;
                        default:
                            if (mView != null) {
                                if ("100169".equals(user.getCode())) {
                                    mView.showVerifyCode(user.getMessage());
                                } else if ("100016".equals(user.getCode())) {
                                    try {
                                        if (user.getData() != null) {
                                            if (user.getData().getNeedYzm() != null) {
                                                if ("1".equals(user.getData().getNeedYzm())) {
                                                    mView.showVerifyCode(user.getMessage());
                                                }
                                            }
                                        } else {
                                            mView.accountOrPswError(user.getCode(), user.getMessage());
                                        }
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mView.accountOrPswError(user.getCode(), user.getMessage());
                                }
                            }
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                LogUtils.e(errorNo + "--" + strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.loginFailed(String.valueOf(errorNo));
                    }
                    super.onFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void login(String account, String psw, String keyCode, String code) {
        mKjHttp.cleanCache();
        HttpParams params = new HttpParams();
        params.put("email", account);
        params.put("pwd", psw);
        params.put("keyCode", keyCode);
        params.put("code", code);
        mKjHttp.post(ApiJava.LOGIN, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                System.out.println(t);
                try {
                    UserInfo user = new Gson().fromJson(t, new TypeToken<UserInfo>() {
                    }.getType());

                    switch (user.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null) {
                                mView.loginSuccess(user.getData());
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null) {
                                mView.loginFailed(user.getCode());
                            }
                            break;
                        //用户名 密码不存在
                        case ApiJava.NOT_EXIST_USER:
                            if (mView != null) {
                                mView.accountOrPswError(user.getMessage());
                            }
                            break;
                        case "300001":
                            if (mView != null) {
                                mView.refreshVerifyCode(user.getMessage());
                            }
                            break;
                        default:
                            if (mView != null) {
                                if ("100169".equals(user.getCode())) {
                                    mView.showVerifyCode(user.getMessage());
                                } else if ("100016".equals(user.getCode())) {
                                    try {
                                        if (user.getData() != null) {
                                            if (user.getData().getNeedYzm() != null) {
                                                if ("1".equals(user.getData().getNeedYzm())) {
                                                    mView.showVerifyCode(user.getMessage());
                                                }
                                            }
                                        }
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    mView.accountOrPswError(user.getCode(), user.getMessage());
                                }
                            }
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                LogUtils.e(errorNo + "--" + strMsg);
                try {
                    if (mView != null) {
                        mView.uidNull(strMsg);
                        mView.loginFailed(String.valueOf(errorNo));
                    }
                    super.onFailure(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.requestFinish();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void QueryVerifyCode() {
        mKjHttp.cleanCache();
        mKjHttp.get(ApiJava.SENDS_CAPTCHA + "?channel=1", new HttpParams(), new HttpCallBack() {
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
}
