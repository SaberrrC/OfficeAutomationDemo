package com.shanlinjinrong.oa.ui.activity.home.workreport.contract;

import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.model.selectContacts.Group;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by 丁 on 2017/8/21.
 * 选择人员页面接口
 */
public interface SelectContactActivityContract {
    interface View extends BaseView {
        void loadDataSuccess(ArrayList<Group> groups, Child selectChild);

        void loadDataFailed(int errCode, String errMsg);

        void loadDataFinish();

        void loadDataEmpty();
    }

    interface Presenter extends BasePresenter<View> {
        void loadData(String departmentId, String searchName, String selectChildId);
    }
}
