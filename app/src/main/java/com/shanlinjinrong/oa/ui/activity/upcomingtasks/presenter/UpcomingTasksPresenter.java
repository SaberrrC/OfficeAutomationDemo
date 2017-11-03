package com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

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
    public void getApproveData(String approveState, String billType, String pageNum, String pageSize, String time) {
        //        Map<String, String> map = new HashMap<>();
        //        map.put("approveState", String.valueOf(approveState));
        //        map.put("billType", String.valueOf(billType));
        //        map.put("pageNum", String.valueOf(pageNum));
        //        map.put("pageSize", String.valueOf(pageSize));
        //        map.put("time", String.valueOf(time));
        //        HttpMethods.getInstance().getApproveData(map, new Subscriber<String>() {
        //            @Override
        //            public void onCompleted() {
        //
        //            }
        //
        //            @Override
        //            public void onError(Throwable e) {
        //
        //            }
        //
        //            @Override
        //            public void onNext(String s) {
        //
        //            }
        //        });
        HttpParams httpParams = new HttpParams();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiJava.MYAPPLY_QUERY_APPROVE);
        if (!TextUtils.isEmpty(pageNum)) {
            stringBuilder.append("?" + "pageNum=" + pageNum);
        }
        if (!TextUtils.isEmpty(pageSize)) {
            stringBuilder.append("&" + "pageSize=" + pageSize);
        }
        if (!TextUtils.isEmpty(approveState)) {
            stringBuilder.append("&" + "approveState=" + approveState);
        }
        if (!TextUtils.isEmpty(billType)) {
            stringBuilder.append("&" + "billType=" + billType);
        }
        if (!TextUtils.isEmpty(time)) {
            stringBuilder.append("&" + "time=" + time);
        }
        mKjHttp.jsonGet(stringBuilder.toString(), httpParams, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                UpcomingTaskItemBean bean = new Gson().fromJson(t, UpcomingTaskItemBean.class);
                mView.onGetApproveDataSuccess(bean);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.onGetApproveDataFailure(errorNo, strMsg);
            }
        });
    }
}
