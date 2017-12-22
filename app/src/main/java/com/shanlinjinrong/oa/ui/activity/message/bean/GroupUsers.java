package com.shanlinjinrong.oa.ui.activity.message.bean;

import android.text.TextUtils;

import com.shanlinjinrong.oa.model.Contacts;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class GroupUsers {

    private String  uid;
    private String  sex;
    public  String  code;
    private String  orgId;
    private String  phone;
    private String  email;
    private String  postId;
    private String  isShow;
    private String  username;
    private String  postTitle;
    private String  portraits;
    private boolean isChecked;
    private String  departmentName;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortraits() {
        return portraits;
    }

    public void setPortraits(String portraits) {
        this.portraits = portraits;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void save(Contacts contacts) {
        try {
            if (!TextUtils.isEmpty(contacts.getUid()))
                setUid(contacts.getUid());
            setSex(contacts.getSex());
            setCode(contacts.getCode());
            setPhone(contacts.getPhone());
            //            setOrgId(contacts.getOrgId());
            setEmail(contacts.getEmail());
            setPostId(contacts.getPostId());
            setIsShow(contacts.getIsshow());
            setChecked(contacts.isChecked());
            setUsername(contacts.getUsername());
            setPostTitle(contacts.getPostTitle());
            setPortraits(contacts.getPortraits());
            setDepartmentName(contacts.getDepartmentName());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
