package com.shanlinjinrong.oa.ui.activity.my.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.UserDetailsBean;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.my.contract.UserInfoActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.Cache;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.CharArrayReader;
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
    public void upLoadPortrait(File file) {
        mKjHttp.cleanCache();
        HttpParams params = new HttpParams();
        params.put("file", file);
        mKjHttp.jsonPost(Api.PERSON_UPLOAD, params, new HttpCallBack() {
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
//                                mView.uidNull(Api.getCode(jo));
                                break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    LogUtils.e("上传图片失败" + errorNo + strMsg);
                    if (mView != null)
                        mView.upLoadFailed(errorNo, "上传头像失败");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null)
                        mView.upLoadFinish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    @Override
    public void queryUserInfo() {
        HttpParams httpParams = new HttpParams();
        mKjHttp.jsonGet(Api.SEARCH_USER_DETAILS, httpParams, new HttpCallBack() {

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    UserDetailsBean userInfo = new Gson().fromJson(t, new TypeToken<UserDetailsBean>() {
                    }.getType());
                    if (userInfo != null) {
                        switch (userInfo.getCode()) {
                            case ApiJava.REQUEST_CODE_OK:
                                Observable.create(e -> {
                                    SharedPreferences.Editor editor = AppManager.mContext.getSharedPreferences(AppConfig.APP_CONFIG, Context.MODE_PRIVATE).edit();
                                    editor.putString(AppConfig.PREF_KEY_CODE, userInfo.getData().getCode());
                                    editor.putString(AppConfig.PREF_KEY_USER_EMAIL, userInfo.getData().getEmail());
                                    editor.putString(AppConfig.PREF_KEY_USERNAME, userInfo.getData().getUsername());
                                    editor.putString(AppConfig.PREF_KEY_SEX, userInfo.getData().getSex());
                                    editor.putString(AppConfig.PREF_KEY_PHONE, userInfo.getData().getPhone());
                                    editor.putString(AppConfig.PREF_KEY_DEPARTMENT_NAME, userInfo.getData().getOrgan());
                                    editor.putString(AppConfig.PREF_KEY_PORTRAITS, Constants.SLPicBaseUrl + userInfo.getData().getPortrait());
                                    editor.putString(AppConfig.PREF_KEY_POST_NAME, userInfo.getData().getPostname())
                                    ;
                                    editor.apply();
                                    e.onComplete();
                                }).observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(o -> {
                                        }, Throwable::printStackTrace, () -> {
                                            try {
                                                if (mView != null) {
                                                    mView.queryUserInfoSuccess(userInfo);
                                                }
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        });
                                break;
                            case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                            case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            case ApiJava.ERROR_TOKEN:
                                if (mView != null)
                                    mView.uidNull(userInfo.getCode());
                                break;
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
