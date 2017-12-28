package com.shanlinjinrong.oa.ui.activity.my.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.CommonRequestBean;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.my.contract.ModifyPhoneActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 修改手机号码 presenter
 */
public class ModifyPhoneActivityPresenter extends HttpPresenter<ModifyPhoneActivityContract.View> implements ModifyPhoneActivityContract.Presenter {

    @Inject
    public ModifyPhoneActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void modifyPhone(final String phoneNum) {
        HttpParams params = new HttpParams();
        params.put("phone", phoneNum);
        mKjHttp.post( Api.PHONENUMBER_UPDATE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    CommonRequestBean requestStatus = new Gson().fromJson(t, new TypeToken<CommonRequestBean>() {
                    }.getType());
                    switch (requestStatus.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.modifySuccess(phoneNum);
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                                mView.uidNull(requestStatus.getCode());
                            break;
                        default:
                            if (mView != null)
                                mView.modifyFailed(0, "修改手机号失败");
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
                        mView.modifyFailed(errorNo, strMsg);
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
