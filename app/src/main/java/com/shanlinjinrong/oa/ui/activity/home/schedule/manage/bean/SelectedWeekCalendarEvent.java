package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean;

import java.io.Serializable;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/17
 * 功能描述：
 */

public class SelectedWeekCalendarEvent implements Serializable {

    private int position;

    private String event;

    public SelectedWeekCalendarEvent(int position, String event) {
        this.position = position;
        this.event = event;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
