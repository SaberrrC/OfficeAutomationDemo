package com.shanlinjinrong.oa.ui.fragment.presenter;

import com.google.gson.Gson;
import com.hyphenate.easeui.utils.AESUtils;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.main.bean.AppVersionBean;
import com.shanlinjinrong.oa.ui.activity.main.contract.TabMeGetVersionInfo;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

public class TabMeGetVersionPresenter extends HttpPresenter<TabMeGetVersionInfo.View> implements TabMeGetVersionInfo.Presenter {
    @Inject
    public TabMeGetVersionPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    /**
     * 获取版本号信息
     */
    @Override
    public void getAppEdition() {
        HttpParams httpParams = new HttpParams();
        try {
            String pattern = "yyyy-MM-dd HH:mm:ss";
            JSONObject jsonObject = new JSONObject();
            String time = String.valueOf(DateUtils.dateToLong(DateUtils.getCurrentDate(pattern), pattern)).substring(0, 13);
            jsonObject.put("time", time);
            httpParams.putHeaders("sign", AESUtils.Encrypt(jsonObject.toString()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mKjHttp.get(ApiJava.APP_GETAPPEDITION, httpParams, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (jo.getString("code")) {
                        case ApiJava.REQUEST_CODE_OK:
                            JSONObject data = jo.getJSONObject("data");
                            if (mView != null) {
                                AppVersionBean mAppVersionBean = new Gson().fromJson(t, AppVersionBean.class);
                                mView.getAppEditionSuccess(mAppVersionBean);
                            }
                            break;
                        case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                        case ApiJava.REQUEST_TOKEN_OUT_TIME:
                        case ApiJava.ERROR_TOKEN:
                            break;
                        default:
                            if (mView != null) {
                            }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try{
                    mView.getAppEdittionFailed(errorNo,strMsg);
                }catch (Throwable e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }
}
