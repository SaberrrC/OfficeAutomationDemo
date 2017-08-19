package com.shanlin.oa.ui.activity.login.presenter;

import com.shanlin.oa.common.Api;
import com.shanlin.oa.net.MyKjHttp;
import com.shanlin.oa.ui.activity.login.contract.LoginActivityContract;
import com.shanlin.oa.ui.base.HttpPresenter;
import com.shanlin.oa.utils.LogUtils;

import org.json.JSONException;
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
        HttpParams params = new HttpParams();
        params.put("email", account);
        params.put("pwd", psw);
        mKjHttp.post(Api.LOGIN, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                System.out.println(t);
                LogUtils.e("登录返回数据-》" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        mView.loginSuccess(Api.getDataToJSONObject(jo));
                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL) {
                       mView.loginFailed(Api.getCode(jo));
                    } else if ((Api.getCode(jo) == Api.RESPONSES_CODE_ACCOUNT_PASSWORD_ERROR)) {
                       mView.accountOrPswError(Api.getInfo(jo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                LogUtils.e(errorNo + "--" + strMsg);
                mView.loginFailed(errorNo);
                super.onFailure(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }
        });
    }
}
