package com.itcrm.GroupInformationPlatform.model.approval;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by Tsui on Date:2016/11/22 11:47
 * Description:审批：办公用品
 */
public class ApprovalOfficeSupplies {

    List<ApproversList> approversLists=new ArrayList<>();
    private ApproversList al;
    private Info info;

    public List<ApproversList> getApproversLists() {
        return approversLists;
    }

    public void setApproversLists(List<ApproversList> approversLists) {
        this.approversLists = approversLists;
    }

    public ApproversList getAl() {
        return al;
    }

    public Info getInfo() {
        return info;
    }

    public ApprovalOfficeSupplies(JSONObject jsonObject) {
        try {
            JSONArray ja = jsonObject.getJSONArray("approvers_list");
            for (int i = 0; i < ja.length(); i++) {
                al = new ApproversList(ja.getJSONObject(i));
                approversLists.add(al);
            }
            JSONObject InfoObj = jsonObject.getJSONObject("info");
            info = new Info(InfoObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ApproversList {

        String time_after;        //	时分
        String time_before;    // 	年月日
        String reply;        // 	批注
        String user;        // 	最后审批人

        public void setTime_after(String time_after) {
            this.time_after = time_after;
        }

        public void setTime_before(String time_before) {
            this.time_before = time_before;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(String approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        private String approvalStatus;

        public String getTime_after() {
            return time_after;
        }

        public String getTime_before() {
            return time_before;
        }

        public String getReply() {
            return reply;
        }

        public String getUser() {
            return user;
        }

        public ApproversList(JSONObject jo) {

            try {

                time_after = jo.getString("time_after");
                time_before = jo.getString("time_before");
                reply = jo.getString("reply");
                user = jo.getString("user");
                approvalStatus = jo.getString("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public class Info {
       String application_time;
        String appr_id;
        String article_name;
        String oal_id;//创建时间
        String remark;
        String status;    //	 	1未通过2通过3驳回
        String time_after;    //	 	1未通过2通过3驳回
        String time_before;    //	 	1未通过2通过3驳回
        String update_time;    //	 	1未通过2通过3驳回
        String username;//

        public String getApplication_time() {
            return application_time;
        }

        public void setApplication_time(String application_time) {
            this.application_time = application_time;
        }

        public String getAppr_id() {
            return appr_id;
        }

        public void setAppr_id(String appr_id) {
            this.appr_id = appr_id;
        }

        public String getArticle_name() {
            return article_name;
        }

        public void setArticle_name(String article_name) {
            this.article_name = article_name;
        }

        public String getOal_id() {
            return oal_id;
        }

        public void setOal_id(String oal_id) {
            this.oal_id = oal_id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTime_after() {
            return time_after;
        }

        public void setTime_after(String time_after) {
            this.time_after = time_after;
        }

        public String getTime_before() {
            return time_before;
        }

        public void setTime_before(String time_before) {
            this.time_before = time_before;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Info(JSONObject jo) {
            try {
                application_time = jo.getString("application_time");
                appr_id = jo.getString("appr_id");
                article_name = jo.getString("article_name");
                oal_id = jo.getString("oal_id");
                remark = jo.getString("remark");
                update_time = jo.getString("update_time");
                time_after = jo.getString("time_after");
                status = jo.getString("status");
                time_before = jo.getString("time_before");
                username = jo.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
