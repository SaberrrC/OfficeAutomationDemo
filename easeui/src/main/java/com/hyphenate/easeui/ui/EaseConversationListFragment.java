package com.hyphenate.easeui.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.UserDetailsBean;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.event.OnCountRefreshEvent;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * conversation list fragment
 */
public class EaseConversationListFragment extends EaseBaseFragment {
    private final static int MSG_REFRESH = 2;
    protected EditText    query;
    protected ImageButton clearSearch;
    protected boolean     hidden;
    protected List<EMConversation> conversationList = new ArrayList<>();
    public    EaseConversationList conversationListView;
    protected FrameLayout          errorItemContainer;

    protected boolean isConflict;

    private TextView tvErrorView;
    private       long lastClickTime = 0;
    private final int  REFRESH_DATA  = -3, REFRESH_SETUP = -4;
    private boolean mIsSetup;
    private List<EMConversation> list                       = new ArrayList<>();
    private List<EMConversation> conversationIncompleteList = new ArrayList<>();

    private static final String APP_CONFIG              = "app_config";
    public static final  String DEFAULT_ARGUMENTS_VALUE = "";
    private EMConversation emConversation;
    Context mContext;
    private int    tempCount;
    private String userCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mContext = getActivity();
        if (!EventBus.getDefault().isRegistered(mContext)) {
            EventBus.getDefault().register(mContext);
        }
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        tvErrorView = (TextView) getView().findViewById(R.id.tv_error_layout);
        query = (EditText) getView().findViewById(R.id.query);
        // button to clear content in search bar
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
    }

    @Override
    protected void setUpView() {
//        conversationList.addAll(loadConversationList());
//        conversationListView.init(conversationList);
        loadConversationList();
        conversationListView.init(conversationList);
        if (listItemClickListener != null) {
            conversationListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= conversationList.size()) {
                        return;
                    }
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    if (currentTime - lastClickTime < 1000) {
                        lastClickTime = currentTime;
                        return;
                    }
                    lastClickTime = currentTime;
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
        }

        EMClient.getInstance().addConnectionListener(connectionListener);

        conversationListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
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
    private OnCountRefreshEvent onCountRefreshEvent = new OnCountRefreshEvent();

    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;
                case REFRESH_SETUP:
                    conversationList.clear();
                    conversationList.addAll(list);
                    conversationListView.refresh();
                    mIsSetup = true;
                    RefreshBadge();
                    break;

                case MSG_REFRESH: {
                    loadConversationList();
                    break;
                }
                case REFRESH_DATA:
                    if (mIsSetup) {
                        conversationList.clear();
                        conversationList.addAll(list);
                        conversationListView.refresh();
                    }
                    //刷新badge
                    RefreshBadge();
                    break;
                default:
                    break;
            }
        }
    };

    private void RefreshBadge() {
        //刷新badge
        try {
            tempCount = 0;
            for (int i = 0; i < list.size(); i++) {
                int size = list.get(i).getUnreadMsgCount();
                tempCount = tempCount + size;
            }
            onCountRefreshEvent.setUnReadMsgCount(tempCount);
            EventBus.getDefault().post(onCountRefreshEvent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //错误页面展示
        try {
            if (conversationList != null && conversationList.size() == 0) {
                tvErrorView.setVisibility(View.VISIBLE);
            } else {
                tvErrorView.setVisibility(View.GONE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

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
    public int loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.clear();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        try {
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    //过滤
                    conversationIncompleteList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        EMMessage lastMessage = list.get(i).getLastMessage();
                        if (lastMessage.getChatType() == EMMessage.ChatType.GroupChat) {
                            String nickName = FriendsInfoCacheSvc.getInstance(getContext()).getNickName(lastMessage.conversationId());
                            if (TextUtils.isEmpty(nickName) || nickName.equals("匿名群组")) {
                                EMConversation emConversation = list.get(i);
                                conversationIncompleteList.add(emConversation);
                            }

                        } else if (lastMessage.getChatType() == EMMessage.ChatType.Chat) {
                            if (lastMessage.conversationId().contains("admin") || lastMessage.conversationId().contains("notice")) {
                                continue;
                            }

                            String conversationId = lastMessage.conversationId().substring(0, 12);
                            String nickName = FriendsInfoCacheSvc.getInstance(getContext()).getNickName(conversationId);
                            if (TextUtils.isEmpty(nickName) || nickName.equals("匿名用户")) {
                                EMConversation emConversation = list.get(i);
                                conversationIncompleteList.add(emConversation);
                            }
                        }
                    }

                    if (conversationIncompleteList.size() != 0) {
                        list.removeAll(conversationIncompleteList);
                    }

                    if (!mIsSetup) {
                        if (!handler.hasMessages(REFRESH_SETUP)) {
                            handler.sendEmptyMessage(REFRESH_SETUP);
                        }
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = list.size() - 1; j > i; j--) {
                                if (list.get(i) == list.get(j)) {
                                    list.remove(j);
                                }
                            }
                        }

                        if (!handler.hasMessages(REFRESH_DATA)) {
                            handler.sendEmptyMessage(REFRESH_DATA);
                        }
                    }

                    //加载未刷新的
                    for (int i = 0; i < conversationIncompleteList.size(); i++) {
                        emConversation = conversationIncompleteList.get(i);
                        EMMessage lastMessage = conversationIncompleteList.get(i).getLastMessage();
                        if (lastMessage.getChatType() == EMMessage.ChatType.GroupChat) {
                            try {
                                EMGroup groupFromServer = EMClient.getInstance().groupManager().getGroupFromServer(lastMessage.conversationId());
                                FriendsInfoCacheSvc.getInstance(getContext()).addOrUpdateFriends(new Friends(groupFromServer.getGroupId(), groupFromServer.getGroupName(), ""));
                                list.add(0, conversationIncompleteList.get(i));
                                if (!handler.hasMessages(MSG_REFRESH)) {
                                    handler.sendEmptyMessage(MSG_REFRESH);
                                }
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                                try {
                                    FriendsInfoCacheSvc.getInstance(getContext()).addOrUpdateFriends(new Friends(lastMessage.conversationId(), "匿名群组", ""));
                                    list.add(0, conversationIncompleteList.get(i));
                                    if (!handler.hasMessages(MSG_REFRESH)) {
                                        handler.sendEmptyMessage(MSG_REFRESH);
                                    }
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        } else if (lastMessage.getChatType() == EMMessage.ChatType.Chat) {
                            String conversationId = lastMessage.conversationId().substring(0, 12);
                            userCode = conversationId.substring(3, conversationId.length());
                            String token = getContext().getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE).getString("pref_key_private_token", DEFAULT_ARGUMENTS_VALUE);
                            String uid = getContext().getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE).getString("pref_key_user_uid", DEFAULT_ARGUMENTS_VALUE);
                            KJHttp kjHttp = new KJHttp();
                            HttpConfig config = new HttpConfig();
                            HttpConfig.TIMEOUT = 30000;
                            kjHttp.setConfig(config);
                            kjHttp.cleanCache();
                            HttpParams httpParams = new HttpParams();
                            httpParams.putHeaders("token", token);
                            httpParams.putHeaders("uid", uid);
                            //TODO 生产
                            kjHttp.get(EaseConstant.PHP_URL + "user/getinfo/?code=" + userCode, httpParams, new HttpCallBack() {

                                private UserDetailsBean userDetailsBean;

                                @Override
                                public void onFailure(int errorNo, String strMsg) {
                                    super.onFailure(errorNo, strMsg);
                                    try {
                                        FriendsInfoCacheSvc.getInstance(getContext()).addOrUpdateFriends(new Friends("sl_" + userCode, "匿名用户", ""));
                                        list.add(0, emConversation);
                                        if (!handler.hasMessages(MSG_REFRESH)) {
                                            handler.sendEmptyMessage(MSG_REFRESH);
                                        }
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onSuccess(String t) {
                                    super.onSuccess(t);
                                    try {
                                        userDetailsBean = new Gson().fromJson(t, new TypeToken<UserDetailsBean>() {
                                        }.getType());
                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                    if (userDetailsBean != null) {
                                        try {
                                            switch (userDetailsBean.getCode()) {
                                                case 200:
                                                    Observable.create(new Observable.OnSubscribe<Object>() {
                                                        @Override
                                                        public void call(Subscriber<? super Object> subscriber) {
                                                            FriendsInfoCacheSvc.getInstance(getContext()).addOrUpdateFriends(new Friends("sl_" + userDetailsBean.getData().get(0).getCode(), userDetailsBean.getData().get(0).getUsername(), "http://" + userDetailsBean.getData().get(0).getImg()));
                                                            list.add(0, emConversation);
                                                            if (!handler.hasMessages(MSG_REFRESH)) {
                                                                handler.sendEmptyMessage(MSG_REFRESH);
                                                            }
                                                        }
                                                    }).subscribeOn(Schedulers.io())
                                                            .subscribe(new Action1<Object>() {
                                                                @Override
                                                                public void call(Object o) {

                                                                }
                                                            }, new Action1<Throwable>() {
                                                                @Override
                                                                public void call(Throwable throwable) {
                                                                    throwable.printStackTrace();
                                                                }
                                                            });
                                                    break;
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return tempCount;
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
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
//        if (!hidden) {
//            refresh();
//        }
//        conversationList.clear();
//        conversationList.addAll(loadConversationList());
//        conversationListView.refresh();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData() {
        if (mIsSetup) {
            conversationList.clear();
            conversationList.addAll(list);
            conversationListView.refresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(getContext())) {
            EventBus.getDefault().unregister(getContext());
        }
    }
}
