package com.shanlinjinrong.oa.ui.activity.push.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.model.SystemNotice;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.push.contract.SystemNoticesContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/30.
 * SystemNoticesPresenter
 */
public class SystemNoticesPresenter extends HttpPresenter<SystemNoticesContract.View> implements SystemNoticesContract.Presenter {

    @Inject
    public SystemNoticesPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void readAllSystemNotices(String departmentId) {
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        mKjHttp.post(Api.SYSTEM_NOTICE_ALL_READ, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                mView.requestFinish();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.readSystemNoticesFailed(errorNo, strMsg);
            }
        });
    }

    @Override
    public void loadData(String departmentId) {
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        mKjHttp.post(Api.SYSTEM_NOTICE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            List<SystemNotice> list = new ArrayList<>();
                            JSONArray notices = Api.getDataToJSONArray(jo);
                            for (int i = 0; i < notices.length(); i++) {
                                JSONObject jsonObject = notices.getJSONObject(i);
                                SystemNotice systemNotice =
                                        new SystemNotice(jsonObject);
                                list.add(systemNotice);
                            }
                            mView.loadDataSuccess(list);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mView.loadDataEmpty();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
//                            mView.uidNull(Api.getCode(jo));
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
