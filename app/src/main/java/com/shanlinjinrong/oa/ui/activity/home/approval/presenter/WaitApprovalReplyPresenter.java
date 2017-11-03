package com.shanlinjinrong.oa.ui.activity.home.approval.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.approval.contract.WaitApprovalReplyContract;
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

public class WaitApprovalReplyPresenter extends HttpPresenter<WaitApprovalReplyContract.View> implements WaitApprovalReplyContract.Presenter {

    @Inject
    public WaitApprovalReplyPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void finishReply(boolean isReject, String apprId, String oaId, String reply) {
        HttpParams params = new HttpParams();

        params.put("oa_id", oaId == null ? "" : oaId);
        params.put("reply", reply);

        // 1公告 2办公用品3请假4出差
        String url = "";
        if (isReject) {
            if (apprId.equals("1")) {
                url = Api.APPROVAL_TURNED_DOWN_NOTICES;

            }
            if (apprId.equals("2")) {
                url = Api.APPROVAL_TURNED_DOWN_OFFICE;

            }
            if (apprId.equals("3")) {
                url = Api.APPROVAL_TURNED_DOWN_OFFWORK;
            }
            if (apprId.equals("4")) {
                url = Api.APPROVAL_TURNED_DOWN_TRIP;
            }
            if (apprId.equals("5")) {
                url = Api.APPROVAL_REJECTOVER_TIME;
            }
            if (apprId.equals("6")) {
                url = Api.APPROVAL_TURNEDDOWN_BUSINESS;
            }

        } else {

            if (apprId.equals("1")) {
                url = Api.APPROVAL_ACCESS_NOTICE;

            }
            if (apprId.equals("2")) {
                url = Api.APPROVAL_ACCESS_OFFICE;

            }
            if (apprId.equals("3")) {
                url = Api.APPROVAL_ACCESS_OFFWORK;
            }
            if (apprId.equals("4")) {
                url = Api.APPROVAL_ACCESS_TRIP;
            }
            if (apprId.equals("5")) {
                url = Api.APPROVAL_ACCESSOVER_TIME;
            }
            if (apprId.equals("6")) {
                url = Api.APPROVAL_ACCESS_BUSINESS;
            }
        }
        mKjHttp.post(url, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("onSuccess-->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if ((Api.getCode(jo) == Api.RESPONSES_CODE_OK)) {
                        mView.replySuccess();
                    } else if ((Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL)) {
                        mView.uidNull(Api.getCode(jo));
                    } else {
                        mView.replyResponseOther(Api.getInfo(jo));
                    }
                } catch (JSONException e) {
                    LogUtils.e(e);
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
                mView.replyFailed(errorNo, strMsg);
            }
        });
    }

}
