package com.shanlinjinrong.oa.ui.activity.home.approval.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.approval.contract.ApplyForOfficeSuppliesContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/9/1.
 */

public class ApplyForOfficeSuppliesPresenter extends HttpPresenter<ApplyForOfficeSuppliesContract.View> implements ApplyForOfficeSuppliesContract.Presenter {

    @Inject
    public ApplyForOfficeSuppliesPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void loadAdmin(String apprId, String isLeader, String oid) {
        HttpParams params = new HttpParams();
        params.put("appr_id", "2");
        params.put("isleader", isLeader);
        params.put("oid", oid);
        mKjHttp.post(Api.GET_APPROVERS, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONArray ja = Api.getDataToJSONArray(jo);
                            mView.loadDataSuccess(ja);
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
                mView.loadDataFailed(errorNo, strMsg);
            }
        });
    }

    @Override
    public void submitData(String articleName, String applyTime) {
        HttpParams params = new HttpParams();
        params.put("article_name", articleName);
        params.put("application_time", applyTime);
        mKjHttp.post(Api.POSTOFFICE_APPROVAL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if ((Api.getCode(jo) == Api.RESPONSES_CODE_OK)) {
                        mView.submitSuccess();
                    } else if ((Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL)) {
                        mView.uidNull(Api.getCode(jo));
                    } else {
                        mView.submitOther(Api.getInfo(jo));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.submitFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.submitFailed(errorNo, strMsg);
            }
        });
    }
}
