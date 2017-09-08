package com.shanlinjinrong.oa.ui.activity.home.workreport.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by 丁 on 2017/8/22.
 * 时报填写内容实体类
 */
public class HourReportBean implements Parcelable {
    private String mWorkPlan;//工作计划
    private String mRealWork;//实际工作
    private String mSelfEvaluate;//自评

    public HourReportBean(String mWorkPlan, String mRealWork, String mSelfEvaluate) {
        this.mWorkPlan = mWorkPlan;
        this.mRealWork = mRealWork;
        this.mSelfEvaluate = mSelfEvaluate;
    }

    public String getWorkPlan() {
        return mWorkPlan;
    }

    public HourReportBean setWorkPlan(String mWorkPlan) {
        this.mWorkPlan = mWorkPlan;
        return this;
    }

    public String getRealWork() {
        return mRealWork;
    }

    public HourReportBean setRealWork(String mRealWork) {
        this.mRealWork = mRealWork;
        return this;
    }

    public String getSelfEvaluate() {
        return mSelfEvaluate;
    }

    public HourReportBean setSelfEvaluate(String mSelfEvaluate) {
        this.mSelfEvaluate = mSelfEvaluate;
        return this;
    }

    /**
     * 判断时候有空的内容
     *
     * @return
     */
    public boolean checkHasEmpty() {
        return TextUtils.isEmpty(mWorkPlan) || TextUtils.isEmpty(mRealWork) || TextUtils.isEmpty(mSelfEvaluate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mWorkPlan);
        dest.writeString(this.mRealWork);
        dest.writeString(this.mSelfEvaluate);
    }

    protected HourReportBean(Parcel in) {
        this.mWorkPlan = in.readString();
        this.mRealWork = in.readString();
        this.mSelfEvaluate = in.readString();
    }

    public static final Parcelable.Creator<HourReportBean> CREATOR = new Parcelable.Creator<HourReportBean>() {
        @Override
        public HourReportBean createFromParcel(Parcel source) {
            return new HourReportBean(source);
        }

        @Override
        public HourReportBean[] newArray(int size) {
            return new HourReportBean[size];
        }
    };
}
