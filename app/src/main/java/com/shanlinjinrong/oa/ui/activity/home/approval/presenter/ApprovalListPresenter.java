package com.shanlinjinrong.oa.ui.activity.home.approval.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.model.approval.Approval;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.approval.contract.ApprovalListContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/9/1.
 */

public class ApprovalListPresenter extends HttpPresenter<ApprovalListContract.View> implements ApprovalListContract.Presenter {

    @Inject
    public ApprovalListPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void loadData(final int currentState, int page, int limit, String time, String where, final boolean isMore) {
        HttpParams params = new HttpParams();
        params.put("page", page);
        params.put("limit", limit);
        params.put("time", time);
        params.put("where", where);
        String requestUrl = Api.APPROVAL_LIST_ME_LAUNCH;
        if (currentState == 1) {
            requestUrl = Api.APPROVAL_LIST_ME_LAUNCH;
        } else if (currentState == 2) {
            requestUrl = Api.APPROVAL_LIST_WAIT_APPROVAL;
        } else if (currentState == 3) {
            requestUrl = Api.APPROVAL_LIST_ME_APPROVALED;
        }
        mKjHttp.post(requestUrl, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("ApprovalListActivity->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            ArrayList<Approval> listApproval = new ArrayList<>();
                            JSONArray notices = Api.getDataToJSONArray(jo);
                            Approval notice;
                            for (int i = 0; i < notices.length(); i++) {
                                JSONObject jsonObject = notices.getJSONObject(i);
                                if (currentState == 1) {
                                    notice =
                                            new Approval(jsonObject, true);
                                } else {
                                    notice =
                                            new Approval(jsonObject, false);
                                }
                                listApproval.add(notice);
                            }
                            mView.loadDataSuccess(listApproval, isMore);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                        case Api.RESPONSES_CONTENT_EMPTY:
                            mView.loadDataResponseEmpty();
                            break;
                        case Api.LIMIT_CONTENT_EMPTY:
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

    @Override
    public void readPush(String departmentId, String pid) {
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        params.put("pid", pid);
        mKjHttp.post(Api.MESSAGE_READPUSH, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            break;
                        case Api.RESPONSES_CONTENT_EMPTY:
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
                mView.readFailed(errorNo, strMsg);
            }
        });
    }
}
