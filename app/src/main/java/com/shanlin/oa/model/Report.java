package com.shanlin.oa.model;

import com.shanlin.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h3>Description: 工作汇报实体类 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/6.<br />
 */
public class Report {

    private String id;
    private String content;
    private String sendUser;
    /** 汇报前半段时间 */
    private String firstTime;
    /** 汇报前后半段时间 */
    private String lastTime;
    /** 接收时间年月 */
    private String createTimeYM;
    /** 详情页接收时间 */
    private String createTime;
    /** 接收时间时分 */
    private String createTimeHI;
    /** 分类: 1周报、2月报 */
    private String type;
    /** 1未读，2已读 */
    private String status;

    /** 是否可以回复 1：可以 2：不可以 */
    private String can_reply;
    /** 是否可以回复 抄送人 */
    private String copyname;
    /** 图片地址 */
//    private Array<String> imgs;



    public Report(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("rid");
            content = jsonObject.getString("content");
            sendUser = jsonObject.getString("send_user");
            firstTime = jsonObject.getString("first_time");
            lastTime = jsonObject.getString("last_time");
            createTimeYM = jsonObject.getString("createtime_MD");
            createTimeHI = jsonObject.getString("createtime_HI");
            createTime = jsonObject.getString("createtime");
            type = jsonObject.getString("type");
            status = jsonObject.getString("status");
            LogUtils.e("report的构造方法执行了");
        } catch (JSONException e) {
        }
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getSendUser() {
        return sendUser;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getCreateTimeYM() {
        return createTimeYM;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getCreateTimeHI() {
        return createTimeHI;
    }
}