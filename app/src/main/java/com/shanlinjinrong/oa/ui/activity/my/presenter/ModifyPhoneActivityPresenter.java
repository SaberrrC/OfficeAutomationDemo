package com.shanlinjinrong.oa.ui.activity.my.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.my.contract.ModifyPhoneActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 修改手机号码 presenter
 */
public class ModifyPhoneActivityPresenter extends HttpPresenter<ModifyPhoneActivityContract.View> implements ModifyPhoneActivityContract.Presenter {

    @Inject
    public ModifyPhoneActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void modifyPhone(String departmentId, final String phoneNum) {
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        params.put("phone", phoneNum);
        mKjHttp.post(Api.PHONENUMBER_UPDATE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                try {
                    JSONObject jo = new JSONObject(t);
                    int code = Api.getCode(jo);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            mView.modifySuccess(phoneNum);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(code);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.modifyFailed(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
}
