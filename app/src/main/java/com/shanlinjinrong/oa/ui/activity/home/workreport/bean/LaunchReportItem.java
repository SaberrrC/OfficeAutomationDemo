package com.shanlinjinrong.oa.ui.activity.home.workreport.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 丁 on 2017/8/21.
 * 发报页面的实体类
 */
public class LaunchReportItem implements Parcelable {
    private int mType; // cell 类型
    private String mTitle = ""; // 标题
    private String mContent = ""; //显示内容
    private boolean mGroup;
    private String mGroupTitle = ""; //组标题


    public LaunchReportItem(String mTitle, String mContent, int mType) {
        this.mContent = mContent;
        this.mType = mType;
        this.mTitle = mTitle;
    }

    public LaunchReportItem(String mTitle, String mContent, int mType, boolean mGroup, String mGroupTitle) {
        this.mContent = mContent;
        this.mType = mType;
        this.mTitle = mTitle;
        this.mGroup = mGroup;
        this.mGroupTitle = mGroupTitle;
    }

    public String getGroupTitle() {
        return mGroupTitle;
    }

    public LaunchReportItem setGroupTitle(String mGroupTitle) {
        this.mGroupTitle = mGroupTitle;
        return this;
    }

    public boolean getGroup() {
        return mGroup;
    }

    public LaunchReportItem setGroup(boolean mGroup) {
        this.mGroup = mGroup;
        return this;
    }

    public int getType() {
        return mType;
    }

    public LaunchReportItem setType(int mType) {
        this.mType = mType;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public LaunchReportItem setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public String getContent() {
        return mContent;
    }

    public LaunchReportItem setContent(String mContent) {
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

    protected LaunchReportItem(Parcel in) {
        this.mType = in.readInt();
        this.mTitle = in.readString();
        this.mContent = in.readString();
        this.mGroup = in.readByte() != 0;
        this.mGroupTitle = in.readString();
    }

    public static final Parcelable.Creator<LaunchReportItem> CREATOR = new Parcelable.Creator<LaunchReportItem>() {
        @Override
        public LaunchReportItem createFromParcel(Parcel source) {
            return new LaunchReportItem(source);
        }

        @Override
        public LaunchReportItem[] newArray(int size) {
            return new LaunchReportItem[size];
        }
    };
}
