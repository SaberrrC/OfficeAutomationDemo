package com.hyphenate.easeui.db;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/18.
 * 好友实体类
 */
public class Friends implements Serializable {


    public static final String COLUMNNAME_USERID       = "user_id";
    public static final String COLUMNNAME_UID          = "uid";
    public static final String COLUMNNAME_NICKNAME     = "nickname";
    public static final String COLUMNNAME_PORTRAIT     = "portrait";
    public static final String COLUMNNAME_SEX          = "userSex";
    public static final String COLUMNNAME_PHONE        = "userPhone";
    public static final String COLUMNNAME_POST         = "userPost";
    public static final String COLUMNNAME_DEOARTMENT   = "userDepartment";
    public static final String COLUMNNAME_EMAIL        = "userEmail";
    public static final String COLUMNNAME_DEOARTMENTId = "userDepartmentId";

    private String uid;
    private String user_id;
    private String nickname;
    private String portrait;
    private String userSex;
    private String userPhone;
    private String userPost;
    private String userDepartment;
    private String userDepartmentId;
    private String userEmail;


    public Friends() {

    }

    public Friends(String user_id, String nickname, String portrait, String userSex, String userPhone, String userPost, String userDepartment, String userEmail, String departmentId) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.portrait = portrait;
        this.userSex = userSex;
        this.userPhone = userPhone;
        this.userPost = userPost;
        this.userDepartment = userDepartment;
        this.userEmail = userEmail;
        this.userDepartmentId = departmentId;
    }

    public Friends(String uid ,String user_id, String nickname, String portrait, String userSex, String userPhone, String userPost, String userDepartment, String userEmail, String departmentId) {
        this.uid = uid;
        this.user_id = user_id;
        this.nickname = nickname;
        this.portrait = portrait;
        this.userSex = userSex;
        this.userPhone = userPhone;
        this.userPost = userPost;
        this.userDepartment = userDepartment;
        this.userEmail = userEmail;
        this.userDepartmentId = departmentId;
    }

    public Friends(String user_id, String nickname, String portrait) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.portrait = portrait;
    }

    public Friends(String uid, String code, String nickname, String portrait, String email, String sex) {
        this.uid = uid;
        this.user_id = code;
        this.nickname = nickname;
        this.portrait = portrait;
        this.userEmail = email;
        this.userSex = sex;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getUserDepartmentId() {
        return userDepartmentId;
    }

    public void setUserDepartmentId(String userDepartmentId) {
        this.userDepartmentId = userDepartmentId;
    }
}
