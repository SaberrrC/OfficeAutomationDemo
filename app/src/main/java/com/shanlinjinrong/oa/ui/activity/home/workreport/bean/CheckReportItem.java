package com.shanlinjinrong.oa.ui.activity.home.workreport.bean;

/**
 * Created by 丁 on 2017/9/21.
 */

public class CheckReportItem {
    private int type = 1;//发报类型
    private String reportAccount = "";//发报人
    private String reportTime;//发报日期
    private String reportCommitTime;//提交日报或者周报时间

    private String[] reportType = {"日报", "周报"};//1-日报，2-周报


    public CheckReportItem(int type, String reportAccount, String reportTime, String reportCommitTime) {
        this.reportAccount = reportAccount;
        this.type = type;
        this.reportTime = reportTime;
        this.reportCommitTime = reportCommitTime;
    }

    public String getType() {
        return type == 1 ? reportType[0] : reportType[1];
    }

    public CheckReportItem setType(int type) {
        this.type = type;
        return this;
    }

    public String getReportAccount() {
        return reportAccount;
    }

    public CheckReportItem setReportAccount(String reportAccount) {
        this.reportAccount = reportAccount;
        return this;
    }

    public String getReportTime() {
        return reportTime;
    }

    public CheckReportItem setReportTime(String reportTime) {
        this.reportTime = reportTime;
        return this;
    }

    public String getReportCommitTime() {
        return reportCommitTime;
    }

    public CheckReportItem setReportCommitTime(String reportCommitTime) {
        this.reportCommitTime = reportCommitTime;
        return this;
    }
}
