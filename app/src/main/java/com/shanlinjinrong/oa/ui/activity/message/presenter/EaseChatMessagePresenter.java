package com.shanlinjinrong.oa.ui.activity.message.presenter;


import android.util.Pair;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatMessageContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class EaseChatMessagePresenter extends HttpPresenter<EaseChatMessageContract.View> implements EaseChatMessageContract.Presenter {

    @Inject
    public EaseChatMessagePresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void getUnReadCount() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        if (conversations.isEmpty()) {

        }
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //java.lang.NullPointerException: Attempt to invoke virtual method
                    // 'long com.hyphenate.chat.EMMessage.getMsgTime()' on a null object reference
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        int tempCount = 0;
        for (int i = 0; i < list.size(); i++) {
            int size = list.get(i).getUnreadMsgCount();
            tempCount = tempCount + size;
        }
        mView.setUnreadCount(tempCount);
    }
}