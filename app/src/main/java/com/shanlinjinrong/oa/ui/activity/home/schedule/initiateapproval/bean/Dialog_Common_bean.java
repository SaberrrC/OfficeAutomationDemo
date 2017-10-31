package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean;


public class Dialog_Common_bean {

    private String content;

    private boolean isSelected;

    private String title;

    private String selectedTitle;

    public Dialog_Common_bean() {

    }

    public Dialog_Common_bean(String content, boolean isSelected) {
        this.content = content;
        this.isSelected = isSelected;
    }

    public Dialog_Common_bean(String content, boolean isSelected, String title) {
        this.content = content;
        this.isSelected = isSelected;
        this.title = title;
    }

    public String getSelectedTitle() {
        return selectedTitle;
    }

    public void setSelectedTitle(String selectedTitle) {
        this.selectedTitle = selectedTitle;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
