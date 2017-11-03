package com.hyphenate.easeui.model;

import java.io.Serializable;

/**
 * 作者：王凤旭
 * 时间：2017/8/22
 * 描述：
 */

public class UserInfoDetailsBean implements Serializable {
    public String CODE;
    public String department_name;
    public String email;
    public String phone;
    public String portrait;
    public String post_title;
    public String sex;
    public String username;
    public String getCODE() {
        return CODE;
    }

    public UserInfoDetailsBean setCODE(String CODE) {
        this.CODE = CODE;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserInfoDetailsBean setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public UserInfoDetailsBean setDepartment_name(String department_name) {
        this.department_name = department_name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfoDetailsBean setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserInfoDetailsBean setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getPortrait() {
        return portrait;
    }

    public UserInfoDetailsBean setPortrait(String portrait) {
        this.portrait = portrait;
        return this;
    }

    public String getPost_title() {
        return post_title;
    }

    public UserInfoDetailsBean setPost_title(String post_title) {
        this.post_title = post_title;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public UserInfoDetailsBean setSex(String sex) {
        this.sex = sex;
        return this;
    }
}
