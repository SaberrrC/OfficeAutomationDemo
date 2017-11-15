package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean;

public class SelectedTypeBean {

    private String event;
    private String selectedType;
    private int position;
    private int isBegin;
    public String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public SelectedTypeBean() {

    }

    public SelectedTypeBean(String event) {
        this.event = event;
    }

    public SelectedTypeBean(String event, String selectedType) {
        this.event = event;
        this.selectedType = selectedType;
    }

    public SelectedTypeBean(String event, int position) {
        this.event = event;
        this.position = position;
    }

    public SelectedTypeBean(String event, String selectedType, int position) {
        this.event = event;
        this.selectedType = selectedType;
        this.position = position;
    }

    public SelectedTypeBean(String event, String selectedType, int position, int isBegin) {
        this.event = event;
        this.selectedType = selectedType;
        this.position = position;
        this.isBegin = isBegin;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(int isBegin) {
        this.isBegin = isBegin;
    }
}
