package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by 丁 on 2017/9/27.
 */

public class WeekReportItemBean {
    private int id;
    private int checkmanId;
    private String checkman;
    private long startTime;
    private long endTime;
    private int userId;
    private int ratingStatus;
    private int reportType;
    private String checkmainRating;
    private String remark;
    private String postName;
    private List<WeeklySummaryBean> weeklySummary;
    private List<WeekPlaneBean> weekPlane;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCheckmanId() {
        return checkmanId;
    }

    public void setCheckmanId(int checkmanId) {
        this.checkmanId = checkmanId;
    }

    public String getCheckman() {
        return checkman;
    }

    public void setCheckman(String checkman) {
        this.checkman = checkman;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(int ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public String getCheckmainRating() {
        return checkmainRating;
    }

    public void setCheckmainRating(String checkmainRating) {
        this.checkmainRating = checkmainRating;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public List<WeeklySummaryBean> getWeeklySummary() {
        return weeklySummary;
    }

    public void setWeeklySummary(List<WeeklySummaryBean> weeklySummary) {
        this.weeklySummary = weeklySummary;
    }

    public List<WeekPlaneBean> getWeekPlane() {
        return weekPlane;
    }

    public void setWeekPlane(List<WeekPlaneBean> weekPlane) {
        this.weekPlane = weekPlane;
    }

    public static class WeeklySummaryBean {
        /**
         * difference : vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
         * remark : 111
         * work : 1111
         * workPlan : gnfvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
         */

        private String difference;
        private String remark;
        private String work;
        private String workPlan;

        public String getDifference() {
            return difference;
        }

        public void setDifference(String difference) {
            this.difference = difference;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        public String getWorkPlan() {
            return workPlan;
        }

        public void setWorkPlan(String workPlan) {
            this.workPlan = workPlan;
        }

        public int getWriteState() {
            if (!TextUtils.isEmpty(difference) && !TextUtils.isEmpty(remark) && !TextUtils.isEmpty(work) && !TextUtils.isEmpty(workPlan)) {
                return 0;
            } else if (!TextUtils.isEmpty(difference) || !TextUtils.isEmpty(remark) || !TextUtils.isEmpty(work) || !TextUtils.isEmpty(workPlan)) {
                return 1;
            }
            return 2;

        }
    }

    public static class WeekPlaneBean {
        /**
         * nextWorkPlan : 1
         * personLiable : 1
         * remark : 1
         */

        private String nextWorkPlan;
        private String personLiable;
        private String remark;

        public String getNextWorkPlan() {
            return nextWorkPlan;
        }

        public void setNextWorkPlan(String nextWorkPlan) {
            this.nextWorkPlan = nextWorkPlan;
        }

        public String getPersonLiable() {
            return personLiable;
        }

        public void setPersonLiable(String personLiable) {
            this.personLiable = personLiable;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getWriteState() {
            if (!TextUtils.isEmpty(nextWorkPlan) && !TextUtils.isEmpty(personLiable) && !TextUtils.isEmpty(remark)) {
                return 0;
            } else if (!TextUtils.isEmpty(nextWorkPlan) || !TextUtils.isEmpty(personLiable) || !TextUtils.isEmpty(remark)) {
                return 1;
            }
            return 2;

        }
    }
}
