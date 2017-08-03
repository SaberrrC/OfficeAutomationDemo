package com.shanlin.oa.model.approval;

import com.shanlin.oa.utils.LogUtils;

import org.json.JSONObject;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.model
 * Author:Created by Tsui on Date:2016/11/16 10:31
 * Description:审批实体类
 */
public class Approval {

    private String appr_id;//审批分类ID 2办公用品3请假4出差
    private String appr_name;//审批类型ID 2 办公用品3 请假 4 出差
    private String create_time;//申请时间
    private String oa_id;//审批流程ID
    private String oal_id;//审批内容ID
    private String status;//1为审批中 2为通过 3为驳回
    private String username;//申请人姓名


    public Approval(JSONObject jsonObject, boolean isMeLaunch) {
        try {
            if (isMeLaunch) {
                //我发起的，
                appr_id = jsonObject.getString("appr_id");
                create_time = jsonObject.getString("create_time");
                appr_name = jsonObject.getString("appr_name");
                oal_id = jsonObject.getString("oal_id");
                status = jsonObject.getString("status");
                username = jsonObject.getString("username");
                LogUtils.e(appr_id+","+create_time+","+appr_name+","+oal_id+","+status+","+username);
            } else {
                //待我审批
                appr_id = jsonObject.getString("appr_id");
                appr_name = jsonObject.getString("appr_name");
                create_time = jsonObject.getString("create_time");
                oa_id = jsonObject.getString("oa_id");
                oal_id = jsonObject.getString("oal_id");
                status = jsonObject.getString("status");
                username = jsonObject.getString("username");
            }
        } catch (Exception e) {
        }
    }

    public String getAppr_id() {
        return appr_id;
    }

    public void setAppr_id(String appr_id) {
        this.appr_id = appr_id;
    }

    public String getAppr_name() {
        return appr_name;
    }

    public void setAppr_name(String appr_name) {
        this.appr_name = appr_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOa_id() {
        return oa_id;
    }

    public void setOa_id(String oa_id) {
        this.oa_id = oa_id;
    }

    public String getOal_id() {
        return oal_id;
    }

    public void setOal_id(String oal_id) {
        this.oal_id = oal_id;
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
