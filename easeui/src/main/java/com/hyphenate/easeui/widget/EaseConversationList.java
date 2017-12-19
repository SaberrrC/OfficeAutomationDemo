package com.hyphenate.easeui.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.UserDetailsBean;
import com.hyphenate.easeui.adapter.EaseConversationAdapter;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

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

        for (int i = 0; i < conversationList.size(); i++) {
            if (conversationList.get(i).conversationId().length() > 11) {
                conversationId = conversationList.get(i).conversationId().substring(0, 12);
            } else {
                conversationId = conversationList.get(i).conversationId();
            }

            if (FriendsInfoCacheSvc.getInstance(getContext()).getNickName(conversationId).equals("") && conversationList.get(i).getType() != EMConversation.EMConversationType.GroupChat) {
                String userCode = conversationList.get(i).conversationId().substring(3, conversationList.get(i).conversationId().length());
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
                kjHttp.get("http://testoa.shanlinjinrong.com/webApi/user/getinfo/?code=" + userCode, httpParams, new HttpCallBack() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        UserDetailsBean userDetailsBean = new Gson().fromJson(t, UserDetailsBean.class);
                        if (userDetailsBean != null) {
                            try {
                                switch (userDetailsBean.getCode()) {
                                    case 200:
                                        FriendsInfoCacheSvc.
                                                getInstance(getContext()).addOrUpdateFriends(new Friends("sl_" + userDetailsBean.getData().get(0).getCode(),
                                                userDetailsBean.getData().get(0).getUsername(), "http://" + userDetailsBean.getData().get(0).getImg(), "", "", "", "", "", ""));
                                        if (adapter != null) {
                                            adapter.notifyDataSetChanged();
                                        }
                                        break;
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
            } else {
                existConversation.add(conversationList.get(i));
            }
        }

        adapter = new EaseConversationAdapter(context, 0, existConversation);
        adapter.setCvsListHelper(conversationListHelper);
        adapter.setPrimaryColor(primaryColor);
        adapter.setPrimarySize(primarySize);
        adapter.setSecondaryColor(secondaryColor);
        adapter.setSecondarySize(secondarySize);
        adapter.setTimeColor(timeColor);
        adapter.setTimeSize(timeSize);
        setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case MSG_REFRESH_ADAPTER_DATA:
                    if (adapter != null) {
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
        if (!handler.hasMessages(MSG_REFRESH_ADAPTER_DATA)) {
            handler.sendEmptyMessage(MSG_REFRESH_ADAPTER_DATA);
        }
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
