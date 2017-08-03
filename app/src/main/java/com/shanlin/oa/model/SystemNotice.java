package com.shanlin.oa.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by CXP on Date: 2016/9/18 18:18.
 * Description:系统消息实体类
 */
public class SystemNotice {

    String content; //通知公告内容
    String createtime;//通知公告发布书剑
    String nid;//通知公告id
    String status;//已读未读状态

    public SystemNotice(JSONObject jsonObject) {
        try {
            nid = jsonObject.getString("nid");
            content = jsonObject.getString("content");
            createtime = jsonObject.getString("createtime");

            status = jsonObject.getString("status");
        } catch (JSONException e) {
        }
    }

    public String getContent() {
        return content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getNid() {
        return nid;
    }

    public String getStatus() {
        return status;
    }


}
