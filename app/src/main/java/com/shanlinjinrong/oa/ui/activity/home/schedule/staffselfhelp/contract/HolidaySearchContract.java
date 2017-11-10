package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract;

import com.example.retrofit.model.responsebody.HolidaySearchResponse;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

public interface HolidaySearchContract {

    interface View extends BaseView {

        void showLoading();

        void getDataSuccess(List<HolidaySearchResponse> myAttandanceResponse);

        void getDataFailed(int errCode, String msg);

        void getDataFinish();
    }

    interface Presenter extends BasePresenter<HolidaySearchContract.View> {

        void getData(String year, String type);

    }
}
