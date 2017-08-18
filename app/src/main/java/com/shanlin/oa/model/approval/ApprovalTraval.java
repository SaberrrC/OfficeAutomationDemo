package com.shanlin.oa.model.approval;

import com.shanlin.oa.model.TravalSingle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by Tsui on Date:2016/11/22 11:47
 * Description:审批：差旅
 */
public class ApprovalTraval {
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

    public ApprovalTraval(JSONObject jsonObject) {
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
        String appr_id;
        String day;
        String remark;
        String status;    //	 	1未通过2通过3驳回
        String time_after;    //	 	1未通过2通过3驳回
        String time_before;    //	 	1未通过2通过3驳回
        String title;
        String username;//
        List<TravalSingle> text=new ArrayList<>();

        public String getAppr_id() {
            return appr_id;
        }

        public String getDay() {
            return day;
        }

        public String getRemark() {
            return remark;
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

        public String getTitle() {
            return title;
        }

        public String getUsername() {
            return username;
        }

        public List<TravalSingle> getText() {
            return text;
        }

        public Info(JSONObject jo) {
            try {
                appr_id = jo.getString("appr_id");
                day = jo.getString("day");
                remark = jo.getString("remark");
                status = jo.getString("status");
                time_after = jo.getString("time_after");
                time_before = jo.getString("time_before");
                title = jo.getString("title");
                username = jo.getString("username");
                JSONArray arr = jo.getJSONArray("text");
                for (int i = 0; i < arr.length(); i++) {
                    TravalSingle ts = new TravalSingle(arr.getJSONObject(i).getString("address")
                            , arr.getJSONObject(i).getString("start_time"), arr.getJSONObject(i)
                            .getString("end_time"),arr.getJSONObject(i).getString("vehicle"));
                    text.add(ts);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
