package com.shanlinjinrong.oa.ui.activity.home.workreport.contract;

import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.MyLaunchReportItem;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by 丁 on 2017/8/21.
 * 我发起的接口
 */
public interface MyLaunchWorkReportContract {
    interface View extends BaseView {

        void loadDataSuccess(List<MyLaunchReportItem> reports, int pageNum, int pageSize, boolean hasNextPage, boolean isLoadMore);

        void loadDataFailed(int errCode, String errMsg);

        void loadDataFinish();

        void loadDataEmpty();
    }

    interface Presenter extends BasePresenter<View> {
        void loadData(int reportType, int pageSize, int pageNum, int timeType, boolean isLoadMore);
    }
}
