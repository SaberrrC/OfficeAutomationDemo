package com.shanlinjinrong.oa.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by Tsui on Date:2016/11/21 11:15
 * Description: 推送消息
 */
public class Push {
    private String content;
    private String created;
    private String id;
    private String status;
    private String type;

    public Push(JSONObject jsonObject) {
        try {
            content = jsonObject.getString("content");
            created = jsonObject.getString("created");


            id = jsonObject.getString("id");
            status = jsonObject.getString("status");

            type = jsonObject.getString("type");

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
