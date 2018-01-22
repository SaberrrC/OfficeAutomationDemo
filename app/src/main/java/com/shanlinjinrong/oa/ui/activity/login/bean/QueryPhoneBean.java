package com.shanlinjinrong.oa.ui.activity.login.bean;

import java.io.Serializable;

/**
 * Created by dell on 2017/12/29.
 */

public class QueryPhoneBean implements Serializable{

    /**
     * code : 000000
     * message : 查询成功
     * data : {"phone":"15122665511"}
     */

    private String code;
    private String   message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * phone : 15122665511
         */

        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
