package com.shanlinjinrong.oa.ui.activity.message.bean;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class GroupEventListener {
    private int event;

    private boolean isEvent;

    private String memberCode;

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

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }
}
