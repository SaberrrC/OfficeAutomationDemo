package com.shanlinjinrong.oa.ui.activity.home.schedule.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.json.JSONObject;

/**
 * Created by 丁 on 2017/8/24.
 * 日程安排页面
 */
public interface ScheduleActivityContract {
    interface View extends BaseView {

        void loadDataSuccess(JSONObject jsonData);

        void loadDataFailed(int errCode, String msg);

        void loadDataFinish();
    }

    interface Presenter extends BasePresenter<View> {
        void loadData(String date);
    }
}
