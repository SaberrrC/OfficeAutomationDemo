package com.itcrm.GroupInformationPlatform.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/19.
 */

public class MeetDatils implements MultiItemEntity,Serializable {
    private String username;
    private String theme;
    private String begintime;
    private String endtime;



    public MeetDatils(String username, String theme, String begintime, String endtime) {
        this.username = username;
        this.theme = theme;
        this.begintime = begintime;
        this.endtime = endtime;
    }
    public MeetDatils(JSONObject jsonObject) {
        try {
            username = jsonObject.getString("username");
            theme = jsonObject.getString("theme");
            begintime = jsonObject.getString("begintime");
            endtime = jsonObject.getString("endtime");


        }catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("user解析异常-》"+e.toString());
        }
    }
    @Override
    public String toString() {
        return "MeetDatils{" +
                "username='" + username + '\'' +
                ", theme='" + theme + '\'' +
                ", begintime='" + begintime + '\'' +
                ", endtime='" + endtime + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    @Override
    public int getItemType() {
        return 20;
    }

}
