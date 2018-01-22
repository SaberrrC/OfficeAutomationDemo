package com.hyphenate.easeui.adapter.bean;

import com.hyphenate.chat.EMConversation;

/**
 * Created by dell on 2017/12/22.
 */

public class NotifyConversation {

    private EMConversation conversation;

    private int flag;

    public NotifyConversation() {

    }

    public EMConversation getConversation() {
        return conversation;
    }

    public void setConversation(EMConversation conversation) {
        this.conversation = conversation;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }
}
