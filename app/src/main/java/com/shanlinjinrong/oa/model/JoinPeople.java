package com.shanlinjinrong.oa.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by CXP on Date: 2016/9/6 10:51.
 * Description:轻量级联系人，用于各个界面之间的传输
 */
public class JoinPeople implements Parcelable, MultiItemEntity {
    public String department_id;
    public String department_name;
    public String portraits;
    public String post;
    public String sex;
    public String uid;
    public String username;

    public boolean isChecked = false;


    public JoinPeople(String portraits, String username, String uid,
                       String department_id, String department_name
            ,  String sex,String post) {
        this.portraits = portraits;
        this.username = username;
        this.department_id=department_id;
        this.department_name=department_name;
        this.sex=sex;
        this.uid=uid;
        this.post=post;
    }

    public JoinPeople(String username, String uid,boolean isChecked) {
        this.username = username;
        this.uid=uid;
        this.isChecked=isChecked;
    }

    public JoinPeople() {
    }

    public JoinPeople(Parcel in) {
        portraits = in.readString();
        uid = in.readString();
        username = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<JoinPeople> CREATOR = new Creator<JoinPeople>() {
        @Override
        public JoinPeople createFromParcel(Parcel in) {
            return new JoinPeople(in);
        }

        @Override
        public JoinPeople[] newArray(int size) {
            return new JoinPeople[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(portraits);
        dest.writeString(uid);
        dest.writeString(username);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int getItemType() {
        return 1;
    }

    @Override
    public String toString() {
        return "JoinPeople{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
