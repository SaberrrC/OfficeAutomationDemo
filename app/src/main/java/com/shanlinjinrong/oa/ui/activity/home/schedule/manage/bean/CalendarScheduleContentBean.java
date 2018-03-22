package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean;

import java.util.List;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/24
 * 功能描述：
 */

public class CalendarScheduleContentBean {


    /**
     * code : 000000
     * message : success
     * data : [{"id":129,"userId":52837,"taskTheme":"吴","taskDate":"2018-01-29","startTime":"2018-01-29 10:00:00","endTime":"2018-01-29 11:00:00","taskDetail":"没有","taskType":2,"taskId":0,"status":0,"remark":null,"address":null}]
     */

    private String code;
    private String         message;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 129
         * userId : 52837
         * taskTheme : 吴
         * taskDate : 2018-01-29
         * startTime : 2018-01-29 10:00:00
         * endTime : 2018-01-29 11:00:00
         * taskDetail : 没有
         * taskType : 2
         * taskId : 0
         * status : 0
         * remark : null
         * address : null
         */

        private int id;
        private int    userId;
        private String taskTheme;
        private String taskDate;
        private String startTime;
        private String endTime;
        private String taskDetail;
        private int    taskType;
        private int    taskId;
        private int    status;
        private String remark;
        private String address;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getTaskTheme() {
            return taskTheme;
        }

        public void setTaskTheme(String taskTheme) {
            this.taskTheme = taskTheme;
        }

        public String getTaskDate() {
            return taskDate;
        }

        public void setTaskDate(String taskDate) {
            this.taskDate = taskDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getTaskDetail() {
            return taskDetail;
        }

        public void setTaskDetail(String taskDetail) {
            this.taskDetail = taskDetail;
        }

        public int getTaskType() {
            return taskType;
        }

        public void setTaskType(int taskType) {
            this.taskType = taskType;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
