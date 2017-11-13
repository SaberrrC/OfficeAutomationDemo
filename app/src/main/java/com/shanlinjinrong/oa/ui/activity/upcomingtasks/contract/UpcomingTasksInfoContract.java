package com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract;

import com.example.retrofit.model.responsebody.ApporveBodyItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.AgreeDisagreeResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.DeleteBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.TackBackResultBean;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by 丁 on 2017/8/19.
 * 相关接口
 */
public interface UpcomingTasksInfoContract {
    interface View extends BaseView {
        void onGetApproveInfoSuccess(String json);

        void onGetApproveInfoFailure(String errorNo, String strMsg);

        void onTackBackSuccess(TackBackResultBean tackBackResultBean);

        void onApproveSuccess(AgreeDisagreeResultBean resultBean);

        void onApproveFailure(int errorNo, String strMsg);

        void onDeleteSuccess(DeleteBean bean);

        void onDeleteFailure(String s, String strMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void getInfoData(String billType, String billCode);

        void postTackBack(String billCode, String billType);

        void postApproval(List<ApporveBodyItemBean> list);

        void getDelete(String billCode, String billType);
    }
}
