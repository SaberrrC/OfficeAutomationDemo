package com.shanlinjinrong.oa.ui.activity.my.presenter;

import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.my.contract.ModificationEmailContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

public class ModificationEmailPresenter extends HttpPresenter<ModificationEmailContract.View> implements ModificationEmailContract.Presenter {

    @Inject
    public ModificationEmailPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void modificationEmail(String email) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        httpParams.putJsonParams(email);
        mKjHttp.jsonPost(ApiJava.CHANGE_EMAIL, httpParams, new HttpCallBack() {
            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.modificationEmailFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    String code = jsonObject.getString("code");
                    switch (code) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.modificationEmailSuccess();
                            break;

                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                            mView.uidNull(code);
                        default:
                            if (mView != null)
                                mView.modificationEmailFailed(0, jsonObject.getString("info"));
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
