package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.model.CommonRequestBean;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract.CalendarRedactActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

public class CalendarRedactActivityPresenter extends HttpPresenter<CalendarRedactActivityContract.View> implements CalendarRedactActivityContract.Presenter {

    @Inject
    public CalendarRedactActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    /**
     * 添加日程
     */
    @Override
    public void addCalendarSchedule(String schedule) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        httpParams.putJsonParams(schedule);
        mKjHttp.jsonPost("schedule/insertSchedule", httpParams, new HttpCallBack() {

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
                super.onSuccess(t);
                try {
                    CommonRequestBean bean = new Gson().fromJson(t, new TypeToken<CommonRequestBean>() {
                    }.getType());
                    if (mView != null) {
                        mView.hideLoading();
                        switch (bean.getCode()) {
                            case ApiJava.REQUEST_CODE_OK:
                                mView.addCalendarScheduleSuccess();
                                break;
                            default:
                                mView.addCalendarScheduleFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
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
                        mView.addCalendarScheduleFailure(errorNo, strMsg);
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

    /**
     * 删除日程
     */
    @Override
    public void deleteCalendarSchedule(int calendarId) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        httpParams.put("id", calendarId);

        mKjHttp.post("schedule/deleteSchedule", httpParams, new HttpCallBack() {
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
                super.onSuccess(t);
                try {
                    CommonRequestBean bean = new Gson().fromJson(t, new TypeToken<CommonRequestBean>() {
                    }.getType());
                    if (mView != null) {
                        mView.hideLoading();
                        switch (bean.getCode()) {
                            case ApiJava.REQUEST_CODE_OK:
                                mView.deleteCalendarScheduleSuccess();
                                break;
                            default:
                                mView.addCalendarScheduleFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
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
                        mView.deleteCalendarScheduleFailure(errorNo, strMsg);
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

    /**
     * 更新日程
     */
    @Override
    public void updateCalendarSchedule(String schedule) {
        mKjHttp.cleanCache();
        HttpParams httpParams = new HttpParams();
        httpParams.putJsonParams(schedule);
        mKjHttp.jsonPost("schedule/updateSchedule", httpParams, new HttpCallBack() {
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
                super.onSuccess(t);
                try {
                    CommonRequestBean bean = new Gson().fromJson(t, new TypeToken<CommonRequestBean>() {
                    }.getType());
                    if (mView != null) {
                        switch (bean.getCode()) {
                            case ApiJava.REQUEST_CODE_OK:
                                mView.updateCalendarScheduleSuccess();
                                break;
                            default:
                                mView.updateCalendarScheduleFailure(Integer.parseInt(bean.getCode()), bean.getMessage());
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
                        mView.updateCalendarScheduleFailure(errorNo, strMsg);
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
