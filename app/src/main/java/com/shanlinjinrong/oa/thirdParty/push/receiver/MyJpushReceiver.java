package com.shanlinjinrong.oa.thirdParty.push.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * ProjectName: ZhiTongOA_BI
 * PackageName: com.itcrm.zhitongoa.bi.common
 * Author:Created by Tsui on Date:2016/11/2 9:55
 * Description:  为极光推送定义的广播接受器
 */
public class MyJpushReceiver extends BroadcastReceiver {
    private Notification notification;
    private NotificationManager nm;
    private Context mContext;

    private Integer type = 0;//推送的类型
    private Integer ap_type = 0;//审批的类型
    private Integer list_type = 0;//列表的类型
    private static String DOT_STATUS = "DOT_STATUS";

    private static int TYPE_SEND_TO_ME = 0;//发送我的
    private static int TYPE_WAIT_ME_APPROVAL = 1;//待我审批
    public static String DOT_SEND = "DOT_SEND";
    public static String DOT_APPORVAL = "DOT_APPORVAL";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = AppManager.mContext;
        Bundle bundle = intent.getExtras();

        //推送的内容
        String pushMessage = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String pushStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!StringUtils.isBlank(pushStr)) {
            try {
                JSONObject jo = new JSONObject(pushStr);
                this.type = Integer.valueOf(jo.getString("type"));

                SharedPreferences sp = mContext.getSharedPreferences(AppConfig.getAppConfig(mContext).getPrivateUid() + DOT_STATUS, Context.MODE_PRIVATE);
                if (type == TYPE_SEND_TO_ME) {
                    sp.edit().putBoolean("DOT_SEND", true).apply();
                } else if (type == TYPE_WAIT_ME_APPROVAL) {
                    sp.edit().putBoolean("DOT_APPORVAL", true).apply();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
