package com.shanlinjinrong.oa.ui.activity.contracts.contract;

import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by 丁 on 2017/8/21.
 */

public interface ContractActivityContract {
    interface View extends BaseView {
        void loadDataSuccess(List<Contacts> contacts); //加载联系人成功

        void loadDataFailed(int errorNo, String strMsg); //加载联系人失败

        void loadDataEmpty(); //加载联系人为空

        void loadDataFinish(); //加载操作完成

        void loadDataTokenNoMatch(int code);
    }

    interface Presenter extends BasePresenter<View> {
        List<Contacts> loadData(String departmentId); //加载通讯录数据
    }
}
