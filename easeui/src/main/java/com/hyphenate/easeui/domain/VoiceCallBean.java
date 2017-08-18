package com.hyphenate.easeui.domain;

/**
 * 作者：王凤旭
 * 时间：2017/8/4
 * 描述：
 */

public class VoiceCallBean {
    String username;

    String nike;

    String meUsername;

    String meUserPortrait;

    boolean isComingCall;

    public VoiceCallBean() {
    }

    public VoiceCallBean(String username, String nike, String meUsername, String meUserPortrait, boolean isComingCall) {
        this.username = username;
        this.nike = nike;
        this.meUsername = meUsername;
        this.meUserPortrait = meUserPortrait;
        this.isComingCall = isComingCall;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNike() {
        return nike;
    }

    public void setNike(String nike) {
        this.nike = nike;
    }

    public String getMeUsername() {
        return meUsername;
    }

    public void setMeUsername(String meUsername) {
        this.meUsername = meUsername;
    }

    public String getMeUserPortrait() {
        return meUserPortrait;
    }

    public void setMeUserPortrait(String meUserPortrait) {
        this.meUserPortrait = meUserPortrait;
    }

    public boolean getIsComingCall() {
        return isComingCall;
    }

    public void setIsComingCall(boolean isComingCall) {
        this.isComingCall = isComingCall;
    }
}
