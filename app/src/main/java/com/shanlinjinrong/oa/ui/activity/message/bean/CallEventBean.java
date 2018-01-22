package com.shanlinjinrong.oa.ui.activity.message.bean;

/**
 * 作者：王凤旭
 * 时间：2017/11/30
 * 描述：
 */

public class CallEventBean {
    private String event;
    private String userName;
    private String portraits;

    public CallEventBean(String event, String userName, String portraits) {
        this.event = event;
        this.userName = userName;
        this.portraits = portraits;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPortaits() {
        return portraits;
    }

    public void setPortaits(String portraits) {
        this.portraits = portraits;
    }
}
