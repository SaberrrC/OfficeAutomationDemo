package com.shanlinjinrong.oa.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * <h3>Description: 用户实体类 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/30.<br />
 */
public class User implements MultiItemEntity, Serializable {
    private String yx_token;//云信token
    private String is_initial_pwd;
    private String uid;
    private String token;
    private String email;
    private String portraits;//头像
    private String username;//用户名
    private String sex;//性别
    private String phone;
    private String isshow;
    private String oid;


    private String hiredate;//入职日期
    private String companyName;//公司名称
    private String postId;//岗位ID
    private String postName;//岗位名称
    private String departmentId;//部门ID
    private String departmentName;//部门名称
    private String isleader;//

    private String code;//员工工号

    public User() {
    }

    public User(String username, String phone, String portraits, String sex, String postId, String code
            , String departmentId, String postName, String departmentName, String email) {
        this.username = username;
        this.phone = phone;
        this.portraits = portraits;
        this.sex = sex;
        this.postId = postId;
        this.code = code;
        this.departmentId = departmentId;
        this.postName = postName;
        this.departmentName = departmentName;
        this.email = email;
        if (departmentId.equals(AppConfig.getAppConfig(AppManager.mContext).getDepartmentId())) {
            isshow = "1";
        } else {
            isshow = "0";
        }
    }

    public String getYx_token() {
        return yx_token;
    }

    public String getIs_initial_pwd() {
        return is_initial_pwd;
    }

    public String getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getPortraits() {
        return "http://" + portraits;
    }

    public String getUsername() {
        return username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIs_initial_pwd(String is_initial_pwd) {
        this.is_initial_pwd = is_initial_pwd;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setPortraits(String portraits) {
        this.portraits = portraits;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public String getHiredate() {
        return hiredate;
    }

    public void setHiredate(String hiredate) {
        this.hiredate = hiredate;
    }

    public void setYx_token(String yx_token) {
        this.yx_token = yx_token;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getIsleader() {
        return isleader;
    }

    public void setIsleader(String isleader) {
        this.isleader = isleader;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public User(JSONObject jsonObject) {
        try {
            code = jsonObject.getString("CODE");
            uid = jsonObject.getString("uid");
            token = jsonObject.getString("token");
            email = jsonObject.getString("email");
            is_initial_pwd = jsonObject.getString("is_initial_pwd");
            portraits = jsonObject.getString("portraits");
            username = jsonObject.getString("username");
            sex = jsonObject.getString("sex");
            phone = jsonObject.getString("phone");
            hiredate = jsonObject.getString("hiredate");
            companyName = jsonObject.getString("company_name");
            postId = jsonObject.getString("post_id");
            postName = jsonObject.getString("post_title");
            departmentId = jsonObject.getString("department_id");
            departmentName = jsonObject.getString("department_name");
            email = jsonObject.getString("email");
            isleader = jsonObject.getString("isleader");
            yx_token = jsonObject.getString("yx_token");
            oid = jsonObject.getString("oid");

            //登陆更新自己的信息
            FriendsInfoCacheSvc.getInstance(AppManager.mContext)
                    .addOrUpdateFriends(new Friends(uid, username, getPortraits(), sex, phone, postName, departmentName, email, departmentId));
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("user解析异常-》" + e.toString());
        }
    }


    @Override
    public String toString() {
        return "User{" +
                "yx_token='" + yx_token + '\'' +
                ", is_initial_pwd='" + is_initial_pwd + '\'' +
                ", uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", portraits='" + portraits + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", isshow='" + isshow + '\'' +
                ", oid='" + oid + '\'' +
                ", hiredate='" + hiredate + '\'' +
                ", companyName='" + companyName + '\'' +
                ", postId='" + postId + '\'' +
                ", postName='" + postName + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", isleader='" + isleader + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    @Override
    public int getItemType() {
        return 100;
    }
}