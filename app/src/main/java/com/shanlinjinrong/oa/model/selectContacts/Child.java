package com.shanlinjinrong.oa.model.selectContacts;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model.selectContacts
 * Author:Created by Tsui on Date:2017/3/1 18:41
 * Description:选择联系人二级实体类
 */
public class Child implements Parcelable, MultiItemEntity {
    private String oname;
    private String portrait;
    private String post;
    private String uid;
    private String username;
    private boolean isChecked;

    //------------
    private String sex;
    private String department_id;
    private String CODE;


    public Child(String oname, String portrait, String post, String uid, String username,
                 String sex, String departmentId,String code,
                 boolean isChecked) {
        this.oname = oname;
        this.portrait = portrait;
        this.post = post;
        this.uid = uid;
        this.username = username;
        this.isChecked = isChecked;
        this.sex = sex;
        this.department_id = departmentId;
        this.CODE = code;
    }

    @Override
    public String toString() {
        return "Child{" +
                "oname='" + oname + '\'' +
                ", portrait='" + portrait + '\'' +
                ", post='" + post + '\'' +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", isChecked=" + isChecked +
                ", sex='" + sex + '\'' +
                ", department_id='" + department_id + '\'' +
                ", CODE='" + CODE + '\'' +
                '}';
    }

    public Child(Parcel in) {
        oname = in.readString();
        portrait = in.readString();
        post = in.readString();
        uid = in.readString();
        username = in.readString();
        isChecked = in.readByte() != 0;
        sex = in.readString();
        department_id = in.readString();
        CODE = in.readString();
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
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


    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void toggle() {
        this.isChecked = !this.isChecked;
    }

    public boolean getChecked() {
        return this.isChecked;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oname);
        dest.writeString(portrait);
        dest.writeString(post);
        dest.writeString(uid);
        dest.writeString(username);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(sex);
        dest.writeString(department_id);
        dest.writeString(CODE);

    }

    @Override
    public int getItemType() {
        return 100;
    }
}
