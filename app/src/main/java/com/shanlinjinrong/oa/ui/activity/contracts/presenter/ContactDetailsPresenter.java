package com.shanlinjinrong.oa.ui.activity.contracts.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.contracts.bean.ContactDetailsBean;
import com.shanlinjinrong.oa.ui.activity.contracts.contract.ContactDetailsContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：王凤旭
 * 时间：2017/11/30
 * 描述：
 */

public class ContactDetailsPresenter extends HttpPresenter<ContactDetailsContract.View> implements ContactDetailsContract.Presenter {
    @Inject
    public ContactDetailsPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    String uid = "";

    @Override
    public void searchUserDetails(int code) {
        mKjHttp.cleanCache();
//        Observable.create((ObservableOnSubscribe<String>) e -> {
//            uid = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getUid("sl_" + code);
//            e.onComplete();
//        }).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(s -> {
//                }, Throwable::printStackTrace, new Action() {
//                    @Override
//                    public void run() throws Exception {
        HttpParams httpParams = new HttpParams();
        mKjHttp.get(ApiJava.ID_SEARCH_USER_DETAILS + "?uid=" + code, httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null)
                        mView.showLoading();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    ContactDetailsBean contactDetailsBean = new Gson().fromJson(t, new TypeToken<ContactDetailsBean>() {
                    }.getType());
                    if (contactDetailsBean != null) {
                        switch (contactDetailsBean.getCode()) {
                            case ApiJava.REQUEST_CODE_OK:
                                mView.searchUserDetailsSuccess(contactDetailsBean.getData());
                                break;
                            case ApiJava.REQUEST_TOKEN_NOT_EXIST:
                            case ApiJava.REQUEST_TOKEN_OUT_TIME:
                            case ApiJava.ERROR_TOKEN:
                                if (mView != null) {
                                    mView.uidNull(contactDetailsBean.getCode());
                                }
                                break;
                            default:
                                if (mView != null) {
                                    mView.searchUserDetailsFailed(0, contactDetailsBean.getMessage());
                                    mView.hideLoading();
                                }
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
                try {
                    if (mView != null) {
                        mView.hideLoading();
                        mView.searchUserDetailsFailed(errorNo, strMsg);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                try {
                    if (mView != null) {
                        mView.hideLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
//                    }
//                });
    }
}
