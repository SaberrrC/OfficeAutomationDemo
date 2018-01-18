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

    private float rowY;

    private float rowX;


    public SelectedWeekCalendarEvent(int position, String event) {
        this.position = position;
        this.event = event;
    }

    public SelectedWeekCalendarEvent(int position, String event, float rowX, float rowY) {
        this.position = position;
        this.event = event;
        this.rowY = rowY;
        this.rowX = rowX;
    }

    public float getRowY() {
        return rowY;
    }

    public void setRowY(float rowY) {
        this.rowY = rowY;
    }

    public float getRowX() {
        return rowX;
    }

    public void setRowX(float rowX) {
        this.rowX = rowX;
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
