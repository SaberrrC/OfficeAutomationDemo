package com.shanlinjinrong.oa.thirdParty.push;

import com.shanlinjinrong.oa.utils.LogUtils;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * ProjectName: dev-beta
 * PackageName: com.itcrm.GroupInformationPlatform.ui
 * Author:Created by Tsui on Date:2017/3/22 9:55
 * Description:
 */
public class VideoNumbersObservers {

    private Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }

            for (IMMessage msg : messages) {
                if (msg == null) {
                    LogUtils.e( "receive chat room message null");
                    continue;
                }

                LogUtils.e( "receive msg type:" + msg.getMsgType());
                if (msg.getMsgType() == MsgTypeEnum.notification) {
//                    handleNotification(msg);
                }

                // 成员权限
//                if (sendReceiveMemPermissions(msg)) {
//                    return;
//                }
//
//                for (RoomMsgObserver observer : roomMsgObservers) {
//                    observer.onMsgIncoming(messages);
//                }
            }
        }
    };

}
