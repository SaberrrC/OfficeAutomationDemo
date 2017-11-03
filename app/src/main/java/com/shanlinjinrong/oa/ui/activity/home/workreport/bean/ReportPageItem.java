package com.shanlinjinrong.oa.ui.activity.home.workreport.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 丁 on 2017/8/21.
 * 发报页面的实体类
 */
public class ReportPageItem implements Parcelable {
    private int mType; // cell 类型
    private String mTitle = ""; // 标题
    private String mContent = ""; //显示内容
    private boolean mGroup;
    private String mGroupTitle = ""; //组标题
    private String mEvaluationSupervisor = ""; //监督人评价
    private String mEvaluationCheckMan = ""; //检查人评价


    public ReportPageItem(String mTitle, String mContent, int mType) {
        this.mContent = mContent;
        this.mType = mType;
        this.mTitle = mTitle;
    }

    public ReportPageItem(String mTitle, String mContent, String mEvaluationSupervisor, String mEvaluationCheckMan, int mType) {
        this.mContent = mContent;
        this.mType = mType;
        this.mTitle = mTitle;
        this.mEvaluationSupervisor = mEvaluationSupervisor;
        this.mEvaluationCheckMan = mEvaluationCheckMan;
    }

    public ReportPageItem(String mTitle, String mContent, int mType, boolean mGroup, String mGroupTitle) {
        this.mContent = mContent;
        this.mType = mType;
        this.mTitle = mTitle;
        this.mGroup = mGroup;
        this.mGroupTitle = mGroupTitle;
    }

    public ReportPageItem(String mTitle, String mContent, String mEvaluationSupervisor, String mEvaluationCheckMan, int mType, boolean mGroup, String mGroupTitle) {
        this.mContent = mContent;
        this.mType = mType;
        this.mTitle = mTitle;
        this.mGroup = mGroup;
        this.mGroupTitle = mGroupTitle;
        this.mEvaluationSupervisor = mEvaluationSupervisor;
        this.mEvaluationCheckMan = mEvaluationCheckMan;
    }

    public String getGroupTitle() {
        return mGroupTitle;
    }

    public ReportPageItem setGroupTitle(String mGroupTitle) {
        this.mGroupTitle = mGroupTitle;
        return this;
    }

    public boolean getGroup() {
        return mGroup;
    }

    public ReportPageItem setGroup(boolean mGroup) {
        this.mGroup = mGroup;
        return this;
    }

    public int getType() {
        return mType;
    }

    public ReportPageItem setType(int mType) {
        this.mType = mType;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public ReportPageItem setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public String getContent() {
        return mContent;
    }

    public ReportPageItem setContent(String mContent) {
        this.mContent = mContent;
        return this;
    }

    public String getEvaluationSupervisor() {
        return mEvaluationSupervisor;
    }

    public ReportPageItem setEvaluationSupervisor(String mEvaluationSupervisor) {
        this.mEvaluationSupervisor = mEvaluationSupervisor;
        return this;
    }

    public String getEvaluationCheckMan() {
        return mEvaluationCheckMan;
    }

    public ReportPageItem setEvaluationCheckMan(String mEvaluationCheckMan) {
        this.mEvaluationCheckMan = mEvaluationCheckMan;
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
        dest.writeString(this.mEvaluationSupervisor);
        dest.writeString(this.mEvaluationCheckMan);
    }

    protected ReportPageItem(Parcel in) {
        this.mType = in.readInt();
        this.mTitle = in.readString();
        this.mContent = in.readString();
        this.mGroup = in.readByte() != 0;
        this.mGroupTitle = in.readString();
        this.mEvaluationSupervisor = in.readString();
        this.mEvaluationCheckMan = in.readString();
    }

    public static final Creator<ReportPageItem> CREATOR = new Creator<ReportPageItem>() {
        @Override
        public ReportPageItem createFromParcel(Parcel source) {
            return new ReportPageItem(source);
        }

        @Override
        public ReportPageItem[] newArray(int size) {
            return new ReportPageItem[size];
        }
    };
}
