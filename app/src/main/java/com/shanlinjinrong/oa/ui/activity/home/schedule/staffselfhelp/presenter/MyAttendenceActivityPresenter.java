package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;

import android.content.Context;
import android.util.Log;

import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.MyAttendenceActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class MyAttendenceActivityPresenter extends HttpPresenter<MyAttendenceActivityContract.View> implements MyAttendenceActivityContract.Presenter {

    @Inject
    public MyAttendenceActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void sendData(Context context) {
        HttpParams params = new HttpParams();
        String privateUid = AppConfig.getAppConfig(context).getPrivateUid();
        params.put("begindate","2017-10");
//        params.put("userid",privateUid);
//        params.putJsonParams();u
        mKjHttp.get("nchrCardRecord/getCardRecord ",params,new HttpCallBack(){

            @Override
            public void onSuccess(String str) {
                Log.i("haha",str);
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
