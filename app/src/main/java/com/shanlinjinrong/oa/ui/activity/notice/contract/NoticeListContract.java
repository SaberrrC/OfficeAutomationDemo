package com.shanlinjinrong.oa.ui.activity.notice.contract;

import com.shanlinjinrong.oa.model.Notice;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by 丁 on 2017/8/19.
 * 找回密码接口
 */
public interface NoticeListContract {
    interface View extends BaseView {
        void loadDataSuccess(List<Notice> listNotices, boolean loadMore); // 成功

        void loadDataFinish(); //请求结束

        void loadDataFailed(int errCode, String msg); //失败

        void loadDataEmpty(); //加载数据为空

        void loadDataNoMore(); //没有更多数据了

    }

    interface Presenter extends BasePresenter<View> {
        void loadData(String departmentId, int limit, int currentPage, boolean loadMore); //加载消息列表
    }
}
