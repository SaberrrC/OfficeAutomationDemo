package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.Constant;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.ChatRowVoiceCall;
import com.hyphenate.easeui.ui.ContextMenuActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatRowRecall;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.MediaPlayerHelper;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.message.contract.LookMessageRecordContract;
import com.shanlinjinrong.oa.ui.activity.message.presenter.LookMessageRecordPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//查看消息记录界面
public class LookMessageRecordActivity extends HttpBaseActivity<LookMessageRecordPresenter> implements LookMessageRecordContract.View {

    @BindView(R.id.tv_title)
    TextView            mTvTitle;
    @BindView(R.id.message_list)
    EaseChatMessageList messageList;
    private int                                     chatType;
    private String                                  toChatUsername;
    private EaseChatFragment.EaseChatFragmentHelper chatFragmentHelper;
    private SwipeRefreshLayout                      swipeRefreshLayout;
    private EMConversation                          conversation;
    private EMMessage                               mEmMessage;
    private Bundle                                  mBundle;
    private List<EMMessage> mAllMessages = new ArrayList<>();
    private String mGroupId;
    private boolean isLoadOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_message_record);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        Intent intent = getIntent();
        chatType = intent.getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {//个人聊天
            mBundle = intent.getParcelableExtra("EXTRAS");
            toChatUsername = mBundle.getString("toChatUsername");
            conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        } else {//群组
            mGroupId = intent.getStringExtra(EaseConstant.GROUPID);
            if (!TextUtils.isEmpty(mGroupId)) {
                conversation = EMClient.getInstance().chatManager().getConversation(mGroupId);
            }
        }
        //         mBundle.getInt("CHAT_TYPE", 666);
    }

    private void initView() {
        messageList.setShowUserNick(true);
        chatFragmentHelper = new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
            }

            @Override
            public void onEnterToChatDetails() {
            }

            @Override
            public void onAvatarClick(String username) {
                //                mListener.clickUserInfo(username, mEmMessage);
            }

            @Override
            public void onAvatarLongClick(String username) {
            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                try {
                    EMMessageBody body = message.getBody();
                    String body1 = body.toString();
                    String amr = body1.substring(body1.indexOf("localurl") + 10, body1.indexOf("remoteurl") - 1);
                    if (!TextUtils.isEmpty(amr.trim())) {
                        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        int mode = audioManager.getRingerMode();
                        if (mode == AudioManager.RINGER_MODE_NORMAL) {//普通模式
                        }
                        if (mode == AudioManager.RINGER_MODE_VIBRATE) {//振动模式
                            MediaPlayerHelper.playSound(amr, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    MediaPlayerHelper.realese();
                                }
                            });
                        }
                        if (mode == AudioManager.RINGER_MODE_SILENT) {//静音模式
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {
                startActivityForResult((new Intent(LookMessageRecordActivity.this, ContextMenuActivity.class)).putExtra("message", message).putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM), EaseChatFragment.REQUEST_CODE_CONTEXT_MENU);
            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return new CustomChatRowProvider();
            }
        };
        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setEnabled(false);
        onMessageListInit();
        onConversationInit();
        //        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername);
        //获取此会话在本地的所有的消息数量
        int allMsgCount = conversation.getAllMsgCount();
        int count = allMsgCount / PAGE_SIZE;
        if (allMsgCount % PAGE_SIZE != 0) {
            count++;
        }
        totalPage = count;
        setTtitle();
    }

    private void setTtitle() {
        mTvTitle.setText("聊天记录(" + currentPage + "/" + totalPage + ")");
    }

    private   int currentPage = 1;
    private   int totalPage   = 0;
    protected int PAGE_SIZE   = 20;

    protected void onConversationInit() {
        //        if (conversation != null) {
        //        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        //        new Thread() {
        //            @Override
        //            public void run() {
        //                try {
        //                    EMClient.getInstance().chatManager().fetchHistoryMessages(toChatUsername, EaseCommonUtils.getConversationType(chatType), PAGE_SIZE, "");
        //                    final List<EMMessage> msgs = conversation.getAllMessages();
        //                    int msgCount = msgs != null ? msgs.size() : 0;
        //                    if (msgCount <= conversation.getAllMsgCount() && msgCount < PAGE_SIZE) {
        //                        String msgId = null;
        //                        if (msgs != null && msgs.size() > 0) {
        //                            msgId = msgs.get(0).getMsgId();
        //                        }
        //                        List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(msgId, PAGE_SIZE - msgCount);
        //                        mEmMessage = emMessages.get(0);
        //                    }
        //                    messageList.refreshSelectLast();
        //        messageList.refreshPageSizeItem(messages2);
        //                } catch (HyphenateException e) {
        //                    e.printStackTrace();
        //                }
        //                return;
        //            }
        //        }.start();
        //        EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        showLoadingView();
        new Thread() {
            @Override
            public void run() {
                mAllMessages = conversation.loadMoreMsgFromDB("", Integer.MAX_VALUE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAllMessages.size() == 0) {
                            isLoadOver = true;
                            hideLoadingView();
                            return;
                        }
                        List<EMMessage> mdatas = new ArrayList<>();
                        if (mAllMessages.size() <= PAGE_SIZE) {
                            messageList.refreshPageSizeItem(mAllMessages);
                            isLoadOver = true;
                            hideLoadingView();
                            return;
                        }
                        for (int i = mAllMessages.size() - PAGE_SIZE; i < mAllMessages.size(); i++) {
                            mdatas.add(mAllMessages.get(i));
                        }
                        isLoadOver = true;
                        hideLoadingView();
                        messageList.refreshPageSizeItem(mdatas);
                    }
                });
            }
        }.start();
    }

    private boolean isMessageListInited;

    protected void onMessageListInit() {
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ? chatFragmentHelper.onSetCustomChatRowProvider() : null, false);
        //        setListItemClickListener();
        isMessageListInited = true;
    }

    //    public void resendMessage(EMMessage message) {
    //        message.setStatus(EMMessage.Status.CREATE);
    //        EMClient.getInstance().chatManager().sendMessage(message);
    //        messageList.refresh();
    //    }

    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        if (type == EMMessage.Type.TXT) {
            if (forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(), forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
            } else {
                // get the content and send it
                String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                sendTextMessage(content);
            }
        }
        if (type == EMMessage.Type.IMAGE) {
            String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
            if (filePath != null) {
                File file = new File(filePath);
                if (!file.exists()) {
                    // send thumb nail if original image does not exist
                    filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                }
                sendImageMessage(filePath);
            }
        }
        if (forward_msg.getChatType() == EMMessage.ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        //TODO 图片携带消息
        //TODO TEXT 携带消息  暂做修改
        //readUserInfoDetailsMessage();
        sendUserInfoDetailsMessage(message);
        sendMessage(message);
    }

    private void sendUserInfoDetailsMessage(EMMessage message) {

        //TODO 暂做修改
        //        String from = message.getFrom();
        //        UserInfoSelfDetailsBean bean = new Gson().fromJson(userInfo_self, UserInfoSelfDetailsBean.class);
        //        String newUserInfo_self = null;
        //        String newUserInfo = null;
        //        try {
        //            if (!from.equals("sl_" + bean.CODE_self)) {
        //                newUserInfo_self = userInfo_self.replaceAll("_self", "");
        //                newUserInfo = userInfo.replace("phone", "phone_self");
        //                newUserInfo = newUserInfo.replace("CODE", "CODE_self");
        //                newUserInfo = newUserInfo.replace("sex", "sex_self");
        //                newUserInfo = newUserInfo.replace("post_title", "post_title_self");
        //                newUserInfo = newUserInfo.replace("username", "username_self");
        //                newUserInfo = newUserInfo.replace("portrait", "portrait_self");
        //                newUserInfo = newUserInfo.replace("/portrait_self", "/portrait/");
        //                newUserInfo = newUserInfo.replace("email", "email_self");
        //                newUserInfo = newUserInfo.replace("department_name", "department_name_self");
        //                sendUserInfo_self = new JSONObject(newUserInfo_self);
        //                sendUserInfo = new JSONObject(newUserInfo);
        //                message.setAttribute("userInfo_self", sendUserInfo);
        //                message.setAttribute("userInfo", sendUserInfo_self);
        //
        //            } else {
        //                sendUserInfo_self = new JSONObject(userInfo_self);
        //                sendUserInfo = new JSONObject(userInfo);
        //                message.setAttribute("userInfo_self", sendUserInfo_self);
        //                message.setAttribute("userInfo", sendUserInfo);
        //            }
        //        } catch (Throwable e) {
        //            e.printStackTrace();
        //        }

        //TODO 新消息扩展
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userHead", mBundle.getString("userHeadSelf"));
            jsonObject.put("userId", mBundle.getString("userIdSelf"));
            jsonObject.put("userCode", mBundle.getString("userCodeSelf"));
            jsonObject.put("userName", mBundle.getString("userNameSelf"));
            message.setAttribute("userInfo", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void sendTextMessage(String content) {
        //readUserInfoDetailsMessage();
        if (EaseAtMessageHelper.get().containsAtUsername(content)) {
            sendAtMessage(content);
        } else {
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            if (!message.getFrom().equals(mBundle.getString("toChatUsername"))) {
                EMConversation conversationDB = EMClient.getInstance().chatManager().getConversation(toChatUsername);
                if (conversationDB != null) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userName", mBundle.getString("userName"));
                        jsonObject.put("userCode", mBundle.getString("toChatUsername"));
                        jsonObject.put("userHead", mBundle.getString("userHead"));
                        conversationDB.setExtField(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            sendUserInfoDetailsMessage(message);
            sendMessage(message);
            //            mEmMessage = message;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void sendAtMessage(String content) {
        if (chatType != EaseConstant.CHATTYPE_GROUP) {
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);

        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.get().containsAtAll(content)) {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        } else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseAtMessageHelper.get().atListToJsonArray(EaseAtMessageHelper.get().getAtMessageUsernames(content)));
        }
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message) {
        //给对方发送自己的扩展信息用户名和头像
        //FriendsInfoCacheSvc.getInstance(getContext()).addOrUpdateFriends(new Friends(toChatUsername,
        //toChatUsernike, toChatUserpic));
        if (message == null) {
            return;
        }
        if (chatFragmentHelper != null) {
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        EMClient.getInstance().chatManager().sendMessage(message);
        if (isMessageListInited) {
            messageList.refreshSelectLast();
        }
    }

    protected void sendBigExpressionMessage(String name, String identityCode) {
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        //TODO 语音携带消息
        //TODO TEXT 携带消息  暂做修改
        //readUserInfoDetailsMessage();
        sendUserInfoDetailsMessage(message);
        sendMessage(message);
    }

    @OnClick({R.id.iv_back, R.id.iv_search, R.id.iv_first, R.id.iv_last, R.id.iv_next, R.id.iv_final})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                if (!isLoadOver) {
                    return;
                }
                Intent intent = new Intent(this, MessageSearchActivity.class);
                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    intent.putExtra("EXTRAS", mBundle);
                    intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                    startActivity(intent);
                    return;
                }
                intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                intent.putExtra(EaseConstant.GROUPID, mGroupId);
                startActivity(intent);
                break;
            case R.id.iv_first:
                if (!isLoadOver) {
                    return;
                }
                if (currentPage == totalPage) {
                    Toast.makeText(this, getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<EMMessage> mdatasFirst = new ArrayList<>();
                for (int i = 0; i < mAllMessages.size() - (totalPage - 1) * PAGE_SIZE; i++) {
                    if (i < 0) {
                        continue;
                    }
                    mdatasFirst.add(mAllMessages.get(i));
                }
                messageList.refreshPageSizeItem(mdatasFirst);
                currentPage = totalPage;
                setTtitle();
                break;
            case R.id.iv_last:
                if (!isLoadOver) {
                    return;
                }
                if (currentPage == totalPage) {
                    Toast.makeText(this, getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<EMMessage> mdatas = new ArrayList<>();
                for (int i = mAllMessages.size() - (currentPage + 1) * PAGE_SIZE; i < mAllMessages.size() - currentPage * PAGE_SIZE; i++) {
                    if (i < 0) {
                        continue;
                    }
                    mdatas.add(mAllMessages.get(i));
                }
                messageList.refreshPageSizeItem(mdatas);
                currentPage++;
                setTtitle();
                break;
            case R.id.iv_next:
                if (!isLoadOver) {
                    return;
                }
                if (currentPage == 1) {
                    Toast.makeText(this, getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<EMMessage> mdatasNext = new ArrayList<>();
                for (int i = mAllMessages.size() - (currentPage - 1) * PAGE_SIZE; i < mAllMessages.size() - (currentPage - 2) * PAGE_SIZE; i++) {
                    if (i < 0) {
                        continue;
                    }
                    if (i >= mAllMessages.size()) {
                        break;
                    }
                    mdatasNext.add(mAllMessages.get(i));
                }
                messageList.refreshPageSizeItem(mdatasNext);
                currentPage--;
                setTtitle();
                break;
            case R.id.iv_final:
                if (!isLoadOver) {
                    return;
                }
                if (currentPage == 1) {
                    Toast.makeText(this, getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAllMessages.size() <= PAGE_SIZE) {
                    messageList.refreshPageSizeItem(mAllMessages);
                    return;
                }
                List<EMMessage> mdatasFinal = new ArrayList<>();
                for (int i = mAllMessages.size() - PAGE_SIZE; i < mAllMessages.size(); i++) {
                    if (i < 0) {
                        continue;
                    }
                    mdatasFinal.add(mAllMessages.get(i));
                }
                messageList.refreshPageSizeItem(mdatasFinal);
                currentPage = 1;
                setTtitle();
                break;
        }
    }

    @Override
    public void uidNull(String code) {
    }

    public final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            return 11;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? EaseChatFragment.MESSAGE_TYPE_RECV_VOICE_CALL : EaseChatFragment.MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? EaseChatFragment.MESSAGE_TYPE_RECV_VIDEO_CALL : EaseChatFragment.MESSAGE_TYPE_SENT_VIDEO_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)) {
                    return EaseChatFragment.MESSAGE_TYPE_RECALL;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) || message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    return new ChatRowVoiceCall(LookMessageRecordActivity.this, message, position, adapter);
                } else if (message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)) {
                    return new EaseChatRowRecall(LookMessageRecordActivity.this, message, position, adapter);
                }
            }
            return null;
        }
    }
}