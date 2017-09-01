package com.shanlinjinrong.oa.ui.activity.push.contract;

import com.shanlinjinrong.oa.model.PushMsg;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by 丁 on 2017/8/30.
 */

public interface PushListContract {
    interface View extends BaseView {
        void loadPushMsgSuccess(ArrayList<PushMsg> pushMessages, boolean loadMore);//加载成功

        void loadPushMsgEmpty();//加载数据为空

        void loadPushMsgLimitContentEmpty();//没有更多数据加载

        void loadPushMsgFailed(int errCode, String errMsg);//加载数据为空

        void requestFinish();

        void readPushFailed(int errCode, String errMsg);

    }

    interface Presenter extends BasePresenter<View> {
        void readPush(String departmentId, String pid); //push信息已阅读

        void loadPushMsg(String departmentId, int curPage, int limit, boolean loadMore);//加载push信息
    }

}
