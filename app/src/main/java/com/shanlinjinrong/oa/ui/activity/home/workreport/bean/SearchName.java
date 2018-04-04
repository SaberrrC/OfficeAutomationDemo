package com.shanlinjinrong.oa.ui.activity.home.workreport.bean;

import java.util.List;

/**
 * Created by dell on 2018/4/4.
 */

public class SearchName {
    /**
     * code : 000000
     * message : success
     * data : [{"uid":"1","username":"王玖琴","phone":"","email":null,"portraits":null,"sex":"女","post_title":"高级理财顾问（P3）","post_id":"00017d650b5640759eef663dd583a68d","department_id":"5614","department_name":"大团队A","code":"010013703"}]
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
         * uid : 1
         * username : 王玖琴
         * phone :
         * email : null
         * portraits : null
         * sex : 女
         * post_title : 高级理财顾问（P3）
         * post_id : 00017d650b5640759eef663dd583a68d
         * department_id : 5614
         * department_name : 大团队A
         * code : 010013703
         */

        private String uid;
        private String username;
        private String phone;
        private Object email;
        private Object portraits;
        private String sex;
        private String post_title;
        private String post_id;
        private String department_id;
        private String department_name;
        private String code;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getPortraits() {
            return portraits;
        }

        public void setPortraits(Object portraits) {
            this.portraits = portraits;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPost_title() {
            return post_title;
        }

        public void setPost_title(String post_title) {
            this.post_title = post_title;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getDepartment_id() {
            return department_id;
        }

        public void setDepartment_id(String department_id) {
            this.department_id = department_id;
        }

        public String getDepartment_name() {
            return department_name;
        }

        public void setDepartment_name(String department_name) {
            this.department_name = department_name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
