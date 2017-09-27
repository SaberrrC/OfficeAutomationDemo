package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean;

/**
 * Created by tonny on 2017/9/20.
 */

public class WorkContentBean {

    private String Title;
    private String State;

    public WorkContentBean() {
    }

    public WorkContentBean(String title, String state) {
        Title = title;
        State = state;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
