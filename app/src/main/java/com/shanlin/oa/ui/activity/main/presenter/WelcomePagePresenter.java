package com.shanlin.oa.ui.activity.main.presenter;

import com.shanlin.oa.common.Api;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;
import com.shanlin.oa.ui.activity.main.contract.WelcomePageContract;
import com.shanlin.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/18.
 */

public class WelcomePagePresenter implements WelcomePageContract.Presenter {

    private WelcomePageContract.View mView;
    private KJHttp mKjHttp;

    @Inject
    public WelcomePagePresenter(WelcomePageContract.View mView, KJHttp mKjHttp) {
        this.mView = mView;
        this.mKjHttp = mKjHttp;
    }

    /**
     * 登录超时判断
     */
    @Override
    public void checkoutTimeOut() {
        HttpParams params = new HttpParams();
        final String uid = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_USER_UID);
        final String token = AppConfig.getAppConfig(AppManager.mContext)
                .get(AppConfig.PREF_KEY_TOKEN);
        params.put("uid", uid);
        params.put("token", token);

        mKjHttp.post(AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.BASE_URL) + Api.SITE_TIMEOUT, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                mView.loadFinish();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("---------------成功" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        JSONObject data = jo.getJSONObject("data");
                        String timeout = data.getString("timeout");
                        boolean isTimeOut = !timeout.equals("1");
                        mView.checkTimeOutSuccess(isTimeOut, uid);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.checkTimeOutFailed(token);
            }
        });
    }

    /**
     * 请求接口的域名
     */
    @Override
    public void getDomain() {
        HttpParams params = new HttpParams();
        params.put("cid", Constants.GETBASEURLCID);
        mKjHttp.post(Api.ORIGINAL_URL, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        JSONObject data = Api.getDataToJSONObject(jo);
                        String url = data.getString("domain");
                        AppConfig.getAppConfig(AppManager.mContext).set(AppConfig.BASE_URL, url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                // TODO: 2017/8/15 域名请求失败，不需要给一个默认的域名吗？
                AppConfig.getAppConfig(AppManager.mContext).set(AppConfig.BASE_URL, "http://api.sl.s1.zhitongoa.com/");
                LogUtils.e("onFailure" + strMsg);
            }
        });
    }


}
