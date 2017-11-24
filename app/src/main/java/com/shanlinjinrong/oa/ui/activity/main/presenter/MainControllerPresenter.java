package com.shanlinjinrong.oa.ui.activity.main.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.hyphenate.chat.EMClient;
import com.pgyersdk.update.PgyUpdateManager;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.main.contract.MainControllerContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.SharedPreferenceUtils;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 */

public class MainControllerPresenter extends HttpPresenter<MainControllerContract.View> implements MainControllerContract.Presenter {

    @Inject
    public MainControllerPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    //存储权限判断
    public void applyPermission(Activity context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (SharedPreferenceUtils.getShouldAskPermission(context, "firstshould") && !ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//第一次已被拒绝
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("权限开启");
                builder.setMessage("更新功能无法正常使用，去权限列表开启该权限");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.startAppSetting();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            } else {//
                SharedPreferenceUtils.setShouldAskPermission(context, "firstshould", ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }

        } else {
            //            PgyUpdateManager.setIsForced(true);
            PgyUpdateManager.register(context, "com.shanlinjinrong.oa.fileprovider");
        }
    }

    public void setUnreadMessageCount() {
        int tempCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        mView.bindBadgeView(tempCount);
    }

    //    @Override
    //    public void loginIm(final Context context) {
    //        try {
    //            EMClient.getInstance().login(Constants.CID + "_" +
    //                    AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_CODE), "123456", new EMCallBack() {//回调
    //
    //                @Override
    //                public void onSuccess() {
    //                    EMClient.getInstance().groupManager().loadAllGroups();
    //                    EMClient.getInstance().chatManager().loadAllConversations();
    //                    LogUtils.e("登录聊天服务器成功！");
    //                    String u_id = Constants.CID + "_" + AppConfig.getAppConfig(context).getPrivateCode();
    //                    String u_name = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_USERNAME);
    //                    String u_pic = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_PORTRAITS);
    //                    String sex = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_SEX);
    //                    String phone = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_PHONE);
    //                    String post = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_POST_NAME);
    //                    String department = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_DEPARTMENT_NAME);
    //                    String email = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_USER_EMAIL);
    //                    String departmentId = AppConfig.getAppConfig(context).get(AppConfig.PREF_KEY_DEPARTMENT);
    //                    FriendsInfoCacheSvc.getInstance(context)
    //                            .addOrUpdateFriends(new Friends(u_id, u_name, u_pic, sex, phone, post, department, email, departmentId));
    //                }
    //
    //                @Override
    //                public void onProgress(int progress, String status) {
    //
    //                }
    //
    //                @Override
    //                public void onError(int code, String message) {
    //                    LogUtils.e("登录聊天服务器失败！" + "code:" + code + "," + message);
    //                }
    //            });
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    //
    //    @Override
    //    public void initEase(final AbortableFuture<LoginInfo> loginRequest, final String account, final String token) {
    //        new Thread(new Runnable() {
    //            @Override
    //            public void run() {
    //                // 登录
    //                LogUtils.e("account:" + account + ",token:" + token);
    //                if (loginRequest == null) {
    //                    final AbortableFuture<LoginInfo> mRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(
    //                            account, token));
    //                    mRequest.setCallback(new RequestCallback<LoginInfo>() {
    //                        @Override
    //                        public void onSuccess(LoginInfo param) {
    //                            LogUtils.e("云信login success。。。");
    //                            //// TODO: 2017/8/19 为啥之前成功也返回空？
    //                            mView.easeInitFinish(mRequest);
    //                        }
    //
    //                        @Override
    //                        public void onFailed(int code) {
    //                            mView.easeInitFinish(null);
    //                            LogUtils.e("云信login Failed。。。" + code);
    //                        }
    //
    //                        @Override
    //                        public void onException(Throwable exception) {
    //                            mView.easeInitFinish(null);
    //                            LogUtils.e("云信login Failed。。。" + exception);
    //                        }
    //                    });
    //                }
    //            }
    //        }
    //        ).start();
    //    }

}
