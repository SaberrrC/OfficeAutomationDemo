package com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract;

import com.example.retrofit.model.responsebody.ApporveBodyItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.AgreeDisagreeResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingSearchResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by 丁 on 2017/8/19.
 * 相关接口
 */
public interface UpcomingTasksContract {
    interface View extends BaseView {
        void onGetApproveDataSuccess(UpcomingTaskItemBean bean);

        void onGetApproveDataFailure(int errorNo, String strMsg);

        void onSearchSuccess(UpcomingSearchResultBean bean);

        void onApproveFailure(int errorNo, String strMsg);

        void onApproveSuccess(AgreeDisagreeResultBean resultBean, List<ApporveBodyItemBean> list);

        void onSearchFailure(int i, String message);

//        void onOfficeSuppliesApproveSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void getApproveData(String approveState, String billType, String pageNum, String pageSize, String time);

        void getSelectData(String privateCode, String noCheck, String pageNum, String pageSize, String time, String billType, String userName);

        void postAgreeDisagree(List<ApporveBodyItemBean> approveBeanList);

        void getOfficeSuppliesApproveData(String timeCode,String loableStatus);
    }
}
