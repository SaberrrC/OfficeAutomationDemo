package com.shanlinjinrong.oa.ui.activity.netease;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;

/**
 * Created by hzxuwen on 2016/5/5.
 */
public class MsgHelper {

    public static MsgHelper getInstance() {
        return InstanceHolder.instance;
    }

    // 发送点对点不推送不支持离线的自定义系统通知
    public void sendP2PCustomNotification(String roomId, String command, String account) {
        CustomNotification notification = new CustomNotification();
        notification.setSessionId(account);
        notification.setSessionType(SessionTypeEnum.P2P);
        CustomNotificationConfig config = new CustomNotificationConfig();
        config.enablePush = false; // 不推送
        notification.setConfig(config);
        notification.setSendToOnlineUserOnly(true); // 不支持离线

        JSONObject data = new JSONObject();
        data.put("command", command);
        data.put("room_id", roomId);
        JSONObject json = new JSONObject();
        json.put("data", data);
        notification.setContent(json.toString());

        // 发送自定义通知
        NIMClient.getService(MsgService.class).sendCustomNotification(notification);
    }

    /**
     * ************************************ 单例 ***************************************
     */
    static class InstanceHolder {
        final static MsgHelper instance = new MsgHelper();
    }
}
