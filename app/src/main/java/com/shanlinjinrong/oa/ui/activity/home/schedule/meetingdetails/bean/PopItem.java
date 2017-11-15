package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

/**
 * Created by 丁 on 2017/10/12.
 */

public class PopItem {
    private String content;
    private boolean enable;
    private boolean isSelect;
    private int dateType;

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    public PopItem(String content, boolean enable, boolean isSelect) {
        this.enable = enable;
        this.content = content;
        this.isSelect = isSelect;
    }



    public String getContent() {
        return content;
    }

    public PopItem setContent(String content) {
        this.content = content;
        return this;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public PopItem setSelect(boolean select) {
        isSelect = select;
        return this;
    }

    public boolean isEnable() {
        return enable;
    }

    public PopItem setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }
}
