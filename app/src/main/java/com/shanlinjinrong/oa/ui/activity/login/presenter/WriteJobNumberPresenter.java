package com.shanlinjinrong.oa.ui.activity.login.presenter;

import android.text.TextUtils;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.login.contract.WriteJobNumberContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/10/09.
 * 获取验证码 presenter
 */
public class WriteJobNumberPresenter extends HttpPresenter<WriteJobNumberContract.View> implements WriteJobNumberContract.Presenter {

    @Inject
    public WriteJobNumberPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void getIdentifyingCode() {
        mKjHttp.cleanCache();

        mKjHttp.phpJsonGet(Api.SENDS_CAPTCHA, new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (mView != null)
                        mView.getIdentifyingCodeSuccess(data.getString("img"), data.getString("keycode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.getIdentifyingCodeFailed(errorNo);
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
    public void searchUser(String imgCode, String keyCode, String userCode) {
        mKjHttp.phpJsonGet(Api.USERS_SEARCH + "?imgcode=" + imgCode + "&keycode=" + keyCode + "&code=" + userCode, new HttpParams(), new HttpCallBack() {

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
                    if (mView != null) {
                        mView.hideLoading();
                    }
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    switch (code) {
                        case Api.RESPONSES_CODE_OK:
                            JSONArray data = jsonObject.getJSONArray("data");
                            User user = new User();
                            String email = data.getJSONObject(0).optString("email");
                            if (TextUtils.isEmpty(email) || "null".equalsIgnoreCase(email)) {
                                email = "";
                            }
                            user.setEmail(email);
//                            user.setCode(data.getJSONObject(0).getString("code"));
                            if (mView != null)
                                mView.searchUserSuccess(user);
                            break;
                        default:
                            if (mView != null)
                                mView.searchUserFailed(code, jsonObject.getString("info"));
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null)
                        mView.hideLoading();
                    mView.requestFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.hideLoading();
                    mView.searchUserFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    @Override
//    public void verifyCode(String imgCode, String keyCode) {
//        mKjHttp.phpJsonGet(Api.VERIFY_CODE + "?imgcode=" + imgCode + "&keycode=" + keyCode, new HttpParams(), new HttpCallBack() {
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(t);
//                    int code = jsonObject.getInt("code");
//                    switch (code) {
//                        case Api.RESPONSES_CODE_OK:
//                            if (mView != null)
//                                mView.verifyCodeSuccess();
//                            break;
//                        default:
//                            if (mView != null)
//                                mView.verifyCodeFailed(code, jsonObject.getString("info"));
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int errorNo, String strMsg) {
//                super.onFailure(errorNo, strMsg);
//                try {
//                    if (mView != null)
//                        mView.searchUserFailed(errorNo, strMsg);
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                try {
//                    if (mView != null)
//                        mView.requestFinish();
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
