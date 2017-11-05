package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;

import android.content.Context;
import android.util.Log;

import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.net.ApiException;
import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.MyAttendenceActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.Request;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class MyAttendenceActivityPresenter extends HttpPresenter<MyAttendenceActivityContract.View> implements MyAttendenceActivityContract.Presenter {

    @Inject
    public MyAttendenceActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void sendData(String userId,String year,String month , Context context) {
        HashMap<String ,String> map = new HashMap<>();
        map.put("userCode",userId);
        map.put("date",year+"-"+month);
        HttpMethods.getInstance().getMyAttantanceData(map, new Subscriber<MyAttandanceResponse>() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

                if(e instanceof ApiException){
                    ApiException baseException = (ApiException)e;
                    String code = baseException.getCode();
                    String message = baseException.getMessage();
                    mView.sendDataFailed(code,message);
                }else {
                    mView.sendDataFailed("555","请检查网络！");
                }
            }

            @Override
            public void onNext(MyAttandanceResponse myAttandanceResponse) {
                mView.sendDataSuccess(myAttandanceResponse);
                Log.i("hahaha",myAttandanceResponse.toString());
            }
        });
    }



}
