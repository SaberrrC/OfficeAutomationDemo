package com.shanlinjinrong.oa.ui.activity.my.presenter;

import android.support.v4.app.NavUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.CommonRequestBean;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.my.contract.ModifyPswActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 修改密码 presenter
 */
public class ModifyPswActivityPresenter extends HttpPresenter<ModifyPswActivityContract.View> implements ModifyPswActivityContract.Presenter {

    @Inject
    public ModifyPswActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void modifyPsw(String oldPsw, String newPsw) {
        HttpParams params = new HttpParams();
        params.put("oldpassword", oldPsw);
        params.put("newpassword", newPsw);
        mKjHttp.post(Api.PASSWORD_UPDATE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    CommonRequestBean requestCode = new Gson().fromJson(t, new TypeToken<CommonRequestBean>() {
                    }.getType());
                    switch (requestCode.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.modifySuccess();
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                                mView.uidNull(requestCode.getCode());
                            break;
                        default:
                            if (mView != null)
                                mView.modifyFailed(requestCode.getMessage());
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
                        mView.modifyFailed("修改密码失败");
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
}
