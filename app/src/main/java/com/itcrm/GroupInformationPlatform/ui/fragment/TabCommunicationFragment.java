package com.itcrm.GroupInformationPlatform.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.ui.activity.EaseChatMessageActivity;
import com.itcrm.GroupInformationPlatform.ui.activity.MainController;
import com.itcrm.GroupInformationPlatform.ui.activity.TabContactsActivity;
import com.itcrm.GroupInformationPlatform.ui.base.BaseFragment;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import butterknife.ButterKnife;

/**
 * <h3>Description: 首页消息列表页面</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class TabCommunicationFragment extends BaseFragment {

    private EaseConversationListFragment conversationListFragment;
    private RelativeLayout view;

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

    private void initData() {
        if (conversationListFragment == null) {
            conversationListFragment = new EaseConversationListFragment();
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.easeConversationListFragment, conversationListFragment).commit();
        conversationListFragment
                .setConversationListItemClickListener(new EaseConversationListFragment
                        .EaseConversationListItemClickListener() {

                    @Override
                    public void onListItemClicked(EMConversation conversation) {
                        startActivity(new Intent(getActivity(), EaseChatMessageActivity.class)
                                .putExtra("u_id", conversation.getUserName())
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