package com.shanlin.oa.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by CXP on Date: 2016/9/18 18:18.
 * Description:公告通知实体类
 */
public class Notice implements Serializable {

    String content; //通知公告内容
    String createtime;//通知公告发布
    String nid;//通知公告id
    String status;//已读未读状态
    String type;//通知公告类型

    String created;//通知公告详情发布时间
    String orgname;//发送的部门名称
    String state;//状态1：未处理2：通过，3：驳回
    String title;//通知公告标题
    List<String> imgsLists;

    public Notice() {
    }

    public Notice(JSONObject jsonObject) {
        try {

            content = jsonObject.getString("content");
            created = jsonObject.getString("created");
            createtime = jsonObject.getString("createtime");
            nid = jsonObject.getString("nid");
            orgname = jsonObject.getString("orgname");
            state = jsonObject.getString("state");
            status = jsonObject.getString("status");
            title = jsonObject.getString("title");
            type = jsonObject.getString("type");

            imgsLists=new ArrayList<>();
            JSONArray imgs = jsonObject.getJSONArray("imgs");
            for (int i = 0; i < imgs.length(); i++) {imgs.get(i);
                imgsLists.add("http://"+imgs.get(i).toString());
            }

        } catch (JSONException e) {
        }
    }

    public List<String> getImgsLists() {
        return imgsLists;
    }

    public void setImgsLists(List<String> imgsLists) {
        this.imgsLists = imgsLists;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getType() {
        return type;
    }


    public String getCreated() {
        return created;
    }

    public String getOrgname() {
        return orgname;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "content='" + content + '\'' +
                ", createtime='" + createtime + '\'' +
                ", nid='" + nid + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' + '\'' +
                ", created='" + created + '\'' +
                ", orgname='" + orgname + '\'' +
                ", state='" + state + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
