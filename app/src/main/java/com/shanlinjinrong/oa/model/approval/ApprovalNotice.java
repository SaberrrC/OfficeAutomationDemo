package com.shanlinjinrong.oa.model.approval;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by Tsui on Date:2016/11/22 11:47
 * Description:审批详情：公告实体类
 */
public class ApprovalNotice {


    String approver;
    String approve_time;
    String title;
    String orgnames;
    String oa_id;
    String create_date;
    String content;
    String create_time;//创建时间
    String status;    //	 	1未通过2通过3驳回
    String username;//


    public ApprovalNotice(JSONObject jo) {
        try {
            content = jo.getString("content");
            create_time = jo.getString("create_time");
            create_date = jo.getString("create_date");
            oa_id = jo.getString("oa_id");
            orgnames = jo.getString("orgnames");
            status = jo.getString("status");
            title = jo.getString("title");
            username = jo.getString("username");

            approve_time = jo.getString("approve_time");
            approver = jo.getString("approver");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApprove_time() {
        return approve_time;
    }

    public void setApprove_time(String approve_time) {
        this.approve_time = approve_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrgnames() {
        return orgnames;
    }

    public void setOrgnames(String orgnames) {
        this.orgnames = orgnames;
    }

    public String getOa_id() {
        return oa_id;
    }

    public void setOa_id(String oa_id) {
        this.oa_id = oa_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
