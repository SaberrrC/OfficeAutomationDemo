package com.shanlinjinrong.oa.model;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by Tsui on Date:2016/12/14 15:20
 * Description:日程recyclerView条目数据
 */
public class ScheduleRvItemData {
   public  String cf_id;
    public String   roomname;
    public String  theme;
    public String      time;
    public String      date;

    public ScheduleRvItemData() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCf_id() {
        return cf_id;
    }

    public void setCf_id(String cf_id) {
        this.cf_id = cf_id;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
