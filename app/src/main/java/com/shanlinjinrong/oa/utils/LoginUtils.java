package com.shanlinjinrong.oa.utils;

import android.content.Context;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;

/**
 * Created by 丁 on 2017/9/30.
 */

public class LoginUtils {

    public static void loginIm(final Context context, final ImLoginListener imLoginListener) {
        try {
            EMClient.getInstance().login(Constants.CID + "_" +
                    AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_CODE), "123456", new EMCallBack() {//回调
                @Override
                public void onSuccess() {
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    LogUtils.e("登录聊天服务器成功！");
                    String u_id = Constants.CID + "_" + AppConfig.getAppConfig(context).getPrivateCode();
                    String u_name = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_USERNAME);
                    String u_pic = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_PORTRAITS);
                    String sex = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_SEX);
                    String phone = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_PHONE);
                    String post = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_POST_NAME);
                    String department = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_DEPARTMENT_NAME);
                    String email = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_USER_EMAIL);
                    String departmentId = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_DEPARTMENT);
                    FriendsInfoCacheSvc.getInstance(context)
                            .addOrUpdateFriends(new Friends(u_id, u_name,  u_pic, sex, phone, post, department, email, departmentId));
                    if (imLoginListener != null) {
                        imLoginListener.loginImSuccess();
                    }

                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    LogUtils.e("登录聊天服务器失败！" + "code:" + code + "," + message);
                    if (imLoginListener != null) {
                        imLoginListener.loginImFailed();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initEase(final Context context, final EaseInitLoginListener easeInitLoginListener) {

    }

    public interface EaseInitLoginListener {
        void easeInitSuccess();

        void easeInitFailed();
    }

    public interface ImLoginListener {
        void loginImSuccess();

        void loginImFailed();
    }


}
