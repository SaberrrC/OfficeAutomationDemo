package com.shanlinjinrong.oa.model;

/**
 * ProjectName: dev-beta
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by Tsui on Date:2017/3/23 13:33
 * Description:会议成员实体类
 */
public class JoinVideoMember {
    private String uid;
    private String code;
    private String userName;
    private String post;
    private String state;//状态
    private boolean isHand;//是够举手

    public JoinVideoMember(String uid, String userName, String post) {
        this.uid = uid;
        this.userName = userName;
        this.post = post;
    }

    public JoinVideoMember(String uid, String code, String userName, String post) {
        this.uid = uid;
        this.code = code;
        this.userName = userName;
        this.post = post;
    }

    @Override
    public String toString() {
        return "JoinVideoMember{" +
                "uid='" + uid + '\'' +
                ", code='" + code + '\'' +
                ", userName='" + userName + '\'' +
                ", post='" + post + '\'' +
                ", state='" + state + '\'' +
                ", isHand=" + isHand +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isHand() {
        return isHand;
    }

    public void setHand(boolean hand) {
        isHand = hand;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
