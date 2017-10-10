package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

/**
 * Created by tonny on 2017/9/30.
 */

public class ReservationRecordBean {

    //0:过期会议  1：未过期会议
    private String state;

    private String date;

    private String meetingContent;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMeetingContent() {
        return meetingContent;
    }

    public void setMeetingContent(String meetingContent) {
        this.meetingContent = meetingContent;
    }
}
