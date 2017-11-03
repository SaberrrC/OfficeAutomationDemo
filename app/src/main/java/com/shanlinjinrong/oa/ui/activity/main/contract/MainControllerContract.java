package com.shanlinjinrong.oa.ui.activity.main.contract;

import android.app.Activity;

import com.hyphenate.chat.EMConversation;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.util.List;

/**
 * Created by 丁 on 2017/8/19.
 * 首页接口
 */
public interface MainControllerContract {
    interface View extends BaseView {
        void loadUnReadMsgOk(String num); //加载未读消息成功

        void loadUnReadMsgEmpty(); //加载未读消息条数为0

        void bindBadgeView(int msgCount);

        void startAppSetting();

        void easeInitFinish(AbortableFuture<LoginInfo> loginRequest); // 云信初始化结束
    }

    interface Presenter extends BasePresenter<View> {

        void applyPermission(Activity activity); // 申请权限

        void loadUnReadMsg(); //获取未读消息数量

        List<EMConversation> loadConversationList();
//
//        void loginIm(Context context);//登录环信
//
//        void initEase(AbortableFuture<LoginInfo> loginRequest, String account, String token); //登录云信
    }
}
