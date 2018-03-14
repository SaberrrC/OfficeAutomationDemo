package com.shanlinjinrong.oa.ui.activity.my.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.retrofit.model.UpLoadPortraitsBean;
import com.example.retrofit.net.HttpMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.UserDetailsBean;
import com.hyphenate.easeui.utils.AESUtils;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.my.contract.UserInfoActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.DateUtils;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * userinfo presenter
 */
public class UserInfoActivityPresenter extends HttpPresenter<UserInfoActivityContract.View> implements UserInfoActivityContract.Presenter {

    @Inject
    public UserInfoActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void upLoadPortrait(File file) {

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        HttpMethods.getInstance().uploadPortraits(body, new Subscriber<UpLoadPortraitsBean>() {
            @Override
            public void onCompleted() {
                try {
                    if (mView != null)
                        mView.upLoadFinish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    if (((HttpException) e).code() == 401) {
                        mView.uidNull(((HttpException) e).code() + "");
                        return;
                    }
                    if (((HttpException) e).code() > 400) {
                        mView.upLoadFailed(((HttpException) e).code(), "服务器异常，请稍后重试！");
                    }
                } else if (e instanceof SocketTimeoutException) {
                    mView.upLoadFailed(-1, "网络不通，请检查网络连接！");
                } else if (e instanceof NullPointerException) {
                    mView.upLoadFailed(-1, "网络不通，请检查网络连接！");
                } else if (e instanceof ConnectException || e instanceof SocketException) {
                    mView.upLoadFailed(-1, "网络不通，请检查网络连接！");
                }

            }

            @Override
            public void onNext(UpLoadPortraitsBean upLoadPortraitsBean) {
                try {
                    if (upLoadPortraitsBean != null) {
                        if (mView != null) {
                            mView.upLoadSuccess(upLoadPortraitsBean.getPortrait());
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    @Override
    public void queryUserInfo() {
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
        mKjHttp.get(ApiJava.ID_SEARCH_USER_DETAILS, httpParams, new HttpCallBack() {

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    mView.uidNull(strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
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
                                    editor.putString(AppConfig.PREF_KEY_PORTRAITS, userInfo.getData().getPortrait());
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
