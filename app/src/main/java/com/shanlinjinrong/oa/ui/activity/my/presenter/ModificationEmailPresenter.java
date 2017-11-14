package com.shanlinjinrong.oa.ui.activity.my.presenter;

import com.shanlinjinrong.oa.common.Api;
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
    public void modificationEmail(String email, String userId) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        httpParams.put("email", email);
        httpParams.put("code", userId);
        mKjHttp.phpJsonPost(Api.MODIFICATION_EMAIL, httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    mView.showLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jsonObject = new JSONObject(t);
                    int code = jsonObject.getInt("code");
                    switch (code) {
                        case Api.RESPONSES_CODE_OK:
                            if (mView != null)
                                mView.modificationEmailSuccess();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            if (mView != null)
                                mView.uidNull(Api.getCode(jsonObject));
                        default:
                            if (mView != null)
                                mView.modificationEmailFailed(code,jsonObject.getString("info"));
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
                        mView.modificationEmailFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    mView.hideLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
