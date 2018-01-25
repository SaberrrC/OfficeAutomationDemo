package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.CalendarScheduleContentBean;

import java.util.List;

/**
 * Created by 丁 on 2017/10/12.
 */

public class MonthlyCalenderPopItem {
    private String content;
    private boolean enable;
    private boolean isSelect;
    private int dateType;
    private List<CalendarScheduleContentBean.DataBean> data;

    public List<CalendarScheduleContentBean.DataBean> getData() {
        return data;
    }

    public void setData(List<CalendarScheduleContentBean.DataBean> data) {
        this.data = data;
    }

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    public MonthlyCalenderPopItem(String content, boolean enable, boolean isSelect) {
        this.enable = enable;
        this.content = content;
        this.isSelect = isSelect;
    }


    public String getContent() {
        return content;
    }

    public MonthlyCalenderPopItem setContent(String content) {
        this.content = content;
        return this;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public MonthlyCalenderPopItem setSelect(boolean select) {
        isSelect = select;
        return this;
    }

    public boolean isEnable() {
        return enable;
    }

    public MonthlyCalenderPopItem setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }
}
