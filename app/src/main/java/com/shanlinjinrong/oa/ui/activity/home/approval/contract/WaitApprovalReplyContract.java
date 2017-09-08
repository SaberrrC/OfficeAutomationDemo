package com.shanlinjinrong.oa.ui.activity.home.approval.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/9/1.
 */
public interface WaitApprovalReplyContract {
    interface View extends BaseView {
        void replySuccess();

        void replyFailed(int errorNo, String strMsg);

        void replyResponseOther(String strMsg);

        void requestFinish();

    }

    interface Presenter extends BasePresenter<View> {
        void finishReply(boolean isReject, String apprId, String oaId, String reply);//加载
    }
}
