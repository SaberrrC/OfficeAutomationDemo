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
 * Description:审批：加班
 */
public class ApprovalOverTime {


    private ApproversList al;
    List<ApproversList> approversLists=new ArrayList<>();

    public ApproversList getAl() {
        return al;
    }


    public List<ApproversList> getApproversLists() {
        return approversLists;
    }



    public ApprovalOverTime(JSONObject jsonObject) {
        try {
            JSONArray ja = jsonObject.getJSONArray("approvers_list");
            for (int i = 0; i < ja.length(); i++) {
                al = new ApproversList(ja.getJSONObject(i));
                approversLists.add(al);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ApproversList {

        String time_after;        //	时分
        String time_before;    // 	年月日
        String reply;        // 	批注
        String user;        // 	最后审批人



        public void setReply(String reply) {
            this.reply = reply;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getApprovalStatus() {
            return approvalStatus;
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




}
