package com.itcrm.GroupInformationPlatform.model;

import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h3>Description: 工作汇报实体类 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/6.<br />
 */
public class ReportList {

    private String id;
    private String sendUser;

    /** 接收时间时分 */
    private String createTimeHI;


    /** 汇报前后半段时间 */
    private String lastTime;
    /** 接收时间年月 */
    private String createTimeYM;

    /** 分类: 1周报、2月报 */
    private String type;
    /** 1未读，2已读 */
    private String status;




    public ReportList(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("rid");
            createTimeHI = jsonObject.getString("createtime_HI");
            createTimeYM = jsonObject.getString("createtime_MD");
            lastTime = jsonObject.getString("last_time");
            sendUser = jsonObject.getString("send_user");
            status = jsonObject.getString("status");
            type = jsonObject.getString("type");

        } catch (JSONException e) {
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getSendUser() {
        return sendUser;
    }

    public String getCreateTimeHI() {
        return createTimeHI;
    }

    public String getLastTime() {
        return lastTime;
    }

    public String getCreateTimeYM() {
        return createTimeYM;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }
}