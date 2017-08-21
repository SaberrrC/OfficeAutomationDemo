package com.shanlin.oa.ui.fragment.contract;

import com.shanlin.oa.model.Contacts;
import com.shanlin.oa.model.User;
import com.shanlin.oa.ui.base.BasePresenter;
import com.shanlin.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by 丁 on 2017/8/21.
 */

public interface TabContractsFragmentContract {
    interface View extends BaseView {
        void autoSearchSuccess(List<User> users);//自动搜索联系人成功

        void autoSearchFailed(int errCode, String errMsg);//自动搜索联系人失败

        void autoSearchOther(String msg);//自动搜索联系人其他结果

        void autoSearchFinish();//请求数据结束

        void loadDataStart();

        void loadDataSuccess(List<Contacts> contracts);

        void loadDataFailed(int code, String msg);

        void loadDataEmpty();

        void loadDataFinish();

        void loadDataTokenNoMatch(int code);

    }

    interface Presenter extends BasePresenter<View> {
        void autoSearch(String departmentId, String isleader, String name);

        void loadData();
    }
}
