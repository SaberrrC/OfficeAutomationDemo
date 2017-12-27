package com.hyphenate.easeui;

import java.io.Serializable;

/**
 * 作者：王凤旭
 * 时间：2017/11/29
 * 描述：
 */

public class UserDetailsBean implements Serializable {

    /**
     * code : 000000
     * message : 获取用户信息成功
     * data : {"uid":"50363","oid":"61","isleader":"0","code":"011000311","username":"杨佳晨","phone":"18149776583","email":"yangjiachen@shanlinjinrong.com","sex":"男","organ":"员工关系组-营业部","postname":"人事专员","token":null,"portrait":"","joindate":"2016-07-11","birthdate":"1993-11-01","education":"专科"}
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
         * uid : 50363
         * oid : 61
         * isleader : 0
         * code : 011000311
         * username : 杨佳晨
         * phone : 18149776583
         * email : yangjiachen@shanlinjinrong.com
         * sex : 男
         * organ : 员工关系组-营业部
         * postname : 人事专员
         * token : null
         * portrait :
         * joindate : 2016-07-11
         * birthdate : 1993-11-01
         * education : 专科
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
        private Object token;
        private String portrait;
        private String joindate;
        private String birthdate;
        private String education;

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

        public Object getToken() {
            return token;
        }

        public void setToken(Object token) {
            this.token = token;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getJoindate() {
            return joindate;
        }

        public void setJoindate(String joindate) {
            this.joindate = joindate;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }
    }
}
