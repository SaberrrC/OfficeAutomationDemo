package com.itcrm.GroupInformationPlatform.model;

import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by Tsui on Date:2016/12/7 10:56
 * Description:推送消息实体
 */
public class PushMsg {
    private  String    pid;
    private String    content;    //推送内容	string
    private String    created;//	推送时间	string	e.g. 11.17 17:56
    private String    status;//	状态 1未读，2已读	string
    private String    type;//类型 5工作汇报，6审批申请，7审批回复	string

    public PushMsg(JSONObject jsonObject) {
        try {
            content = jsonObject.getString("content");
            created = jsonObject.getString("created");
            status = jsonObject.getString("status");
            type = jsonObject.getString("type");
            pid = jsonObject.getString("pid");



        } catch (JSONException e) {
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
