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

    private int index;

    private String startTime;

    private String date;

    private String endTime;

    private boolean isStart;

    private int startHour;
    private int endHour;
    private int startMin;
    private int endMin;

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getEndMin() {
        return endMin;
    }

    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }


    public SelectedWeekCalendarEvent(String date, String event) {
        this.date = date;
        this.event = event;
    }
    public SelectedWeekCalendarEvent(String event, boolean isStart, int startHour, int startMin) {
        this.event = event;
        this.isStart = isStart;
        this.startHour = startHour;
        this.startMin = startMin;
    }

    public SelectedWeekCalendarEvent(String event, int endHour, int endMin, boolean isStart) {
        this.event = event;
        this.isStart = isStart;
        this.endHour = endHour;
        this.endMin = endMin;
    }

    public SelectedWeekCalendarEvent(String event, String startTime, boolean isStart) {
        this.event = event;
        this.startTime = startTime;
        this.isStart = isStart;
    }

    public SelectedWeekCalendarEvent(String event, boolean isStart, String endTime) {
        this.event = event;
        this.endTime = endTime;
        this.isStart = isStart;
    }

    public SelectedWeekCalendarEvent(String event, String startTime, String endTime) {
        this.event = event;
        this.startTime = startTime;
        this.endTime = endTime;
    }

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

    public SelectedWeekCalendarEvent(int position, int index, String date, String event) {
        this.position = position;
        this.event = event;
        this.index = index;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
