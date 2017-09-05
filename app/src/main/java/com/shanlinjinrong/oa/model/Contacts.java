package com.shanlinjinrong.oa.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * <h3>Description: 通讯录实体类 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/1.<br />
 */
public class Contacts implements MultiItemEntity,Serializable {

    /**
     * 0-部门
     */
    public static final int DEPARTMENT = 0;
    /**
     * 1-员工
     */
    public static final int EMPLOYEE = 1;
    private String code;//工号

    private int itemType;

    /**
     * 0-部门 1-员工
     */
    private String type;
    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 部门人数
     */
    private String departmentPersons;


    /**
     * UID
     */
    private String uid;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 性别
     */
    private String sex;
    /**
     * 岗位ID
     */
    private String postId;
    /**
     * 岗位名称
     */
    private String postTitle;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 是否显示
     */
    private String isshow;
    private String portraits;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Contacts(JSONObject jsonObject) {
        try {
            type = jsonObject.getString("type");
            itemType = Integer.parseInt(type);
            departmentId = jsonObject.getString("department_id");
            departmentName = jsonObject.getString("department_name");

            if (itemType == EMPLOYEE) {
                code = jsonObject.getString("CODE");
                portraits = "http://" + jsonObject.getString("portraits");
                uid = jsonObject.getString("uid");
                username = jsonObject.getString("username");
                sex = jsonObject.getString("sex");
                postId = jsonObject.getString("post_id");
                postTitle = jsonObject.getString("post_title");
                phone = jsonObject.getString("phone");
                email=jsonObject.getString("email");
                isshow = jsonObject.getString("isshow");
            } else {
                departmentPersons = jsonObject.getString("department_persons");
            }
        } catch (JSONException e) {
            LogUtils.e("获取部门和员工异常：" + e.toString());
        }
    }

    public String getCode() {
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
}