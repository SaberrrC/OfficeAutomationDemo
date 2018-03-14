package com.shanlinjinrong.oa.ui.activity.message.presenter;

import com.google.gson.Gson;
import com.hyphenate.easeui.utils.AESUtils;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.main.bean.UserDetailsBean;
import com.shanlinjinrong.oa.ui.activity.message.contract.CallActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.DateUtils;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 作者：王凤旭
 * 时间：2017/11/30
 * 描述：
 */

public class CallActivityPresenter extends HttpPresenter<CallActivityContract.View> implements CallActivityContract.Presenter {
    @Inject
    public CallActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void searchUserDetails(String code) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        httpParams.put("codeList", code);
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("codeList", code);
            String time = String.valueOf(DateUtils.dateToLong(DateUtils.getCurrentDate(pattern), pattern)).substring(0, 13);
            jsonObject.put("time", time);
            httpParams.putHeaders("sign", AESUtils.Encrypt(jsonObject.toString()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mKjHttp.post(ApiJava.CODE_SEARCH_USER_DETAILS, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    UserDetailsBean userDetailsBean = new Gson().fromJson(t, UserDetailsBean.class);
                    if (userDetailsBean != null) {
                        switch (userDetailsBean.getCode()) {
                            case ApiJava.REQUEST_CODE_OK:
                                for (int i = 0; i < userDetailsBean.getData().size(); i++) {
                                    if (mView != null)
                                        mView.searchUserDetailsSuccess(userDetailsBean.getData().get(i));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
}
