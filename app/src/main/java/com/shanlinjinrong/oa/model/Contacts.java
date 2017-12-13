package com.shanlinjinrong.oa.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * <h3>Description: 通讯录实体类 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/1.<br />
 */
public class Contacts implements MultiItemEntity, Serializable {

    /**
     * 0-部门
     */
    public static final int DEPARTMENT = 0;
    /**
     * 1-员工
     */
    public static final int EMPLOYEE = 1;
    public String code;//工号

    public int itemType;

    /**
     * 0-部门 1-员工
     */
    public String type;
    /**
     * 部门ID
     */
    public String departmentId;
    /**
     * 部门名称
     */
    public String departmentName;
    /**
     * 部门人数
     */
    public String departmentPersons;


    /**
     * UID
     */
    public String uid;
    /**
     * 用户名称
     */
    public String username;
    /**
     * 性别
     */
    public String sex;
    /**
     * 岗位ID
     */
    public String postId;
    /**
     * 岗位名称
     */
    public String postTitle;
    /**
     * 电话号码
     */
    public String phone;
    /**
     * 是否显示
     */
    public String isshow;
    public String portraits;
    public String email;
    private boolean isChecked;
    private String orgId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Contacts() {
    }

    public Contacts(JSONObject jsonObject) {
        try {
            departmentId = jsonObject.getString("id");
            departmentName = jsonObject.getString("name");
            departmentPersons = jsonObject.getString("memberCount");
            itemType = 0;
        } catch (JSONException e) {
            try {
                uid = jsonObject.getString("uid");
                username = jsonObject.getString("username");
                phone = jsonObject.getString("phone");
                email = jsonObject.getString("email");
                portraits =jsonObject.getString("portraits");
                sex = jsonObject.getString("sex");
                postTitle = jsonObject.getString("post_title");
                postId = jsonObject.getString("department_id");
                departmentName = jsonObject.getString("department_name");
                code = jsonObject.getString("code");
                itemType = 1;
                setIsshow(AppConfig.getAppConfig(AppManager.mContext).getDepartmentId().equals(postId) ? "1" : "0");
            } catch (Throwable e1) {
                e1.printStackTrace();
                LogUtils.e("获取部门和员工异常：" + e1.toString());
            }
        }
    }


    public String getCode() {
        if (code == null) {
            return "";
        }
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public String getType() {
        return type;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getDepartmentPersons() {
        return departmentPersons;
    }

    public String getPortraits() {
        return portraits;
    }


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setDepartmentPersons(String departmentPersons) {
        this.departmentPersons = departmentPersons;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPortraits(String portraits) {
        this.portraits = portraits;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getSex() {
        return sex;
    }

    public String getPostId() {
        return postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "code='" + code + '\'' +
                ", itemType=" + itemType +
                ", type='" + type + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", departmentPersons='" + departmentPersons + '\'' +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", postId='" + postId + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", phone='" + phone + '\'' +
                ", isshow='" + isshow + '\'' +
                ", portraits='" + portraits + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}