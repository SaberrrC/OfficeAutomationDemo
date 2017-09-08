package com.shanlinjinrong.oa.ui.activity.my.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.my.contract.FeedbackActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class FeedbackActivityPresenter extends HttpPresenter<FeedbackActivityContract.View> implements FeedbackActivityContract.Presenter {

    @Inject
    public FeedbackActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void sendFeedback(String departmentId, String content) {
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        params.put("content", content);
        mKjHttp.post(Api.FEEDBACK, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                mView.feedbackFinish();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.d("data-->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            mView.feedbackSuccess();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            mView.uidNull(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    LogUtils.e(e.toString());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.feedbackFailed(errorNo, strMsg);
            }
        });
    }
}
