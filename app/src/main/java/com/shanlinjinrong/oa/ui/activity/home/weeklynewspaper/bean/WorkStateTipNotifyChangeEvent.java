package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean;

/**
 * Created by tonny on 2017/9/21.
 */

public class WorkStateTipNotifyChangeEvent {

    private String state;
    private String position;
    private boolean mIsWorkContent;

    public boolean isWorkContent() {
        return mIsWorkContent;
    }

    public void setWorkContent(boolean workContent) {
        mIsWorkContent = workContent;
    }

    public WorkStateTipNotifyChangeEvent() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
