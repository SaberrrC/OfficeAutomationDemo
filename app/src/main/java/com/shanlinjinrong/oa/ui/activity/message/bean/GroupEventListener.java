package com.shanlinjinrong.oa.ui.activity.message.bean;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class GroupEventListener {
    private int event;

    private boolean isEvent;

    public GroupEventListener(int event) {
        this.event = event;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
