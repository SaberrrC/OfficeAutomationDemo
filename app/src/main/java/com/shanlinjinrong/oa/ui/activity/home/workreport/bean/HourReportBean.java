package com.shanlinjinrong.oa.ui.activity.home.workreport.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by 丁 on 2017/8/22.
 * 时报填写内容实体类
 */
public class HourReportBean implements Parcelable {
    private String mWorkPlan = "";//工作计划
    private String mRealWork = "";//实际工作
    private String mSelfEvaluate = "";//自评
    private String mQuantitative = "";//数据量化

    private String mCheckManEvaluate = "";//检查人评价
    private String mSupervisorEvaluate = "";//监督人评价


    public HourReportBean(String mWorkPlan, String mRealWork, String mSelfEvaluate, String mQuantitative) {
        this.mWorkPlan = mWorkPlan;
        this.mRealWork = mRealWork;
        this.mSelfEvaluate = mSelfEvaluate;
        this.mQuantitative = mQuantitative;
    }

    public HourReportBean(String mWorkPlan, String mRealWork, String mSelfEvaluate, String mQuantitative, String mCheckManEvaluate, String mSupervisorEvaluate) {
        this.mWorkPlan = mWorkPlan;
        this.mRealWork = mRealWork;
        this.mSelfEvaluate = mSelfEvaluate;
        this.mQuantitative = mQuantitative;
        this.mCheckManEvaluate = mCheckManEvaluate;
        this.mSupervisorEvaluate = mSupervisorEvaluate;
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

    public String getQuantitative() {
        return mQuantitative;
    }

    public HourReportBean setQuantitative(String mQuantitative) {
        this.mQuantitative = mQuantitative;
        return this;
    }

    public String getCheckManEvaluate() {
        return mCheckManEvaluate;
    }

    public HourReportBean setCheckManEvaluate(String mCheckManEvaluate) {
        this.mCheckManEvaluate = mCheckManEvaluate;
        return this;
    }

    public String getSupervisorEvaluate() {
        return mSupervisorEvaluate;
    }

    public HourReportBean setSupervisorEvaluate(String mSupervisorEvaluate) {
        this.mSupervisorEvaluate = mSupervisorEvaluate;
        return this;
    }

    /**
     * 判断时候有空的内容
     *
     * @return
     */
    public boolean checkHasEmpty() {
        return TextUtils.isEmpty(mWorkPlan) || TextUtils.isEmpty(mRealWork) || TextUtils.isEmpty(mSelfEvaluate) || TextUtils.isEmpty(mQuantitative);
    }

    /**
     * 判断是否填写完善
     *
     * @return
     */
    public boolean checkIsFull() {
        return !TextUtils.isEmpty(mWorkPlan) && !TextUtils.isEmpty(mRealWork) && !TextUtils.isEmpty(mSelfEvaluate) && !TextUtils.isEmpty(mQuantitative);
    }

    /**
     * 判断是否全部为空
     *
     * @return
     */
    public boolean checkAllEmpty() {
        return TextUtils.isEmpty(mWorkPlan) && TextUtils.isEmpty(mRealWork) && TextUtils.isEmpty(mSelfEvaluate) && TextUtils.isEmpty(mQuantitative);
    }

    /**
     * 判断是否已经评价
     */
    public boolean hasEvaluation() {
        return TextUtils.isEmpty(mWorkPlan) || TextUtils.isEmpty(mRealWork) || TextUtils.isEmpty(mSelfEvaluate) || TextUtils.isEmpty(mQuantitative) || TextUtils.isEmpty(mCheckManEvaluate) || TextUtils.isEmpty(mSelfEvaluate);
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
        dest.writeString(this.mQuantitative);
        dest.writeString(this.mCheckManEvaluate);
        dest.writeString(this.mSupervisorEvaluate);
    }

    protected HourReportBean(Parcel in) {
        this.mWorkPlan = in.readString();
        this.mRealWork = in.readString();
        this.mSelfEvaluate = in.readString();
        this.mQuantitative = in.readString();
        this.mCheckManEvaluate = in.readString();
        this.mSupervisorEvaluate = in.readString();
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
