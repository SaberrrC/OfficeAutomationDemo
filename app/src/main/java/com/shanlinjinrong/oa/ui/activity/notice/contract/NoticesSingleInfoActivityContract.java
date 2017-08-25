package com.shanlinjinrong.oa.ui.activity.notice.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 相关接口
 */
public interface NoticesSingleInfoActivityContract {
    interface View extends BaseView {
        void markSuccess(); // 标记成功
    }

    interface Presenter extends BasePresenter<View> {
        void markReadNotice(String Nid, String departmentId);
    }
}
