package com.shanlinjinrong.oa.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMConversation;
//import com.hyphenate.chatuidemo.ui.EaseConversationListFragment;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.main.MainController;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.base.BaseFragment;
import com.shanlinjinrong.oa.utils.LogUtils;

import butterknife.ButterKnife;

/**
 * <h3>Description: 首页通讯列表页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabCommunicationFragment extends BaseFragment  {

//    private EaseConversationListFragment conversationListFragment;
    public ConversationListFragment myConversationListFragment;
    private RelativeLayout view;
    String userInfo_self;
    String userInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = (RelativeLayout) inflater.inflate(R.layout.tab_communication_fragment, container, false);
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

    private void initData() {
        if (myConversationListFragment == null) {
            myConversationListFragment = new ConversationListFragment();
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.easeConversationListFragment, myConversationListFragment).commit();
        myConversationListFragment
                .setConversationListItemClickListener(new EaseConversationListFragment
                        .EaseConversationListItemClickListener() {

                    @Override
                    public void onListItemClicked(EMConversation conversation) {
                        EMMessage lastMessage = conversation.getLastMessage();
                        userInfo = lastMessage.getStringAttribute("userInfo", "");
                        userInfo_self = lastMessage.getStringAttribute("userInfo_self", "");
                        startActivity(new Intent(getActivity(), EaseChatMessageActivity.class)
                                .putExtra("u_id", conversation.conversationId())
                                .putExtra("userInfo_self",userInfo_self)
                                .putExtra("userInfo", userInfo)
                                .putExtra("nike_name", ""));
                    }
                });
    }

    @Override
    public void onResume() {
        LogUtils.e("刷新下主界面消息的数量");
        //刷新下主界面消息的数量
        MainController mainController = (MainController) getActivity();
        mainController.refreshCommCount();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}