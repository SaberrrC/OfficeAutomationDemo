package com.shanlinjinrong.oa.model.approval;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by Tsui on Date:2016/11/22 11:47
 * Description:审批：请假、公出
 */
public class ApprovalLeave {


    private ApproversList al;
    private Info info;
    List<ApproversList> approversLists=new ArrayList<>();

    public ApproversList getAl() {
        return al;
    }

    public Info getInfo() {
        return info;
    }

    public List<ApproversList> getApproversLists() {
        return approversLists;
    }

    public void setApproversLists(List<ApproversList> approversLists) {
        this.approversLists = approversLists;
    }

    public ApprovalLeave(JSONObject jsonObject) {
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
        String create_time;//创建时间
        String day;//请假时长
        String end_time;
        String remark;
        String start_time;
        String status;    //	 	1未通过2通过3驳回
        String time_after;    //	 	1未通过2通过3驳回
        String time_before;    //	 	1未通过2通过3驳回
        String type;//请假类型
        String username;//

        public String getCreate_time() {
            return create_time;
        }

        public String getDay() {
            return day;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getRemark() {
            return remark;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getStatus() {
            return status;
        }

        public String getTime_after() {
            return time_after;
        }

        public String getTime_before() {
            return time_before;
        }

        public String getType() {
            return type;
        }


        public String getUsername() {
            return username;
        }

        public Info(JSONObject jo) {
            try {
                day = jo.getString("day");
                type = jo.getString("type");
                end_time = jo.getString("end_time");
                remark = jo.getString("remark");
                start_time = jo.getString("start_time");
                time_after = jo.getString("time_after");
                status = jo.getString("status");
                time_before = jo.getString("time_before");
                create_time = jo.getString("create_time");
                username = jo.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
