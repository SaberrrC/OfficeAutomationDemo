package com.shanlinjinrong.oa.ui.activity.my.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 意见反馈接口
 */
public interface FeedbackActivityContract {
     interface View extends BaseView {
        void feedbackSuccess(); // 成功

        void feedbackFinish(); //请求结束

        void feedbackFailed(int errorNo, String strMsg); //失败
    }

     interface Presenter extends BasePresenter<View> {
        void sendFeedback(String departmentId, String content); //意见反馈
    }
}
