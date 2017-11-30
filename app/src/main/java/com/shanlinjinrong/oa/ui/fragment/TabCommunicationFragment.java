package com.shanlinjinrong.oa.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.main.MainActivity;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.message.GroupChatListActivity;
import com.shanlinjinrong.oa.ui.base.BaseFragment;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h3>Description: 首页通讯列表页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabCommunicationFragment extends BaseFragment {

    //    private EaseConversationListFragment conversationListFragment;
    public ConversationListFragment myConversationListFragment;
    //    String userInfo_self;
    //    String userInfo;
    @BindView(R.id.topView)
    CommonTopView mTopView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_communication_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    protected void lazyLoadData() {

    }

    private EMConversation mConversation;
    private String         mTitle;

    private void initData() {
        if (myConversationListFragment == null) {
            myConversationListFragment = new ConversationListFragment();
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.easeConversationListFragment, myConversationListFragment).commit();
        myConversationListFragment.setConversationListItemClickListener(conversation -> {
            EMMessage lastMessage = conversation.getLastMessage();
            if (lastMessage.getTo().equals("sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                mConversation = EMClient.getInstance().chatManager().getConversation(lastMessage.getFrom());
            } else {
                mConversation = EMClient.getInstance().chatManager().getConversation(lastMessage.getTo());
            }
            if (mConversation != null) {
                String extField = mConversation.getExtField();
                try {
                    JSONObject jsonObject = new JSONObject(extField);
                    mTitle = jsonObject.getString("userName");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //TODO 暂做修改 消息透传部分
            //            userInfo = lastMessage.getStringAttribute("userInfo", "");
            //            userInfo_self = lastMessage.getStringAttribute("userInfo_self", "");
            //            startActivity(new Intent(getActivity(), EaseChatMessageActivity.class).putExtra("userId", conversation.conversationId()).putExtra("userInfo_self", userInfo_self).putExtra("userInfo", userInfo).putExtra("nikename", ""));
            //            Intent intent = new Intent(getActivity(), EaseChatMessageActivity.class);
            //            intent.putExtra("userCode", lastMessage.getStringAttribute("userCode", ""));
            //            intent.putExtra("userName", lastMessage.getStringAttribute("userName", ""));
            //            intent.putExtra("userId", lastMessage.getStringAttribute("userId", ""));
            //            intent.putExtra("userHead", lastMessage.getStringAttribute("userHead", ""));
            //            startActivity(intent);


            Intent intent = new Intent(getActivity(), EaseChatMessageActivity.class);
            intent.putExtra("userCodeSelf", "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
            intent.putExtra("userNameSelf", AppConfig.getAppConfig(AppManager.mContext).getPrivateName());
            intent.putExtra("userIdSelf", AppConfig.getAppConfig(AppManager.mContext).getPrivateUid());
            intent.putExtra("userHeadSelf", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PORTRAITS));
            intent.putExtra("title", mTitle);

            //TODO 获取携带的消息
            String userInfo = lastMessage.getStringAttribute("userInfo", "");
            String from = lastMessage.getFrom();
            String to = lastMessage.getTo();
            Bundle bundle = new Bundle();
            if (from.contains(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                bundle.putString(EaseConstant.EXTRA_USER_ID, to);
            }
            if (to.contains(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                bundle.putString(EaseConstant.EXTRA_USER_ID, from);
            }
            if (!userInfo.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(userInfo);
                    if (lastMessage.getTo().equals("sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                        intent.putExtra("toChatUsername", lastMessage.getFrom());
                    } else {
                        intent.putExtra("toChatUsername", lastMessage.getTo());
                    }
                    intent.putExtra("userCode", jsonObject.getString("userCode"));
                    intent.putExtra("userName", jsonObject.getString("userName"));
                    intent.putExtra("userId", jsonObject.getString("userId"));
                    intent.putExtra("userHead", jsonObject.getString("userHead"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            startActivity(intent);
        });
        mTopView.getRightView().setOnClickListener(view -> {
            //TODO 跳转群聊列表
            Intent intent = new Intent(getContext(), GroupChatListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        LogUtils.e("刷新下主界面消息的数量");
        //刷新下主界面消息的数量
        MainActivity mainController = (MainActivity) getActivity();
        mainController.refreshCommCount();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}