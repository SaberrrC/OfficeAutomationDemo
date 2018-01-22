package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean;

import java.io.Serializable;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/20
 * 功能描述：
 */

public class UpdateTitleBean implements Serializable{
    private String title;

    private String event;

    public UpdateTitleBean(String title, String event) {
        this.title = title;
        this.event = event;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
