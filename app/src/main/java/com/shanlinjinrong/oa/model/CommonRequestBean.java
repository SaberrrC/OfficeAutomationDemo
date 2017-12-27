package com.shanlinjinrong.oa.model;

/**
 * Created by dell on 2017/12/27.
 */

public class CommonRequestBean {

    /**
     * code : 100005
     * message : 原密码不正确
     * data : null
     */

    private String code;
    private String message;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
