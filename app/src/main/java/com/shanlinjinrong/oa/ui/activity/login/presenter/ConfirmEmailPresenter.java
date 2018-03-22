package com.shanlinjinrong.oa.ui.activity.login.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.utils.AESUtils;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.CommonRequestBean;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.login.contract.ConfirmEmailContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.DateUtils;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 发送邮件 presenter
 */
public class ConfirmEmailPresenter extends HttpPresenter<ConfirmEmailContract.View> implements ConfirmEmailContract.Presenter {

    @Inject
    public ConfirmEmailPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void sendEmail(String code) {
        mKjHttp.cleanCache();
        HttpParams params = new HttpParams();
        params.put("code", code);

        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", code);
            String time = String.valueOf(DateUtils.dateToLong(DateUtils.getCurrentDate(pattern), pattern)).substring(0, 13);
            jsonObject.put("time", time);
            params.putHeaders("sign", AESUtils.Encrypt(jsonObject.toString()));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        mKjHttp.post(ApiJava.USERS_REPWD, params, new HttpCallBack() {

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
                    CommonRequestBean requestStatus = new Gson().fromJson(t, new TypeToken<CommonRequestBean>() {
                    }.getType());
                    if (mView != null) {
                        mView.hideLoading();
                    }
                    switch (requestStatus.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.sendEmailSuccess();
                            break;
                        default:
                            mView.sendEmailFailed(0, requestStatus.getMessage());
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
                        mView.uidNull(strMsg);
                        mView.hideLoading();
                        mView.sendEmailFailed(errorNo, strMsg);
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
        });
    }
}
