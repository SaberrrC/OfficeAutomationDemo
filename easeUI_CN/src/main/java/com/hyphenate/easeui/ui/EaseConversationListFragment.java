package com.hyphenate.easeui.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.utils.LogUtils;
import com.hyphenate.easeui.widget.EaseConversationList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * conversation list fragment
 */
public class EaseConversationListFragment extends EaseBaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        EMMessageListener {
    private final static int MSG_REFRESH = 2;
    //    protected EditText query;
//    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;

    protected boolean isConflict;

    protected EMConversationListener convListener = new EMConversationListener() {

        @Override
        public void onCoversationUpdate() {
            refresh();
        }

    };
    private View ivEmpty;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PopupWindow popupWindow;
    private LinearLayout mRootView;
    private AlertDialog alertDialog;
    private String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {

        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.chatlist_swipe_layout);
        mRootView = (LinearLayout) getView().findViewById(R.id.id_ll_rootView);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0EA7ED"),
                Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        //Tsui2016-12-16注释
//        query = (EditText) getView().findViewById(R.id.query);
        // button to clear content in search bar
//        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
        ivEmpty = getView().findViewById(R.id.iv_empty);
    }

    @Override
    protected void setUpView() {
        LogUtils.e("setUpView......");

        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);

        if (listItemClickListener != null) {
            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
            conversationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    userName = conversationListView.getItem(position).getUserName();
                    //长按删除
                    showTips("是否删除该会话", userName);

                    return true;
                }
            });
        }

        EMClient.getInstance().addConnectionListener(connectionListener);
        
        /*Tsui2016-12-16注释
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversationListView.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });*/
       /* clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });*/

        conversationListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.contextmenu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean b = EMClient.getInstance().chatManager().deleteConversation(userName, false);
        if (!b) {
            showToast("删除会话失败！请重试");
        } else {

            refresh();
        }
        return super.onContextItemSelected(item);
    }


    public void showTips(String msg, final String userName) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.public_dialog, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("删除提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(getContext(),
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //删除和某个user会话，如果需要保留聊天记录，传false
                        boolean b = EMClient.getInstance().chatManager().deleteConversation(userName, false);
                        if (!b) {
                            showToast("删除会话失败！请重试");
                        } else {

                            refresh();
                        }

                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                Color.parseColor("#ff0000"));
    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH: {
                    conversationList.clear();
                    List<EMConversation> list = loadConversationList();
                    conversationList.addAll(list);

                    if (list.size() == 0) {
                        conversationListView.setVisibility(View.INVISIBLE);
                        ivEmpty.setVisibility(View.VISIBLE);
                    } else {
                        ivEmpty.setVisibility(View.GONE);
                        conversationListView.setVisibility(View.VISIBLE);
                    }

                    conversationListView.refresh();
                    break;
                }
                default:
                    break;
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    /**
     * connected to server
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
    }


    /**
     * refresh ui
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }

    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }


        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("onResume()执行了");
        EMClient.getInstance().chatManager().addMessageListener(this);
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        refresh();
        for (EMMessage message : messages) {

            LogUtils.e("收到消息-》" + message.toString());

            //获取自定义的名称和头像
            String conversationId = message.getStringAttribute("conversationId", "");
            String nickname = message.getStringAttribute("nickname", "");
            String avatarURL = message.getStringAttribute("avatarURL", "");
            if (avatarURL.equals("http://")) {
                avatarURL="";
            }
            LogUtils.e("获取对方的名称和头像");
            LogUtils.e("conversationId-》" + conversationId);
            LogUtils.e("nickname-》" + nickname);
            LogUtils.e("avatarURL-》" + avatarURL);


            FriendsInfoCacheSvc.getInstance(getContext()).addOrUpdateFriends(new
                    Friends(conversationId, nickname, avatarURL));
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }


    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         *
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * set conversation list item click listener
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

}
