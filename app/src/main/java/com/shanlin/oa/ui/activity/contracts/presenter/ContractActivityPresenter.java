package com.shanlin.oa.ui.activity.contracts.presenter;

import com.shanlin.oa.common.Api;
import com.shanlin.oa.model.Contacts;
import com.shanlin.oa.net.MyKjHttp;
import com.shanlin.oa.ui.activity.contracts.contract.ContractActivityContract;
import com.shanlin.oa.ui.base.HttpPresenter;
import com.shanlin.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/21.
 */
public class ContractActivityPresenter extends HttpPresenter<ContractActivityContract.View> implements ContractActivityContract.Presenter {

    @Inject
    public ContractActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public List<Contacts> loadData(String departmentId) {
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        mKjHttp.post(Api.GET_CONTACTS, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                mView.loadDataFinish();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t.toString());
                List<Contacts> items = new ArrayList<>();
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONArray jDepartment = Api.getDataToJSONObject(jo)
                                    .getJSONArray("department");
                            for (int i = 0; i < jDepartment.length(); i++) {
                                JSONObject d = jDepartment.getJSONObject(i);
                                Contacts c = new Contacts(d);
                                items.add(c);
                            }
                            JSONArray jEmployee = Api.getDataToJSONObject(jo)
                                    .getJSONArray("employee");
                            for (int i = 0; i < jEmployee.length(); i++) {
                                JSONObject e = jEmployee.getJSONObject(i);
                                Contacts c = new Contacts(e);
                                items.add(c);
                            }
                            mView.loadDataSuccess(items);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mView.loadDataEmpty();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            mView.loadDataTokenNoMatch(Api.getCode(jo));
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                mView.loadDataFailed(errorNo, strMsg);
                super.onFailure(errorNo, strMsg);
            }
        });
        return null;
    }
}
