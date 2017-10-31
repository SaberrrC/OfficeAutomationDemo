package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean;


public class CommonalityInitiateBean {

    private String content;

    private String selectedTitle;

    private String index;

    public CommonalityInitiateBean(){

    }

    public CommonalityInitiateBean(String content, String index) {
        this.content = content;
        this.index = index;
    }

    public CommonalityInitiateBean(String content, String selectedTitle, String index) {
        this.content = content;
        this.selectedTitle = selectedTitle;
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSelectedTitle() {
        return selectedTitle;
    }

    public void setSelectedTitle(String selectedTitle) {
        this.selectedTitle = selectedTitle;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
