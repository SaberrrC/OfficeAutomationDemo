package com.shanlinjinrong.oa.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h3>Description: 工作汇报实体类 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/6.<br />
 */
public class ReportDetail {

    /** 是否可以回复 1：可以 2：不可以 */
    private String can_reply;
    private String content;
    /** 是否可以回复 抄送人 */
    private String copyname;
    /** 发送时间 */
    private String created;
    /** 图片地址 */
   private  String[] imgs;
    /** 结束时间 */
    private String lastTime;
    /** 接收人 */
    private String receivename;
    /** 回复 */
    private String reply;
    /** 汇报人姓名 */
    private String sendname;
    /** 分类: 1周报、2月报 */
    private String type;










    public ReportDetail(JSONObject jsonObject) {
        try {
            content = jsonObject.getString("content");
            can_reply = jsonObject.getString("can_reply");
            copyname = jsonObject.getString("copyname");
            lastTime = jsonObject.getString("last_time");
            sendname = jsonObject.getString("sendname");
            reply = jsonObject.getString("reply");
            created = jsonObject.getString("created");
            type = jsonObject.getString("type");
        } catch (JSONException e) {
        }
    }


}