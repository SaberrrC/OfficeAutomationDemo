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
    public static final String COLUMNNAME_SEX = "userSex";
    public static final String COLUMNNAME_PHONE = "userPhone";
    public static final String COLUMNNAME_POST = "userPost";
    public static final String COLUMNNAME_DEOARTMENT = "userDepartment";
    public static final String COLUMNNAME_EMAIL = "userEmail";

    public String user_id;
    public String nickname;
    public String portrait;
    public String userSex;
    public String userPhone;
    public String userPost;
    public String userDepartment;
    public String userEmail;



    public Friends() {

    }

    public Friends(String user_id, String nickname, String portrait, String userSex, String userPhone, String userPost, String userDepartment, String userEmail) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.portrait = portrait;
        this.userSex = userSex;
        this.userPhone = userPhone;
        this.userPost = userPost;
        this.userDepartment = userDepartment;
        this.userEmail = userEmail;
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

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPost() {
        return userPost;
    }

    public void setUserPost(String userPost) {
        this.userPost = userPost;
    }

    public String getUserDepartment() {
        return userDepartment;
    }

    public void setUserDepartment(String userDepartment) {
        this.userDepartment = userDepartment;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
