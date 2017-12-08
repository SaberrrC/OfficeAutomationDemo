package com.shanlinjinrong.oa.ui.activity.message.presenter;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.net.remote.JavaApi;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatDetailsActivity;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatDetailsContact;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class EaseChatDetailsPresenter extends HttpPresenter<EaseChatDetailsContact.View> implements EaseChatDetailsContact.Presenter {

    @Inject
    public EaseChatDetailsPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void searchUserListInfo(String codeList) {
        HashMap<String, String> map = new HashMap<>();
        map.put("codeList", "010110027");

        HttpMethods.getInstance().queryUserListInfo(map, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                LogUtils.e(s);
                LogUtils.e(s);
            }
        });
//
//        mKjHttp.cleanCache();
//        HttpParams httpParams = new HttpParams();
//
//        httpParams.put("codeList","010110027");
//
//        mKjHttp.jsonPost(ApiJava.QUERY_USER_LIST_INFO, httpParams, new HttpCallBack() {
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//
//                LogUtils.e(t);
//            }
//
//            @Override
//            public void onFailure(int errorNo, String strMsg) {
//                super.onFailure(errorNo, strMsg);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//            }
//        });
    }
}
