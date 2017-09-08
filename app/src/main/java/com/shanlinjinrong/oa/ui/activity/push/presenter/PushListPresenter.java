package com.shanlinjinrong.oa.ui.activity.push.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.model.PushMsg;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.push.contract.PushListContract;
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
 * Created by 丁 on 2017/8/30.
 * PushListPresenter
 */
public class PushListPresenter extends HttpPresenter<PushListContract.View> implements PushListContract.Presenter {

    @Inject
    public PushListPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void readPush(String departmentId, String pid) {
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        params.put("pid", pid);

        mKjHttp.post(Api.MESSAGE_READPUSH, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
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
                mView.readPushFailed(errorNo, strMsg);
            }
        });
    }

    @Override
    public void loadPushMsg(String departmentId, int curPage, int limit, final boolean loadMore) {
        HttpParams params = new HttpParams();
        params.put("limit", limit);
        params.put("page", curPage);
        params.put("department_id", departmentId);
        mKjHttp.post(Api.MESSAGE_PUSHS, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            ArrayList<PushMsg> listPushMsgs = new ArrayList<>();
                            JSONArray notices = Api.getDataToJSONArray(jo);
                            for (int i = 0; i < notices.length(); i++) {
                                JSONObject jsonObject = notices.getJSONObject(i);
                                PushMsg pushMsg =
                                        new PushMsg(jsonObject);
                                listPushMsgs.add(pushMsg);
                            }
                            mView.loadPushMsgSuccess(listPushMsgs, loadMore);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mView.loadPushMsgEmpty();
                            break;

                        case Api.LIMIT_CONTENT_EMPTY:
                            mView.loadPushMsgLimitContentEmpty();
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
                mView.loadPushMsgFailed(errorNo, strMsg);
            }
        });
    }

}
