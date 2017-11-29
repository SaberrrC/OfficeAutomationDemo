package com.hyphenate.easeui.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.Constant;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.PermissionListener;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.onEaseUIFragmentListener;
import com.hyphenate.easeui.requestPermissionsListener;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.utils.MediaPlayerHelper;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu.ChatInputMenuListener;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView.EaseVoiceRecorderCallback;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * you can new an EaseChatFragment to use or you can inherit it to expand.
 * You need call setArguments to pass chatType and userId
 * <br/>
 * <br/>
 * you can see ChatActivity in demo for your reference
 */
public class EaseChatFragment extends EaseBaseFragment implements EMMessageListener, View.OnClickListener {
    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;

    /**
     * params to fragment
     */
    protected Bundle fragmentArgs;
    protected int chatType;
    protected String toChatUsername;
    protected EaseChatMessageList messageList;
    protected EaseChatInputMenu inputMenu;

    protected EMConversation conversation;

    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;

    protected Handler handler = new Handler();
    protected File cameraFile;
    protected EaseVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView listView;

    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;
    protected GroupListener groupListener;
    protected ChatRoomListener chatRoomListener;
    protected EMMessage contextMenuMessage;

    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_VOICECALL = 3;

    protected int[] itemStrings = {R.string.attach_take_pic, R.string.attach_picture, R.string.attach_voice_call};
    protected int[] itemdrawables = {R.drawable.ease_chat_takepic_selector, R.drawable.ease_chat_image_selector, R.drawable.ease_chat_location_selector};
    protected int[] itemIds = {ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_VOICECALL};
    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;
    protected boolean isRoaming = false;
    private ExecutorService fetchQueue;
    private String userName;
    private onEaseUIFragmentListener mListener;
    //麦克风权限请求码
    private static final int REQUEST_RECORD_AUDIO = 100;
    private ImageView    mIvfirst;
    private ImageView    mIvLast;
    private ImageView    mIvNext;
    private ImageView    mIvFinal;
    private String       PAGE_TYPE;
    private LinearLayout mLlHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, boolean roaming) {
        isRoaming = roaming;
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        fragmentArgs = getArguments();
        chatType = fragmentArgs.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        toChatUsername = fragmentArgs.getString(EaseConstant.EXTRA_USER_ID);
        PAGE_TYPE = fragmentArgs.getString("PAGE_TYPE");
        initData();
        super.onActivityCreated(savedInstanceState);
    }

    private void initData() {
        chatFragmentHelper = new EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
            }

            @Override
            public void onEnterToChatDetails() {
            }

            @Override
            public void onAvatarClick(String username) {
                mListener.clickUserInfo(username, mEmMessage);
            }

            @Override
            public void onAvatarLongClick(String username) {
            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                //TODO 震动模式播放语音 待优化 声音小
                try {
                    EMMessageBody body = message.getBody();
                    String body1 = body.toString();
                    String amr = body1.substring(body1.indexOf("localurl") + 10, body1.indexOf("remoteurl") - 1);
                    if (!TextUtils.isEmpty(amr.trim())) {
                        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
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
                // no message forward when in chat room
                try {
                    startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message", message).putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM), REQUEST_CODE_CONTEXT_MENU);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    }

    protected void initView() {
        // hold to record voice
        //noinspection ConstantConditions
        voiceRecorderView = (EaseVoiceRecorderView) getView().findViewById(R.id.voice_recorder);
        messageList = (EaseChatMessageList) getView().findViewById(R.id.message_list);
        if (chatType != EaseConstant.CHATTYPE_SINGLE)
            messageList.setShowUserNick(true);
        //        messageList.setAvatarShape(1);
        listView = messageList.getListView();
        inputMenu = (EaseChatInputMenu) getView().findViewById(R.id.input_menu);
        extendMenuItemClickListener = new MyItemClickListener();
        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        PAGE_TYPE = "CHAT";
        //        if (TextUtils.equals(PAGE_TYPE, "CHAT")) {
        //            mLlHistory.setVisibility(View.GONE);
        registerExtendMenuItem();
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(new requestPermissionsListener() {
                    @Override
                    public void requestPermissions() {
                        //TODO 请求录音权限
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                    }
                }, v, event, new EaseVoiceRecorderCallback() {
                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (isRoaming) {
            fetchQueue = Executors.newSingleThreadExecutor();
        }
        //        }
        mLlHistory = (LinearLayout) getView().findViewById(R.id.ll_history);
        //        if (TextUtils.equals(PAGE_TYPE, "HISTORY")) {
        mLlHistory.setVisibility(View.VISIBLE);
        //            inputMenu.setVisibility(View.GONE);
        initHistoryBottomView();
        //        }
    }

    private void initHistoryBottomView() {
        mIvfirst = (ImageView) getView().findViewById(R.id.iv_first);
        mIvLast = (ImageView) getView().findViewById(R.id.iv_last);
        mIvNext = (ImageView) getView().findViewById(R.id.iv_next);
        mIvFinal = (ImageView) getView().findViewById(R.id.iv_final);
        mIvfirst.setOnClickListener(this);
        mIvLast.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
        mIvFinal.setOnClickListener(this);
    }

    @Override
    protected void setUpView() {
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }
        setRefreshLayoutListener();
        if (TextUtils.equals(PAGE_TYPE, "CHAT")) {

        }
        if (TextUtils.equals(PAGE_TYPE, "HISTORY")) {
            swipeRefreshLayout.setEnabled(false);
        }
        // show forward message if the message is not null
        String forward_msg_id = getArguments().getString("forward_msg_id");
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
    }

    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem() {
        for (int i = 0; i < itemStrings.length; i++) {
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }


    protected void onConversationInit() {
        //        if (conversation != null) {
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        if (isRoaming) {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().chatManager().fetchHistoryMessages(toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize, "");
                        final List<EMMessage> msgs = conversation.getAllMessages();
                        int msgCount = msgs != null ? msgs.size() : 0;
                        if (msgCount <= conversation.getAllMsgCount() && msgCount < pagesize) {
                            String msgId = null;
                            if (msgs != null && msgs.size() > 0) {
                                msgId = msgs.get(0).getMsgId();
                            }
                            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
                            mEmMessage = emMessages.get(0);
                        }
                        messageList.refreshSelectLast();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            });
            return;
        }
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount <= conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
                mEmMessage = msgs.get(0);
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
    }

    protected void onMessageListInit() {
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ? chatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();
        messageList.getListView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (TextUtils.equals(PAGE_TYPE, "HISTORY")) {
                    return false;
                }
                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });
        isMessageListInited = true;
    }

    protected void setListItemClickListener() {
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarClick(username);
                }
            }

            @Override
            public void onUserAvatarLongClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarLongClick(username);
                }
            }

            @Override
            public void onResendClick(final EMMessage message) {
                new EaseAlertDialog(getActivity(), R.string.resend, R.string.confirm_resend, null, new AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        resendMessage(message);
                    }
                }, true).show();
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onMessageBubbleLongClick(message);
                }
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                if (chatFragmentHelper == null) {
                    return false;
                }
                return chatFragmentHelper.onMessageBubbleClick(message);
            }

        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (!isRoaming) {
                            loadMoreLocalMessage();
                        } else {
                            loadMoreRoamingMessages();
                        }
                    }
                }, 600);
            }
        });
    }

    private void loadMoreLocalMessage() {
        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
            List<EMMessage> messages;
            try {
                messages = conversation.loadMoreMsgFromDB(conversation.getAllMessages().size() == 0 ? "" : conversation.getAllMessages().get(0).getMsgId(), pagesize);
            } catch (Exception e1) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            if (messages.size() > 0) {
                messageList.refreshSeekTo(messages.size() - 1);
                if (messages.size() != pagesize) {
                    haveMoreData = false;
                }
            } else {
                haveMoreData = false;
            }

            isloading = false;
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadMoreRoamingMessages() {
        if (!haveMoreData) {
            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        if (fetchQueue != null) {
            fetchQueue.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<EMMessage> messages = conversation.getAllMessages();
                        EMClient.getInstance().chatManager().fetchHistoryMessages(toChatUsername, EaseCommonUtils.getConversationType(chatType), pagesize, (messages != null && messages.size() > 0) ? messages.get(0).getMsgId() : "");
                        mEmMessage = (messages != null && messages.size() > 0) ? messages.get(0) : null;
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    } finally {
                        Activity activity = getActivity();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loadMoreLocalMessage();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMessageListInited)
            messageList.refresh();
        EaseUI.getInstance().pushActivity(getActivity());
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);

        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }

        if (chatRoomListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomListener(chatRoomListener);
        }

        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
        }
    }

    public void onBackPressed() {
        if (inputMenu.onBackPressed()) {
            getActivity().finish();
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
                EaseAtMessageHelper.get().cleanToAtUserList();
            }
            if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
                EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
            }
        }
    }

    EMMessage mEmMessage;

    // implement methods in EMMessageListener
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            mEmMessage = message;

            String username = null;
            // group message
            if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }
            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername) || message.conversationId().equals(toChatUsername)) {
                messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                conversation.markMessageAsRead(message.getMsgId());
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
        messageList.refresh();
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_first) {
            Toast.makeText(getActivity(), "" + i, Toast.LENGTH_SHORT).show();
        }
        if (i == R.id.iv_last) {
            if (haveMoreData) {
                List<EMMessage> messages;
                try {
                    messages = conversation.loadMoreMsgFromDB(conversation.getAllMessages().size() == 0 ? "" : conversation.getAllMessages().get(0).getMsgId(), pagesize);
                } catch (Exception e1) {
                    return;
                }
                if (messages.size() > 0) {

//                    messageList.refreshSeekTo(messages.size() - 1);
                    messageList.refreshPageSizeItem(messages);
                    if (messages.size() != pagesize) {
                        haveMoreData = false;
                    }
                } else {
                    haveMoreData = false;
                }
                isloading = false;
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
            }
        }
        if (i == R.id.iv_next) {
            Toast.makeText(getActivity(), "" + i, Toast.LENGTH_SHORT).show();
        }
        if (i == R.id.iv_final) {
            Toast.makeText(getActivity(), "" + i, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * handle the click event for extend menu
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if (chatFragmentHelper != null) {
                if (chatFragmentHelper.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal();
                    break;
                case ITEM_VOICECALL:
                    selectFroVoiceCall();
                    break;
            }
        }
    }

    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        if (EMClient.getInstance().getCurrentUser().equals(username) || chatType != EaseConstant.CHATTYPE_GROUP) {
            return;
        }
        EaseAtMessageHelper.get().addAtUser(username);
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (user != null) {
            username = user.getNick();
        }
        if (autoAddAtSymbol)
            inputMenu.insertText("@" + username + " ");
        else
            inputMenu.insertText(username + " ");
    }


    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username) {
        inputAtUsername(username, true);
    }

    //TODO 偷传 暂作修改
    //String userInfo_self;
    //String userInfo;
    //JSONObject sendUserInfo_self;
    //JSONObject sendUserInfo;

    //send message
    protected void sendTextMessage(String content) {
        //TODO 暂作修改
        //readUserInfoDetailsMessage();
        if (EaseAtMessageHelper.get().containsAtUsername(content)) {
            sendAtMessage(content);
        } else {
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);

            //TODO 存储数据
            if (!message.getFrom().equals(getArguments().getString("toChatUsername", ""))) {
                EMConversation conversationDB = EMClient.getInstance().chatManager().getConversation(toChatUsername);
                if (conversationDB != null) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userName", getArguments().getString("userName", ""));
                        jsonObject.put("userCode", getArguments().getString("toChatUsername", ""));
                        jsonObject.put("userHead", getArguments().getString("userHead", ""));
                        jsonObject.put("userId", getArguments().getString("userId", ""));
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
            jsonObject.put("userHead", getArguments().getString("userHeadSelf", ""));
            jsonObject.put("userId", getArguments().getString("userIdSelf", ""));
            jsonObject.put("userCode", getArguments().getString("userCodeSelf", ""));
            jsonObject.put("userName", getArguments().getString("userNameSelf", ""));
            message.setAttribute("userInfo", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //TODO TEXT 携带消息  暂做修改
    //    private void readUserInfoDetailsMessage() {
    //        JSONObject object_self = new JSONObject();
    //        JSONObject object = new JSONObject();
    //        try {
    //            userInfo_self = getArguments().getString("userInfo_self", "");
    //            userInfo = getArguments().getString("userInfo", "");
    //            try {
    //                if (userInfo_self.equals("") && mEmMessage.getStringAttribute("userInfo_self", "") != null) {
    //                    userInfo_self = mEmMessage.getStringAttribute("userInfo_self", "");
    //                }
    //                if (userInfo.equals("") && mEmMessage.getStringAttribute("userInfo", "") != null) {
    //                    userInfo = mEmMessage.getStringAttribute("userInfo", "");
    //                }
    //            } catch (Throwable e1) {
    //                e1.printStackTrace();
    //            }
    //            String user_code = getArguments().getString("user_code", "-");
    //            String to_user_code = getArguments().getString("to_user_code", "-");
    //
    //            if (!user_code.equals("-") && getArguments().getString("user_code", "-").equals(user_code) && userInfo_self.equals("")) {
    //                object_self.put("CODE_self", getArguments().getString("user_code", "-"));
    //                object_self.put("phone_self", getArguments().getString("userPhone", "-"));
    //                object_self.put("sex_self", getArguments().getString("userSex", "-"));
    //                object_self.put("post_title_self", getArguments().getString("userPost", "-"));
    //                object_self.put("username_self", getArguments().getString("userName", "-"));
    //                object_self.put("portrait_self", getArguments().getString("userPic", "-"));
    //                object_self.put("email_self", getArguments().getString("userEmail", "-"));
    //                object_self.put("department_name_self", getArguments().getString("userDepartment", "-"));
    //                userInfo_self = object_self.toString();
    //            }
    //
    //            if (!to_user_code.equals("-") && userInfo.equals("")) {
    //                object.put("CODE", getArguments().getString("to_user_code", "-"));
    //                object.put("phone", getArguments().getString("to_user_phone", "-"));
    //                object.put("sex", getArguments().getString("to_user_sex", "-"));
    //                object.put("post_title", getArguments().getString("to_user_post", "-"));
    //                object.put("username", getArguments().getString("to_user_nike", "-"));
    //                object.put("portrait", getArguments().getString("to_user_pic", "-"));
    //                object.put("email", getArguments().getString("to_user_email", "-"));
    //                object.put("department_name", getArguments().getString("to_user_department", "-"));
    //                userInfo = object.toString();
    //            }
    //        } catch (JSONException e) {
    //            e.printStackTrace();
    //        }
    //    }

    /**
     * send @ message, only support group chat message
     *
     * @param content
     */
    @SuppressWarnings("ConstantConditions")
    private void sendAtMessage(String content) {
        if (chatType != EaseConstant.CHATTYPE_GROUP) {
            EMLog.e(TAG, "only support group chat message");
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

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        //TODO 图片携带消息
        //TODO TEXT 携带消息  暂做修改
        //readUserInfoDetailsMessage();
        sendUserInfoDetailsMessage(message);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        //TODO 文件携带消息
        //TODO TEXT 携带消息  暂做修改
        //readUserInfoDetailsMessage();
        sendUserInfoDetailsMessage(message);
        sendMessage(message);
    }

    public void setListener(onEaseUIFragmentListener listener) {
        mListener = listener;
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
            message.setChatType(ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(ChatType.ChatRoom);
        }
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);

        //refresh ui
        if (isMessageListInited) {
            messageList.refreshSelectLast();
        }
    }


    public void resendMessage(EMMessage message) {
        message.setStatus(EMMessage.Status.CREATE);
        EMClient.getInstance().chatManager().sendMessage(message);
        messageList.refresh();
    }

    //===================================================================================


    /**
     * send image
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * send file
     *
     * @param uri
     */
    protected void sendFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser() + System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        //cameraFile.getParentFile().mkdirs();
        requestRunTimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), REQUEST_CODE_CAMERA);
            }

            @Override
            public void onDenied() {
                Toast.makeText(getContext(), "相机权限被拒绝！请手动设置", Toast.LENGTH_SHORT).show();
            }
        });
    }

    PermissionListener mlistener;

    /**
     * 6.0请求权限函数
     *
     * @param permissions
     * @param listener
     */
    public void requestRunTimePermission(String[] permissions, PermissionListener listener) {
        mlistener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            //权限已经申请，doSomething
            mlistener.onGranted();
        }
    }

    /**
     * select local image
     */
    protected void selectFroVoiceCall() {
        requestRunTimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionListener() {
            @Override
            public void onGranted() {
                mListener.voiceCallListener(toChatUsername, mEmMessage);
            }

            @Override
            public void onDenied() {
                Toast.makeText(getContext(), "语音权限被拒绝！请手动设置", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    /**
     * clear the conversation history
     */
    protected void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getActivity(), null, msg, null, new AlertDialogUser() {

            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (confirmed) {
                    if (conversation != null) {
                        conversation.clearAllMessages();
                    }
                    messageList.refresh();
                    haveMoreData = true;
                }
            }
        }, true).show();
    }

    /**
     * open group detail
     */
    protected void toGroupDetails() {
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        }
    }

    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * forward message
     */
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
        if (forward_msg.getChatType() == ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    /**
     * listen the group event
     */
    private class GroupListener extends EaseGroupListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.you_are_group, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onGroupDestroyed(final String groupId, String groupName) {
            // prompt group is dismissed and finish this activity
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.the_current_group_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }
    }

    /**
     * listen chat room event
     */
    class ChatRoomListener extends EaseChatRoomListener {

        @Override
        public void onChatRoomDestroyed(final String roomId, final String roomName) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getActivity(), R.string.the_current_chat_room_destroyed, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onRemovedFromChatRoom(final String roomId, final String roomName, final String participant) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (roomId.equals(toChatUsername)) {
                        Toast.makeText(getActivity(), R.string.quiting_the_chat_room, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }


        @Override
        public void onMemberJoined(final String roomId, final String participant) {
            if (roomId.equals(toChatUsername)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "member join:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        public void onMemberExited(final String roomId, final String roomName, final String participant) {
            if (roomId.equals(toChatUsername)) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "member exit:" + participant, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }


    }

    protected EaseChatFragmentHelper chatFragmentHelper;

    public void setChatFragmentHelper(EaseChatFragmentHelper chatFragmentHelper) {
        this.chatFragmentHelper = chatFragmentHelper;
    }

    public interface EaseChatFragmentHelper {
        /**
         * set message attribute
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();

        /**
         * on avatar clicked
         *
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * on avatar long pressed
         *
         * @param username
         */
        void onAvatarLongClick(String username);

        /**
         * on message bubble clicked
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * on message bubble long pressed
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * on extend menu item clicked, return true if you want to override
         *
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * on set custom chat row provider
         *
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }

    private static final int REQUEST_CODE_CONTEXT_MENU = 14;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();

                    if (selectedImage != null) {
                        try {
                            //TODO 如果手机使用谷歌的照片管理器，会有问题，暂时try catch了
                            sendPicByUri(selectedImage);
                        } catch (Exception e) {
                        }
                    }
                }
            }
            //            else if (requestCode == REQUEST_CODE_MAP) { // location
            //                double latitude = data.getDoubleExtra("latitude", 0);
            //                double longitude = data.getDoubleExtra("longitude", 0);
            //                String locationAddress = data.getStringExtra("address");
            //                if (locationAddress != null && !locationAddress.equals("")) {
            //                    sendLocationMessage(latitude, longitude, locationAddress);
            //                } else {
            //                    Toast.("无法获取到您的位置信息");
            //                }
            //            }
        }
        /**
         * 回调复制删除转发
         */
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null, ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
                    //Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
                    //intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                    //startActivity(intent);
                    break;
                case ContextMenuActivity.RESULT_CODE_RECALL: //recall 消息撤回

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMMessage msgNotification = EMMessage.createTxtSendMessage(" ", contextMenuMessage.getTo());
                                EMTextMessageBody txtBody = new EMTextMessageBody(getResources().getString(R.string.msg_recall_by_self));
                                msgNotification.addBody(txtBody);
                                msgNotification.setMsgTime(contextMenuMessage.getMsgTime());
                                msgNotification.setLocalTime(contextMenuMessage.getMsgTime());
                                msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);

                                EMClient.getInstance().chatManager().recallMessage(contextMenuMessage);

                                EMClient.getInstance().chatManager().saveMessage(msgNotification);
                                messageList.refresh();
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        switch (e.getErrorCode()) {
                                            case 303:
                                                Toast.makeText(getActivity(), "服务器出错!", Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                });
                            }
                        }
                    }).start();

                    break;
                default:
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 11;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //messagee recall
                else if (message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)) {
                    return MESSAGE_TYPE_RECALL;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // voice call or video call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) || message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
                }
                //recall message
                else if (message.getBooleanAttribute(Constant.MESSAGE_TYPE_RECALL, false)) {
                    return new EaseChatRowRecall(getActivity(), message, position, adapter);
                }
            }
            return null;
        }

    }
}