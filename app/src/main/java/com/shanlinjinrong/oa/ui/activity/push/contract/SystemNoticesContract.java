package com.shanlinjinrong.oa.ui.activity.push.contract;

import com.shanlinjinrong.oa.model.SystemNotice;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by 丁 on 2017/8/30.
 * SystemNotices 接口
 */
public interface SystemNoticesContract {
    interface View extends BaseView {
        void loadDataSuccess(List<SystemNotice> pushMessages);//加载成功

        void loadDataEmpty();//加载数据为空

        void loadDataFailed(int errCode, String errMsg);//加载数据为空

        void requestFinish();

        void readSystemNoticesFailed(int errCode, String errMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void readAllSystemNotices(String departmentId); //push信息已阅读

        void loadData(String departmentId);//加载push信息
    }

}
