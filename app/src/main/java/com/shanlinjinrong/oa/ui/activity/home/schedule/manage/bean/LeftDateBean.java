package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean;

import android.util.SparseArray;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/16
 * 功能描述：
 */

public class LeftDateBean implements MultiItemEntity {
    private String date;

    private int itemType;

    private int position;

    private boolean isSelected;

    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public static class DataBean {
        /**
         * id : 35
         * userId : 40030
         * taskTheme : 测试
         * taskDate : 2018-01-24
         * startTime : 2018-01-24 11:00:00
         * endTime : 2018-01-24 09:00:00
         * taskDetail : xiangqing
         * taskType : 2
         * taskId : 0
         * status : 0
         * remark : null
         */

        private int    id;
        private int    userId;
        private String taskTheme;
        private String taskDate;
        private String startTime;
        private String endTime;
        private String taskDetail;
        private int    taskType;
        private int    taskId;
        private int    status;
        private Object remark;

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

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }
    }
}
