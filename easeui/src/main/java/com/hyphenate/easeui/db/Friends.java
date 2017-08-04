package com.hyphenate.easeui.db;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/18.
 * 好友实体类
 */
public class Friends implements Serializable {


    public static final String COLUMNNAME_USERID = "user_id";
    public static final String COLUMNNAME_NICKNAME = "nickname";
    public static final String COLUMNNAME_PORTRAIT = "portrait";

    public String user_id;

    public String nickname;

    public String portrait;

    public Friends() {

    }

    public Friends(String user_id, String nickname, String portrait) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.portrait = portrait;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }


}
