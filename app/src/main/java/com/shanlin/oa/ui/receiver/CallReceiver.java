package com.shanlin.oa.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.shanlin.oa.ui.activity.VoiceCallActivity;
import com.shanlin.oa.utils.LogUtils;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.receiver
 * Author:Created by Tsui on Date:2016/12/19 15:37
 * Description:环信实时语音电话
 */
public class CallReceiver extends BroadcastReceiver {

//
//    获取 String callExt = EMClient.getInstance().callManager().getCurrentCallSession().getExt();
//    这个 ext 内容只能是 String ，也可以用 json 对象定义好内容后，转成 String 设置


    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.e("接收到新实时语音消息了");
        // 拨打方username
        String from = intent.getStringExtra("from");
        // call type
        String type = intent.getStringExtra("type");

        String extUserInfo = EMClient.getInstance().callManager().getCurrentCallSession().getExt();
        LogUtils.e("ext-->" + extUserInfo);

        try {
            String[] splits = extUserInfo.split("&");

            String nike = splits[0];

            String portrait = null;
            if (splits.length > 1) {
                portrait = splits[1];
            }


            LogUtils.e("nike,portrait->" + nike + "," + portrait);

             FriendsInfoCacheSvc.getInstance(context).addOrUpdateFriends(new Friends(from, nike, portrait));
            if ("voice".equals(type)) { //video call
                context.startActivity(new Intent(context, VoiceCallActivity.class).
                        putExtra("nike", nike).putExtra("isComingCall", true).
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }


//            UserApiModel apiModel = UserInfoCacheSvc.getByChatUserName(context, from);
//            if (null == apiModel || null == apiModel.getUsername()) {
//                boolean orUpdate = UserInfoCacheSvc.createOrUpdate(context, from, nike, portrait);
//                if (orUpdate) {
//                    LogUtils.e("保存拨打电话方的数据成功");
//                    if ("voice".equals(type)) { //video call
//                        context.startActivity(new Intent(context, VoiceCallActivity.class).
//                                putExtra("nike", nike).putExtra("isComingCall", true).
//                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                    }
//                }
//            } else {
//                if ("voice".equals(type)) { //video call
//                    context.startActivity(new Intent(context, VoiceCallActivity.class).
//                            putExtra("nike", nike).putExtra("isComingCall", true).
//                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                }
//            }

        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }
}
