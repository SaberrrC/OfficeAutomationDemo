package com.shanlinjinrong.oa.ui.activity.home.approval.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.approval.contract.MeLaunchOfficesSuppliesContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/9/1.
 */

public class MeLaunchOfficesSuppliesPresenter extends HttpPresenter<MeLaunchOfficesSuppliesContract.View> implements MeLaunchOfficesSuppliesContract.Presenter {

    @Inject
    public MeLaunchOfficesSuppliesPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void loadData(String apprId, String oalId) {
        HttpParams params = new HttpParams();
        params.put("appr_id", apprId);
        params.put("oal_id", oalId);

        mKjHttp.post(Api.APPROVAL_INFO, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONObject data = Api.getDataToJSONObject(jo);
                            mView.loadDataSuccess(data);

                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mView.loadDataEmpty();
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
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.loadDataFailed(errorNo, strMsg);
            }
        });
    }
}
