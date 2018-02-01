package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/15
 * 功能描述：
 */

public class WeekCalendarBean implements Serializable {

    private List<String>  week;
    private List<String>  Day;
    private List<String>  month;
    private List<String>  year;
    private List<Boolean> isSelected;

    private boolean mIsFirst;

    public WeekCalendarBean() {
    }

    public WeekCalendarBean(List<String> week, List<String> day, List<String> month, List<String> year, List<Boolean> isSelected, boolean isFirst) {
        this.week = week;
        Day = day;
        this.month = month;
        this.year = year;
        this.isSelected = isSelected;
        mIsFirst = isFirst;
    }

    public boolean isFirst() {
        return mIsFirst;
    }

    public void setFirst(boolean first) {
        mIsFirst = first;
    }

    public List<String> getWeek() {
        return week;
    }

    public void setWeek(List<String> week) {
        this.week = week;
    }

    public List<String> getDay() {
        return Day;
    }

    public void setDay(List<String> day) {
        Day = day;
    }

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public List<String> getYear() {
        return year;
    }

    public void setYear(List<String> year) {
        this.year = year;
    }

    public List<Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(List<Boolean> isSelected) {
        this.isSelected = isSelected;
    }
}
