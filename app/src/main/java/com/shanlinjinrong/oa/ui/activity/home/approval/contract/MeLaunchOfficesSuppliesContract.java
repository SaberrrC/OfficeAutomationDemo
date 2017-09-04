package com.shanlinjinrong.oa.ui.activity.home.approval.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import org.json.JSONObject;

/**
 * Created by 丁 on 2017/9/1.
 */
public interface MeLaunchOfficesSuppliesContract {
    interface View extends BaseView {
        void loadDataSuccess(JSONObject data);

        void loadDataFailed(int errorNo, String strMsg);


        void loadDataEmpty();

        void requestFinish();

    }

    interface Presenter extends BasePresenter<View> {
        void loadData(String apprId, String oalId);//加载
    }
}
