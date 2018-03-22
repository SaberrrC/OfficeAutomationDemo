package com.example.retrofit.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/24
 * 功能描述：
 */

public class ScheduleBean {

    private Date      task_date;
    private String    taskTheme;
    private Timestamp start_time;
    private Timestamp end_time;
    private String    task_detail;
    private int       task_type;

    public Date getTask_date() {
        return task_date;
    }

    public void setTask_date(Date task_date) {
        this.task_date = task_date;
    }

    public String getTaskTheme() {
        return taskTheme;
    }

    public void setTaskTheme(String taskTheme) {
        this.taskTheme = taskTheme;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    public String getTask_detail() {
        return task_detail;
    }

    public void setTask_detail(String task_detail) {
        this.task_detail = task_detail;
    }

    public int getTask_type() {
        return task_type;
    }

    public void setTask_type(int task_type) {
        this.task_type = task_type;
    }
}
