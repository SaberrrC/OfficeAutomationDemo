package com.shanlin.oa.ui.activity.home.workreport.contract;

import com.shanlin.oa.model.selectContacts.Group;
import com.shanlin.oa.ui.base.BasePresenter;
import com.shanlin.oa.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by 丁 on 2017/8/21.
 * 选择人员页面接口
 */
public interface SelectContactActivityContract {
    interface View extends BaseView {
        void loadDataSuccess(ArrayList<Group> groups);

        void loadDataFailed(int errCode, String errMsg);

        void loadDataFinish();

        void loadDataEmpty();
    }

    interface Presenter extends BasePresenter<View> {
        void loadData(String departmentId, String searchName);
    }
}
