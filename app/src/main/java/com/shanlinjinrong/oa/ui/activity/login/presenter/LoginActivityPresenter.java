package com.shanlinjinrong.oa.ui.activity.login.presenter;

import android.text.TextUtils;

import com.example.retrofit.model.responsebody.LimitResponseBody;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.UserInfo;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.login.contract.LoginActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

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
        mKjHttp.post(ApiJava.LOGIN, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                System.out.println(t);
                LogUtils.e("登录返回数据-》" + t);
                try {
                    UserInfo user = new Gson().fromJson(t, new TypeToken<UserInfo>() {
                    }.getType());

                    switch (user.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.loginSuccess(user.getData());
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
    public void getUserLimit() {
        mKjHttp.cleanCache();
        mKjHttp.get(ApiJava.USER_LIMIT, new HttpParams(), new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    LimitResponseBody bean = new Gson().fromJson(t, LimitResponseBody.class);
                    if (TextUtils.equals(bean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                        if (mView != null) {
                            mView.onGetUserLimitSuccess(bean);
                        }
                        return;
                    }
                    if (mView != null) {
                        mView.onGetUserLimitFailure(bean.getCode(), bean.getMessage());
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
                        mView.onGetUserLimitFailure(String.valueOf(errorNo), strMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
