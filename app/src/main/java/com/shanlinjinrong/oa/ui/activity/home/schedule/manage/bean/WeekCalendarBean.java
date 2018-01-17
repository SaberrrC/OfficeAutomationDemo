package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean;

import java.io.Serializable;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/15
 * 功能描述：
 */

public class WeekCalendarBean implements Serializable {

    private String Week;

    private String Day;

    public String getWeek() {
        return Week;
    }

    public void setWeek(String week) {
        Week = week;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }
}
