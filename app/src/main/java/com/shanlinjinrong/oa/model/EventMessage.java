package com.shanlinjinrong.oa.model;

/**
 * Created by ${GaoBin} on 2017/8/3 0003.
 */

public class EventMessage {
    private String str;
    private int type;

    public EventMessage() {

    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getType() {
        return type;
    }

    public EventMessage setType(int type) {
        this.type = type;
        return this;
    }
}
