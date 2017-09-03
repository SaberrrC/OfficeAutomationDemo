package com.shanlinjinrong.oa.ui.activity.home.approval.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.json.JSONArray;

/**
 * Created by 丁 on 2017/9/1.
 */
public interface ApplyForOfficeSuppliesContract {
    interface View extends BaseView {
        void loadDataSuccess(JSONArray json);

        void loadDataFailed(int errorNo, String strMsg);

        void submitSuccess();

        void submitFailed(int errorNo, String strMsg);

        void submitFinish();

        void submitOther(String msg);
    }

    interface Presenter extends BasePresenter<View> {
        void loadAdmin(String apprId, String isLeader, String oid);//加载

        void submitData(String articleName, String applyTime);
    }
}
