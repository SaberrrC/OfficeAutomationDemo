package com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract;

import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingSearchResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 相关接口
 */
public interface UpcomingTasksContract {
    interface View extends BaseView {
        void onGetApproveDataSuccess(UpcomingTaskItemBean bean);

        void onGetApproveDataFailure(int errorNo, String strMsg);

        void onSearchSuccess(UpcomingSearchResultBean bean);
    }

    interface Presenter extends BasePresenter<View> {
        void getApproveData(String approveState, String billType, String pageNum, String pageSize, String time);

        void getSelectData(String privateCode, String noCheck, String pageNum, String pageSize, String time, String billType, String userName);
    }
}
