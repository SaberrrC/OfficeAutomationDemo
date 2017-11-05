package com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract;

import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.TackBackResultBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 相关接口
 */
public interface UpcomingTasksInfoContract {
    interface View extends BaseView {
        void onGetApproveInfoSuccess(String json);

        void onGetApproveInfoFailure(int errorNo, String strMsg);

        void onTackBackSuccess(TackBackResultBean tackBackResultBean);
    }

    interface Presenter extends BasePresenter<View> {
        void getInfoData(String billType, String billCode);

        void postTackBack(String billCode, String billType);
    }
}
