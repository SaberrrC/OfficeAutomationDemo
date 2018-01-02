package com.hyphenate.easeui.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：王凤旭
 * 时间：2017/8/22
 * 描述：
 */

public class UserInfoDetailsBean implements Serializable {

    /**
     * code : 000000
     * message : 查询成功
     * data : [{"uid":15191,"code":"010051647","username":"赵紫祺","email":"zhaoziqi_jl@shanlinjinrong.com","sex":"女","img":null}]
     */

    private String code;
    private String         message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 15191
         * code : 010051647
         * username : 赵紫祺
         * email : zhaoziqi_jl@shanlinjinrong.com
         * sex : 女
         * img : null
         */

        private int uid;
        private String code;
        private String username;
        private String email;
        private String sex;
        private String img;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
