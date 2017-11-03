package com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter;

import com.example.retrofit.net.HttpMethods;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class UpcomingTasksPresenter extends HttpPresenter<UpcomingTasksContract.View> implements UpcomingTasksContract.Presenter {

    @Inject
    public UpcomingTasksPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void getData() {
        Map<String, String> map = new HashMap<>();
        
        HttpMethods.getInstance().getQueryUserSetting1(map, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        });
    }
}
