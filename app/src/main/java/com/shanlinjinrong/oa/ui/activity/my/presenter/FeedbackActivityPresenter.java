package com.shanlinjinrong.oa.ui.activity.my.presenter;

import android.support.v4.app.NavUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.CommonRequestBean;
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
    public void sendFeedback(String content) {
        HttpParams params = new HttpParams();
        params.put("content", content);
        mKjHttp.post(Api.FEEDBACK, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null)
                        mView.feedbackFinish();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.d("data-->" + t);
                try {
                    CommonRequestBean requestStatus = new Gson().fromJson(t, new TypeToken<CommonRequestBean>() {}.getType());
                    switch (requestStatus.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            if (mView != null)
                                mView.feedbackSuccess();
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            if (mView != null)
                                mView.uidNull(requestStatus.getCode());
                            break;
                        default:
                            if (mView != null)
                                mView.feedbackFailed(requestStatus.getMessage());
                            break;
                    }
                } catch (Throwable e) {
                    LogUtils.e(e.toString());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.feedbackFailed("反馈失败");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
