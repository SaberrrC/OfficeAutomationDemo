package com.hyphenate.easeui;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：王凤旭
 * 时间：2017/11/29
 * 描述：
 */

public class UserDetailsBean implements Serializable {


    /**
     * code : 200
     * info : success
     * data : [{"uid":"78","oid":"611","isleader":"0","code":"011000439","username":"宋慧玲","phone":"13023128805","email":"songhuiling@shanlinjinrong.com","sex":"女","organ":"核算组","postname":"会计","token":null,"img":""}]
     */

    private int code;
    private String info;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 78
         * oid : 611
         * isleader : 0
         * code : 011000439
         * username : 宋慧玲
         * phone : 13023128805
         * email : songhuiling@shanlinjinrong.com
         * sex : 女
         * organ : 核算组
         * postname : 会计
         * token : null
         * img :
         */

        private String uid;
        private String oid;
        private String isleader;
        private String code;
        private String username;
        private String phone;
        private String email;
        private String sex;
        private String organ;
        private String postname;
        private String token;
        private String img;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getIsleader() {
            return isleader;
        }

        public void setIsleader(String isleader) {
            this.isleader = isleader;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public String getOrgan() {
            return organ;
        }

        public void setOrgan(String organ) {
            this.organ = organ;
        }

        public String getPostname() {
            return postname;
        }

        public void setPostname(String postname) {
            this.postname = postname;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
