package com.shanlinjinrong.oa.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.main.MainActivity;
import com.shanlinjinrong.oa.ui.activity.message.EaseChatMessageActivity;
import com.shanlinjinrong.oa.ui.activity.message.GroupChatListActivity;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupEventListener;
import com.shanlinjinrong.oa.ui.base.BaseFragment;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h3>Description: 首页通讯列表页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabCommunicationFragment extends BaseFragment {

    public ConversationListFragment myConversationListFragment;
    @BindView(R.id.topView)
    CommonTopView mTopView;
    private String mNickName;
    private final int REQUESTCODE = 101, RESULTSUCCESS = -2,GROUPNAMEMODIFICATION = -3;
    private long lastClickTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_communication_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime < 1000) {
                lastClickTime = currentTime;
                return;
            }
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
                startActivityForResult(new Intent(getActivity(), EaseChatMessageActivity.class)
                        .putExtra("u_id", conversation.conversationId())
                        .putExtra("title", mNickName)
                        .putExtra("message_to", lastMessage.getTo())
                        .putExtra("message_from", lastMessage.getFrom())
                        .putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE), REQUESTCODE);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshGroup(GroupEventListener event) {
        switch (event.getEvent()) {
            case Constants.MODIFICATIONNAME:
                if (myConversationListFragment != null) {
                    myConversationListFragment.refresh();
                }
                break;
            case Constants.GROUPDISSOLVE:
                if (myConversationListFragment != null) {
                    myConversationListFragment.refresh();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULTSUCCESS:
                //Intent intent = new Intent(getContext(), GroupChatListActivity.class);
                //startActivity(intent);
                break;
            case GROUPNAMEMODIFICATION: //更改名称 刷新
                if (myConversationListFragment != null) {
                    myConversationListFragment.refresh();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}