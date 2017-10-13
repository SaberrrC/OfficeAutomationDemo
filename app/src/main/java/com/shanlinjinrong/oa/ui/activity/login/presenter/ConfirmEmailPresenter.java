package com.shanlinjinrong.oa.ui.activity.login.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.login.contract.ConfirmEmailContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/10/09.
 * 发送邮件 presenter
 */
public class ConfirmEmailPresenter extends HttpPresenter<ConfirmEmailContract.View> implements ConfirmEmailContract.Presenter {

    @Inject
    public ConfirmEmailPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void sendEmail(String code, String emailAddress) {
        HttpParams params = new HttpParams();
        params.put("code", code);
        params.put("email", emailAddress);
        mKjHttp.phpJsonPost(Api.USERS_REPWD, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    if (code == Api.RESPONSES_CODE_OK) {
                        mView.sendEmailSuccess();
                    } else {
                        mView.sendEmailFailed(code, jsonObject.getString("info"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.sendEmailFailed(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
}
