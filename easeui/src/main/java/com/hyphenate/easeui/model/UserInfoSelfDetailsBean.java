package com.hyphenate.easeui.model;

import java.io.Serializable;

/**
 * 作者：王凤旭
 * 时间：2017/8/22
 * 描述：
 */

public class UserInfoSelfDetailsBean implements Serializable {


    public String CODE_self;
    public String department_name_self;
    public String email_self;
    public String phone_self;
    public String portrait_self;
    public String post_title_self;
    public String sex_self;
    public String username_self;

    public String getCODE_self() {
        return CODE_self;
    }

    public UserInfoSelfDetailsBean setCODE_self(String CODE_self) {
        this.CODE_self = CODE_self;
        return this;
    }

    public String getDepartment_name_self() {
        return department_name_self;
    }

    public UserInfoSelfDetailsBean setDepartment_name_self(String department_name_self) {
        this.department_name_self = department_name_self;
        return this;
    }

    public String getEmail_self() {
        return email_self;
    }

    public UserInfoSelfDetailsBean setEmail_self(String email_self) {
        this.email_self = email_self;
        return this;
    }

    public String getPhone_self() {
        return phone_self;
    }

    public UserInfoSelfDetailsBean setPhone_self(String phone_self) {
        this.phone_self = phone_self;
        return this;
    }

    public String getPortrait_self() {
        return portrait_self;
    }

    public UserInfoSelfDetailsBean setPortrait_self(String portrait_self) {
        this.portrait_self = portrait_self;
        return this;
    }

    public String getPost_title_self() {
        return post_title_self;
    }

    public UserInfoSelfDetailsBean setPost_title_self(String post_title_self) {
        this.post_title_self = post_title_self;
        return this;
    }

    public String getSex_self() {
        return sex_self;
    }

    public UserInfoSelfDetailsBean setSex_self(String sex_self) {
        this.sex_self = sex_self;
        return this;
    }

    public String getUsername_self() {
        return username_self;
    }

    public UserInfoSelfDetailsBean setUsername_self(String username_self) {
        this.username_self = username_self;
        return this;
    }
}
