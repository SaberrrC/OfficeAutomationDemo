package com.shanlinjinrong.oa.ui.activity.my.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hyphenate.easeui.UserDetailsBean;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.my.contract.UserInfoActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 丁 on 2017/8/19.
 * userinfo presenter
 */
public class UserInfoActivityPresenter extends HttpPresenter<UserInfoActivityContract.View> implements UserInfoActivityContract.Presenter {

    @Inject
    public UserInfoActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void upLoadPortrait(String departmentId, String portrait, File file) {
        mKjHttp.cleanCache();
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        params.put("file", file);

        //        params.put("file", portrait);
        //        params.put("portrait", file);
        mKjHttp.post(Api.PERSON_UPLOAD, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            if (mView != null)
                                mView.upLoadSuccess(Api.getDataToJSONObject(jo).get("portrait") + "");
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            if (mView != null)
                                mView.uidNull(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e("上传图片失败" + errorNo + strMsg);
                if (mView != null)
                    mView.upLoadFailed(errorNo, strMsg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mView != null)
                    mView.upLoadFinish();
            }
        });
    }

    @Override
    public void queryUserInfo(String userCode) {
        HttpParams httpParams = new HttpParams();
        mKjHttp.phpJsonGet(Api.SEARCH_USER_DETAILS + "?code=" + userCode, httpParams, new HttpCallBack() {

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                UserDetailsBean userDetailsBean = new Gson().fromJson(t, UserDetailsBean.class);
                if (userDetailsBean != null) {
                    try {
                        switch (userDetailsBean.getCode()) {
                            case 200:
                                Observable.create(e -> {
                                    SharedPreferences.Editor editor = AppManager.mContext.getSharedPreferences(AppConfig.APP_CONFIG, Context.MODE_PRIVATE).edit();
                                    editor.putString(AppConfig.PREF_KEY_CODE, userDetailsBean.getData().get(0).getCode());
                                    editor.putString(AppConfig.PREF_KEY_USER_EMAIL, userDetailsBean.getData().get(0).getEmail());
                                    editor.putString(AppConfig.PREF_KEY_USERNAME, userDetailsBean.getData().get(0).getUsername());
                                    editor.putString(AppConfig.PREF_KEY_SEX, userDetailsBean.getData().get(0).getSex());
                                    editor.putString(AppConfig.PREF_KEY_PHONE, userDetailsBean.getData().get(0).getPhone());
                                    editor.putString(AppConfig.PREF_KEY_DEPARTMENT_NAME, userDetailsBean.getData().get(0).getOrgan());
                                    editor.putString(AppConfig.PREF_KEY_PORTRAITS, Constants.PHPSLPicBaseUrl + userDetailsBean.getData().get(0).getImg());
                                    editor.putString(AppConfig.PREF_KEY_POST_NAME, userDetailsBean.getData().get(0).getPostname());
                                    editor.apply();
                                    e.onComplete();
                                }).observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(o -> {
                                        }, Throwable::printStackTrace, () -> {
                                            try {
                                                if (mView != null) {
                                                    mView.queryUserInfoSuccess(userDetailsBean);
                                                }
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        });
                                break;
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
