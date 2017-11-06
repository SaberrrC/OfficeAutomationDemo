package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

/**
 * @Description:
 * @Auther: SaberrrC
 * @Email: saberrrc@163.com
 */
public class ApporveBodyItemBean {
    public String message;//	审批说明	string	非必传
    public String monocode;//	审批单号（以集合方式提交）	string	必传
    public String status;//	状态	string	false:审批未通过,true:审批通过
    public String type;//	审批类型	string	必传，6402签卡申请, 6403出差申请,6404休假申请,6405加班申请

    public ApporveBodyItemBean(String monocode, boolean status, String type) {
        this.monocode = monocode;
        this.status = String.valueOf(status);
        this.type = type;
    }
}
