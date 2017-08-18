package com.shanlin.oa.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by CXP on Date: 2016/9/6 10:51.
 * Description:轻量级联系人，用于各个界面之间的传输
 */
public class GlightContact implements Parcelable, MultiItemEntity {

    public String portraits;
    public String uid;
    public String username;
    public boolean isChecked = false;
    public String get_title;

    public GlightContact(String portraits, String username,String uid,
                         boolean isChecked,String get_title) {
        this.portraits = portraits;
        this.username = username;
        this.isChecked = isChecked;
        this.get_title=get_title;
        this.uid=uid;
    }

    public GlightContact(String username,String uid) {
        this.username = username;
        this.uid=uid;
    }

    public GlightContact() {
    }

    public GlightContact(Parcel in) {
        portraits = in.readString();
        uid = in.readString();
        username = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<GlightContact> CREATOR = new Creator<GlightContact>() {
        @Override
        public GlightContact createFromParcel(Parcel in) {
            return new GlightContact(in);
        }

        @Override
        public GlightContact[] newArray(int size) {
            return new GlightContact[size];
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
}
