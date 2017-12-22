package com.hyphenate.easeui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.UserDetailsBean;
import com.hyphenate.easeui.adapter.EaseConversationAdapter;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.exceptions.HyphenateException;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EaseConversationList extends ListView {

    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;

    private static final String APP_CONFIG = "app_config";
    public static final String DEFAULT_ARGUMENTS_VALUE = "";


    protected final int MSG_REFRESH_ADAPTER_DATA = 0;

    protected Context context;
    protected EaseConversationAdapter adapter;
    protected List<EMConversation> conversations = new ArrayList<EMConversation>();
    protected List<EMConversation> passedListRef = null;
    protected List<EMConversation> existConversation = new ArrayList<EMConversation>();
    protected List<EMConversation> Conversation = new ArrayList<EMConversation>();
    private String conversationId;


    public EaseConversationList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EaseConversationList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseConversationList);
        primaryColor = ta.getColor(R.styleable.EaseConversationList_cvsListPrimaryTextColor, getResources().getColor(R.color.list_itease_primary_color));
        secondaryColor = ta.getColor(R.styleable.EaseConversationList_cvsListSecondaryTextColor, getResources().getColor(R.color.list_itease_secondary_color));
        timeColor = ta.getColor(R.styleable.EaseConversationList_cvsListTimeTextColor, getResources().getColor(R.color.list_itease_secondary_color));
        primarySize = ta.getDimensionPixelSize(R.styleable.EaseConversationList_cvsListPrimaryTextSize, 0);
        secondarySize = ta.getDimensionPixelSize(R.styleable.EaseConversationList_cvsListSecondaryTextSize, 0);
        timeSize = ta.getDimension(R.styleable.EaseConversationList_cvsListTimeTextSize, 0);
        ta.recycle();

    }

    public void init(List<EMConversation> conversationList) {
        this.init(conversationList, null);
    }

    public void init(List<EMConversation> conversationList, EaseConversationListHelper helper) {
        conversations = conversationList;
        if (helper != null) {
            this.conversationListHelper = helper;
        }
        adapter = new EaseConversationAdapter(context, 0, conversations);
        adapter.setCvsListHelper(conversationListHelper);
        adapter.setPrimaryColor(primaryColor);
        adapter.setPrimarySize(primarySize);
        adapter.setSecondaryColor(secondaryColor);
        adapter.setSecondarySize(secondarySize);
        adapter.setTimeColor(timeColor);
        adapter.setTimeSize(timeSize);
        setAdapter(adapter);
        requestLayout();
        adapter.notifyDataSetChanged();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REFRESH_ADAPTER_DATA:
                    if (adapter != null) {
                        requestLayout();
                        adapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public EMConversation getItem(int position) {
        return (EMConversation) adapter.getItem(position);
    }

    public void refresh() {
        RefreshConversation();
        if (!handler.hasMessages(MSG_REFRESH_ADAPTER_DATA)) {
            handler.sendEmptyMessage(MSG_REFRESH_ADAPTER_DATA);
        }
    }

    private void RefreshConversation() {
        //刷新会话列表
        Observable.create(new Observable.OnSubscribe<EMConversation>() {
            @Override
            public void call(final Subscriber<? super EMConversation> subscriber) {
                for (int j = 0; j < conversations.size(); j++) {
                    EMMessage lastMessage = conversations.get(j).getLastMessage();
                    //群组 会话
                    if (lastMessage.getChatType() == EMMessage.ChatType.GroupChat) {
                        String nickName = FriendsInfoCacheSvc.getInstance(getContext()).getNickName(conversations.get(j).conversationId());
                        if (TextUtils.isEmpty(nickName)) {
                            try {
                                EMConversation emConversation = conversations.get(j);
                                conversations.remove(j);
                                EMGroup groupFromServer = EMClient.getInstance().groupManager().getGroupFromServer(conversations.get(j).conversationId());
                                FriendsInfoCacheSvc.getInstance(getContext()).addOrUpdateFriends(new Friends(groupFromServer.getGroupId(), groupFromServer.getGroupName(), ""));
                                subscriber.onNext(emConversation);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //单聊
                    if (lastMessage.getChatType() == EMMessage.ChatType.Chat) {
                        if (conversations.get(j).conversationId().contains("admin") || conversations.get(j).conversationId().contains("notice")) {
                            continue;
                        }
                        String conversationId = "";
                        try {
                            conversationId = conversations.get(j).conversationId().substring(0, 12);
                        } catch (Throwable e) {
                            conversationId = conversations.get(j).conversationId();
                            e.printStackTrace();
                        }
                        String nickName = FriendsInfoCacheSvc.getInstance(getContext()).getNickName(conversationId);
                        if (TextUtils.isEmpty(nickName)) {
                            final EMConversation emConversation = conversations.get(j);
                            conversations.remove(j);
                            String userCode = conversationId.substring(3, conversationId.length());
                            String token = context.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE).getString("pref_key_private_token", DEFAULT_ARGUMENTS_VALUE);
                            String uid = context.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE).getString("pref_key_user_uid", DEFAULT_ARGUMENTS_VALUE);
                            KJHttp kjHttp = new KJHttp();
                            HttpConfig config = new HttpConfig();
                            HttpConfig.TIMEOUT = 30000;
                            kjHttp.setConfig(config);
                            kjHttp.cleanCache();
                            HttpParams httpParams = new HttpParams();
                            httpParams.putHeaders("token", token);
                            httpParams.putHeaders("uid", uid);
                            //TODO 生产
                            kjHttp.get("http://testoa.shanlinjinrong.com/webApi/user/getinfo/?code=" + userCode, httpParams, new HttpCallBack() {
                                @Override
                                public void onSuccess(String t) {
                                    super.onSuccess(t);
                                    final UserDetailsBean userDetailsBean = new Gson().fromJson(t, UserDetailsBean.class);
                                    if (userDetailsBean != null) {
                                        try {
                                            switch (userDetailsBean.getCode()) {
                                                case 200:
                                                    Observable.create(new Observable.OnSubscribe<Object>() {
                                                        @Override
                                                        public void call(Subscriber<? super Object> subscriber) {
                                                            FriendsInfoCacheSvc.
                                                                    getInstance(getContext()).
                                                                    addOrUpdateFriends(new Friends("sl_" + userDetailsBean.getData().get(0).getCode(),
                                                                            userDetailsBean.getData().get(0).getUsername(), "http://" + userDetailsBean.getData().get(0).getImg()));
                                                            subscriber.onCompleted();
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
                                                            }, new Action0() {
                                                                @Override
                                                                public void call() {
                                                                    subscriber.onNext(emConversation);
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
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<EMConversation>() {
                    @Override
                    public void call(EMConversation s) {
                        conversations.add(0, s);
                        requestLayout();
                        adapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        if (adapter != null)
                            requestLayout();
                            adapter.notifyDataSetChanged();
                    }
                });
    }

    public void filter(CharSequence str) {
        adapter.getFilter().filter(str);
    }


    private EaseConversationListHelper conversationListHelper;


    public interface EaseConversationListHelper {
        /**
         * set content of second line
         *
         * @param lastMessage
         * @return
         */
        String onSetItemSecondaryText(EMMessage lastMessage);
    }

    public void setConversationListHelper(EaseConversationListHelper helper) {
        conversationListHelper = helper;
    }
}
