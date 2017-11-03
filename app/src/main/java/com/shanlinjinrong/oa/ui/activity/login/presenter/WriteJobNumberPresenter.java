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
                    mView.getIdentifyingCodeSuccess(data.getString("img"), data.getString("code"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.getIdentifyingCodeFailed(errorNo);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    public void searchUser(String jobNum) {
        mKjHttp.phpJsonGet(Api.USERS_SEARCH + jobNum, new HttpParams(), new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    switch (code) {
                        case Api.RESPONSES_CODE_OK:
                            JSONArray data = jsonObject.getJSONArray("data");
                            User user = new User();
                            String email = data.getJSONObject(0).getString("email");
                            if (TextUtils.isEmpty(email) || "null".equalsIgnoreCase(email)) {
                                email = "";
                            }
                            user.setEmail(email);
                            user.setCode(data.getJSONObject(0).getString("code"));
                            mView.searchUserSuccess(user);
                            break;
                        default:
                            mView.searchUserFailed(code,jsonObject.getString("info"));
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.searchUserFailed(errorNo,strMsg);
            }
        });
    }
}
