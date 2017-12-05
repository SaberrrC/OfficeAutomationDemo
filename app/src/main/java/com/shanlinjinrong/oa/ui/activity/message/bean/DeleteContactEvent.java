package com.shanlinjinrong.oa.ui.activity.message.bean;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class DeleteContactEvent {

    private String event;
    private int position;
    private String code;
    private int size;

    public DeleteContactEvent(String event, int position, String code) {
        this.event = event;
        this.position = position;
        this.code = code;
    }

    public DeleteContactEvent(String event, int position, String code, int size) {
        this.event = event;
        this.position = position;
        this.code = code;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
