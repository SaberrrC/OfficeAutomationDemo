package com.shanlin.oa.ui.activity.login.presenter;

import com.shanlin.oa.common.Api;
import com.shanlin.oa.net.MyKjHttp;
import com.shanlin.oa.ui.activity.login.contract.FindPassWordContract;
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
public class FindPasswordPresenter extends HttpPresenter<FindPassWordContract.View> implements FindPassWordContract.Presenter {

    @Inject
    public FindPasswordPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void findPassword(String email) {
        HttpParams params = new HttpParams();
        params.put("email", email);
        mKjHttp.post(Api.FIND_PASSWORD, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                mView.findFinish();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t.toString());
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.SEND_FAILD_TRY_AGIN) {
                        mView.findFailed();
                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        mView.findSuccess();
                    } else if ((Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL)) {
                        mView.uidNull(Api.getCode(jo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                mView.findFailed();
                super.onFailure(errorNo, strMsg);
            }
        });
    }
}
