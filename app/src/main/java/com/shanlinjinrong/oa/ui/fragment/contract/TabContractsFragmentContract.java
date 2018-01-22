package com.shanlinjinrong.oa.ui.fragment.contract;

import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;
import java.util.List;

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

        void loadDataTokenNoMatch(String code);

    }

    interface Presenter extends BasePresenter<View> {
        void autoSearch(String name);

        void loadData(String orgId);
    }
}
