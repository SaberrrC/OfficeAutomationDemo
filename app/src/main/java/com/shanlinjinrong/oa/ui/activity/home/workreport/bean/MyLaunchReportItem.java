package com.shanlinjinrong.oa.ui.activity.home.workreport.bean;

/**
 * Created by 丁 on 2017/9/21.
 * 发我起的列表item
 */
public class MyLaunchReportItem {
    private String reportAccount = "";//发报人
    private int dailyId; // 日报ID
    private int type = 1;//发报类型
    private String reportTime;//发报日期
    private String reportCommitTime;//提交日报或者周报时间
    private int status = 1;//审批状态


    private String[] reportType = {"日报", "周报"};//1-日报，2-周报
    private String[] reportStatus = {"待评分", "已退回", "已评分"};//平分状态（1-已提交(待评分)，2-已退回，3-已评分）

    public MyLaunchReportItem(int type, String reportTime, String reportCommitTime, int status) {
        this.type = type;
        this.reportTime = reportTime;
        this.reportCommitTime = reportCommitTime;
        this.status = status;
    }

    public MyLaunchReportItem(int dailyId, String reportAccount, int type, String reportTime, String reportCommitTime, int status) {
        this.type = type;
        this.reportAccount = reportAccount;
        this.reportTime = reportTime;
        this.reportCommitTime = reportCommitTime;
        this.dailyId = dailyId;
        this.status = status;
    }

    public String getReportAccount() {
        return reportAccount;
    }

    public MyLaunchReportItem setReportAccount(String reportAccount) {
        this.reportAccount = reportAccount;
        return this;
    }

    public int getDailyId() {
        return dailyId;
    }

    public MyLaunchReportItem setDailyId(int dailyId) {
        this.dailyId = dailyId;
        return this;
    }

    public String getStatusText() {
        if (status > 3 || status < 1)
            return reportStatus[0];
        return reportStatus[status - 1];
    }

    public int getStatus() {
        return status;
    }

    public MyLaunchReportItem setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getReportTime() {
        return reportTime;
    }

    public MyLaunchReportItem setReportTime(String reportTime) {
        this.reportTime = reportTime;
        return this;
    }

    public String getType() {
        return type == 1 ? reportType[0] : reportType[1];
    }

    public MyLaunchReportItem setType(int type) {
        this.type = type;
        return this;
    }

    public String getReportCommitTime() {
        return reportCommitTime;
    }

    public MyLaunchReportItem setReportCommitTime(String reportCommitTime) {
        this.reportCommitTime = reportCommitTime;
        return this;
    }


}
