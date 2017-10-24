package com.shanlinjinrong.oa.ui.activity.my.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.my.contract.UserInfoActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * userinfo presenter
 */
public class UserInfoActivityPresenter extends HttpPresenter<UserInfoActivityContract.View> implements UserInfoActivityContract.Presenter {

    @Inject
    public UserInfoActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void upLoadPortrait(String departmentId, String portrait, File file) {
        mKjHttp.cleanCache();
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        params.put("file", file);

//        params.put("file", portrait);
//        params.put("portrait", file);
        mKjHttp.post(Api.PERSON_UPLOAD, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            mView.upLoadSuccess(Api.getDataToJSONObject(jo).get("portrait") + "");
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e("上传图片失败" + errorNo + strMsg);
                mView.upLoadFailed(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.upLoadFinish();
            }
        });
    }
}
