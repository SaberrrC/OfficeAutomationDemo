package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.CommonRequestBean;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.CalendarScheduleContentBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract.MonthlyCalendarFragmentContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/20
 * 功能描述：
 */

public class MonthlyCalendarFragmentPresenter extends HttpPresenter<MonthlyCalendarFragmentContract.View> implements MonthlyCalendarFragmentContract.Presenter {

    @Inject
    public MonthlyCalendarFragmentPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    /**
     * 根据日期查询日程详情
     */
    @Override
    public void getScheduleByDate(String startDate, String endDate) {
        mKjHttp.cleanCache();

        mKjHttp.get("schedule/getScheduleByDate" + "?startDate=" + startDate + "&endDate=" + endDate, new HttpParams(), new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
                try {
                    if (mView != null) {
                        mView.showLoading();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String t) {
                Log.e("----Success----", t);
                super.onSuccess(t);
                try {
                    CalendarScheduleContentBean bean = new Gson().fromJson(t, new TypeToken<CalendarScheduleContentBean>() {
                    }.getType());
                    switch (bean.getCode()) {
                        case ApiJava.REQUEST_CODE_OK:
                            mView.hideLoading();
                            mView.GetScheduleByDateSuccess(bean);
                            break;
                        case ApiJava.REQUEST_NO_RESULT:
                            break;
                        default:

                            mView.GetScheduleByDateFailure(Integer.parseInt(bean.getCode()),bean.getMessage());
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                Log.e("----Failure----", strMsg);
                try {
                    if (mView != null) {
                        mView.hideLoading();
                        mView.GetScheduleByDateFailure(errorNo, strMsg);
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
    }

}
