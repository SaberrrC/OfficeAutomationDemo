package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract;


import android.content.Context;

import com.example.retrofit.model.responsebody.QueryPayResponse;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.bean.PayQueryDataBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

public interface PayQueryContract {

    interface View extends BaseView {

        void showLoading();

        void payQueryInfoSuccess(QueryPayResponse bean);

        void payQueryInfoFailed(int errCode, String msg);

        void payQueryInfoFinish();
    }

    interface Presenter extends BasePresenter<PayQueryContract.View> {

        void payQueryInfo(String date);
    }
}
