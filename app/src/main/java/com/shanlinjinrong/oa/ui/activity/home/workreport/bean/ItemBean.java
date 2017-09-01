package com.shanlinjinrong.oa.ui.activity.home.workreport.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 丁 on 2017/8/21.
 */

public class ItemBean implements Parcelable {
    private int mType;
    private String mTitle;
    private String mContent;
    private boolean mGroup;
    private String mGroupTitle;



    public ItemBean(String mTitle, String mContent, int mType) {
        this.mContent = mContent;
        this.mType = mType;
        this.mTitle = mTitle;
    }

    public ItemBean(String mTitle, String mContent, int mType, boolean mGroup, String mGroupTitle) {
        this.mContent = mContent;
        this.mType = mType;
        this.mTitle = mTitle;
        this.mGroup = mGroup;
        this.mGroupTitle = mGroupTitle;
    }

    public String getGroupTitle() {
        return mGroupTitle;
    }

    public ItemBean setGroupTitle(String mGroupTitle) {
        this.mGroupTitle = mGroupTitle;
        return this;
    }

    public boolean getGroup() {
        return mGroup;
    }

    public ItemBean setGroup(boolean mGroup) {
        this.mGroup = mGroup;
        return this;
    }

    public int getType() {
        return mType;
    }

    public ItemBean setType(int mType) {
        this.mType = mType;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public ItemBean setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public String getContent() {
        return mContent;
    }

    public ItemBean setContent(String mContent) {
        this.mContent = mContent;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeString(this.mTitle);
        dest.writeString(this.mContent);
        dest.writeByte(this.mGroup ? (byte) 1 : (byte) 0);
        dest.writeString(this.mGroupTitle);
    }

    protected ItemBean(Parcel in) {
        this.mType = in.readInt();
        this.mTitle = in.readString();
        this.mContent = in.readString();
        this.mGroup = in.readByte() != 0;
        this.mGroupTitle = in.readString();
    }

    public static final Parcelable.Creator<ItemBean> CREATOR = new Parcelable.Creator<ItemBean>() {
        @Override
        public ItemBean createFromParcel(Parcel source) {
            return new ItemBean(source);
        }

        @Override
        public ItemBean[] newArray(int size) {
            return new ItemBean[size];
        }
    };
}
