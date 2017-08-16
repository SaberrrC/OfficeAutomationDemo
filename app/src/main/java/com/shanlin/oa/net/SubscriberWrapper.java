package com.shanlin.oa.net;

import android.content.Intent;
import android.text.TextUtils;

import com.shanlin.oa.ui.activity.LoginActivity;
import com.shanlin.oa.utils.LogUtils;

import org.reactivestreams.Subscription;

import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.NonNull;
import retrofit2.HttpException;

/**
 * Created by lvdinghao on 14/06/2017.
 */
public class SubscriberWrapper<T extends BaseBean> implements FlowableSubscriber<T> {

    public static final String ERR_NETWORK_MSG  = "网络异常，请重试";
    public static String ERR_NETWORK_CODE = "0";

    private CallBackListener<T> callBackListener;
    public SubscriberWrapper(CallBackListener<T> callBackListener) {
        this.callBackListener = callBackListener;
    }

    @Override
    public void onNext(T t) {
        String code = t.getCode();
        String msg = t.getMessage();
        if (TextUtils.equals(code, "401")) {//token失效
            AutoStoreApplication.isLogin = false;
            if (!jumpLogin) {
                return;
            }
            toLoginActivity();
            return;
        }
        if (!TextUtils.equals(code, "200")) {
            callBackListener.onFailed(null, code, msg);
            return;
        }
        callBackListener.onSuccess(code, t, msg);
    }

    public boolean jumpLogin = true;

    public void setJumpLogin(boolean isjumpLogin) {
        jumpLogin = isjumpLogin;
    }


    private String key;
    private String value;

    private void toLoginActivity() {
        ToastUtils.showToast("用户已在别处登录，请从新登录");
        Intent toLoginActivity = new Intent(AutoStoreApplication.getApp(), LoginActivity.class);
        if (TextUtils.isEmpty(key) && TextUtils.isEmpty(value)) {
            toLoginActivity.putExtra(key, value);
        }
        toLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AutoStoreApplication.getApp().startActivity(toLoginActivity);
    }



    @Override
    public void onError(Throwable ex) {
        if (ex instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) ex;
            ERR_NETWORK_CODE = httpEx.code() + "";
        }
        if (ex instanceof HttpException) {
            HttpException httpEx = (HttpException) ex;
            ERR_NETWORK_CODE = httpEx.code() + "";
        }
        if (!CommonUtils.checkNet()) {
            callBackListener.onFailed(ex, ERR_NETWORK_CODE, "网络错误");
            return;
        }
        LogUtils.d(ERR_NETWORK_MSG);
        callBackListener.onFailed(ex, ERR_NETWORK_CODE, ERR_NETWORK_MSG);
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(@NonNull Subscription s) {
        s.request(Long.MAX_VALUE);//表示请求的数量
    }

    public interface CallBackListener<T> {
          void onSuccess(String code, T data, String msg);

          void onFailed(Throwable ex, String code, String msg);
    }
}

