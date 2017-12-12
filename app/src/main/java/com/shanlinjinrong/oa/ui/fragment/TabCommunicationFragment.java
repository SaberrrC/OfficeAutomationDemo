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
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.main.MainActivity;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.message.GroupChatListActivity;
import com.shanlinjinrong.oa.ui.base.BaseFragment;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * <h3>Description: 首页通讯列表页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabCommunicationFragment extends BaseFragment {

    public ConversationListFragment myConversationListFragment;
    @BindView(R.id.topView)
    CommonTopView mTopView;
    private String mNickName;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_communication_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        mTopView.getRightView().setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), GroupChatListActivity.class);
            startActivity(intent);
        });
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
        myConversationListFragment.setConversationListItemClickListener(conversation -> {
            if (conversation.isGroup()) {
                String groupName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(conversation.conversationId());
                startActivity(new Intent(getActivity(), EaseChatMessageActivity.class)
                        .putExtra("u_id", conversation.conversationId())
                        .putExtra("groupTitle", groupName)
                        .putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP));
            } else {
                EMMessage lastMessage = conversation.getLastMessage();
                //Title 名字
                mNickName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(conversation.conversationId());
                if (lastMessage.getFrom().contains("admin") || lastMessage.getTo().contains("admin")) {
                    mNickName = "会议邀请";
                } else if (lastMessage.getFrom().contains("notice") || lastMessage.getTo().contains("notice")) {
                    mNickName = "公告通知";
                }
                startActivity(new Intent(getActivity(), EaseChatMessageActivity.class)
                        .putExtra("u_id", conversation.conversationId())
                        .putExtra("title", mNickName)
                        .putExtra("message_to", lastMessage.getTo())
                        .putExtra("message_from", lastMessage.getFrom())
                        .putExtra(EaseConstant.EXTRA_CHAT_TYPE, 0));
            }
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
}