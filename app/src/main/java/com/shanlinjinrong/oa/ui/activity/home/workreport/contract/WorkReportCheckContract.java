package com.shanlinjinrong.oa.ui.activity.home.workreport.contract;

import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.CheckReportItem;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by 丁 on 2017/8/21.
 * 汇报审核接口
 */
public interface WorkReportCheckContract {
    interface View extends BaseView {

        void loadDataSuccess(List<CheckReportItem> reports, int pageNum, boolean hasNextPage, boolean isLoadMore);

        void loadDataFailed(int errCode, String errMsg);

        void loadDataFinish();

        void loadDataEmpty();

        void rejectReportSuccess(int position);

        void rejectReportFailed(int errCode, String errMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void loadData(int reportType, int pageSize, int pageNum, int timeType, int reportStatus, boolean isLoadMore);

        void rejectReport(int reportType, int dailyId, int position);
    }
}
