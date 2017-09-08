package com.shanlinjinrong.oa.ui.activity.home.approval.contract;

import com.shanlinjinrong.oa.model.approval.Approval;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by 丁 on 2017/9/1.
 */
public interface ApprovalListContract {
    interface View extends BaseView {
        void loadDataSuccess(ArrayList<Approval> listApproval, boolean isMore);

        void loadDataFailed(int errorNo, String strMsg);

        void loadDataResponseEmpty();

        void loadDataEmpty();

        void readSuccess();

        void readFailed(int errorNo, String strMsg);

        void requestFinish();

    }

    interface Presenter extends BasePresenter<View> {

        void loadData(int currentState,int page, int limit, String time, String where, boolean isMore);//加载

        void readPush(String departmentId, String pid);
    }
}
