package com.itcrm.GroupInformationPlatform.ui.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.manager.AppManager;
import com.itcrm.GroupInformationPlatform.ui.activity.ApprovalListActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.MainController;
import com.itcrm.GroupInformationPlatform.ui.activity.NoticeListActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.PushListActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.ScheduleActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.SystemNoticesActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.WorkReportListActivity;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;
import com.itcrm.GroupInformationPlatform.utils.StringUtils;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = AppManager.mContext;
        Bundle bundle = intent.getExtras();
        LogUtils.d("onReceive - " + intent.getAction());
        LogUtils.e("bundleaaa：" + bundle.toString());

//        LogUtils.e("收到了通知:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
        //{"ap_type":2,"type":7,"id":2}
        String pushStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!StringUtils.isBlank(pushStr)) {
            try {
                JSONObject jo = new JSONObject(pushStr);
                this.type = Integer.valueOf(jo.getString("type"));
                ap_type = Integer.valueOf(jo.getString("ap_type"));
                list_type = Integer.valueOf(jo.getString("list_id"));
                LogUtils.e("type->" + type);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            LogUtils.e("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.e("收到了通知:" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));

           /* // TODO 如果放到这块接收type，变量type在用户点击了通知后是初始值0，不懂为什么？！！！！
            LogUtils.e("收到了通知:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //{"ap_type":2,"type":7,"id":2}
            String pushStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jo = new JSONObject(pushStr);
                this.type = Integer.valueOf(jo.getString("type"));
                ap_type = Integer.valueOf(jo.getString("ap_type"));
                list_type = Integer.valueOf(jo.getString("id"));
                LogUtils.e("type->" + type);

            } catch (JSONException e) {
                e.printStackTrace();
            }*/
//            if (!StringUtils.isBlank(AppConfig.getAppConfig(context).getPrivateUid())) {
//                initNotification(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
//            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // 在这里可以自己写代码去定义用户点击后的行为
            LogUtils.e("用户点击打开了通知");

            judgeWhereToGo();

          /*  Intent i = new Intent(AppManager.context, PushListActivity.class);  //自定义打开的界面
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);*/

        } else {
            LogUtils.d("Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 判断去哪个列表
     * 1:系统消息 2：公司公告 3：部门通知 4：集团公告 5：工作汇报（我发起的） 6：审批申请
     * 7：审批回复 8：会议 9：视频会议 10：工作汇报(发送我的) 11：工作汇报（抄送我的）
     */
    private void judgeWhereToGo() {
        Intent intent = null;
        LogUtils.e("进入到judgeWhereToGo..." + type + "mContext->" + mContext);
        switch (type) {
            case 1:
                intent = new Intent(mContext, SystemNoticesActivity.class);
                break;
            case 2:
            case 3:
            case 4:
                intent = new Intent(mContext, NoticeListActivity.class);
                break;
            case 5:
                intent = new Intent(mContext, WorkReportListActivity.class);
                intent.putExtra("whichList", 1);
                break;
            case 6:
                intent = new Intent(mContext, ApprovalListActivity.class);
                intent.putExtra("whichList", 2);
                break;
            case 7:
                intent = new Intent(mContext, ApprovalListActivity.class);
                LogUtils.e("type=7");
                intent.putExtra("whichList", 1);
                break;
            case 8:
                intent = new Intent(mContext, ScheduleActivity.class);
                break;
            case 9:
                intent = new Intent(mContext, ScheduleActivity.class);
                break;
            case 10:
                intent = new Intent(mContext, WorkReportListActivity.class);
                intent.putExtra("whichList", 2);
                break;
            case 11:
                intent = new Intent(mContext, WorkReportListActivity.class);
                intent.putExtra("whichList", 3);
                break;

        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }
    }


    public void initNotification(String content) {
        LogUtils.e("initNotification执行了。。。");
        nm = (NotificationManager) AppManager.mContext.getSystemService(Context
                .NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.jpush_notification_icon, content, System
                .currentTimeMillis());
        notification.contentView = new RemoteViews(AppManager.mContext.getPackageName(), R
                .layout.notification);
        //（就是在Android Market下载软件，点击下载但还没获取到目标大小时的状态）
        Intent notificationIntent = new Intent(AppManager.mContext, PushListActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(AppManager.mContext, 0,
                notificationIntent, 0);
        notification.contentIntent = contentIntent;
        synchronized (notification) {
            notification.notify();
        }

    }
}
