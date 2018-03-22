package com.shanlinjinrong.oa.model;

import java.io.Serializable;

/**
 * Created by dell on 2017/12/27.
 */

public class UserInfo implements Serializable{

    /**
     * code : 000000
     * message : 登陆成功
     * data : {"uid":"50362","token":"65e7443057754dcf9a964d76d43c39a21514343392755","isleader":"0","username":"吴佳稚","email":"wujiazhi@shanlinjinrong.com","code":"011000310","phone":"13482322825","password":"$2y$10$LA9e1JmVrHTkF/9dnWQuV.94l/ns4oKBEVt0iOCmwSmrZ/30F4ufq","sex":"女","is_initial_pwd":"1","portrait":"","department_name":"核算组","department_id":"420","company_id":"SL","post_id":"0","post_title":"会计","yx_token":null,"oid":"420","hiredate":"","company_name":"善林（上海）金融信息服务有限公司"}
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
         * uid : 50362
         * token : 65e7443057754dcf9a964d76d43c39a21514343392755
         * isleader : 0
         * username : 吴佳稚
         * email : wujiazhi@shanlinjinrong.com
         * code : 011000310
         * phone : 13482322825
         * password : $2y$10$LA9e1JmVrHTkF/9dnWQuV.94l/ns4oKBEVt0iOCmwSmrZ/30F4ufq
         * sex : 女
         * is_initial_pwd : 1
         * portrait :
         * department_name : 核算组
         * department_id : 420
         * company_id : SL
         * post_id : 0
         * post_title : 会计
         * yx_token : null
         * oid : 420
         * hiredate :
         * company_name : 善林（上海）金融信息服务有限公司
         */

        private String needYzm;
        private String uid;
        private String token;
        private String isleader;
        private String username;
        private String email;
        private String code;
        private String phone;
        private String password;
        private String sex;
        private String is_initial_pwd;
        private String portrait;
        private String department_name;
        private String department_id;
        private String company_id;
        private String post_id;
        private String post_title;
        private String yx_token;
        private String oid;
        private String hiredate;
        private String company_name;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getIsleader() {
            return isleader;
        }

        public void setIsleader(String isleader) {
            this.isleader = isleader;
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

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getIs_initial_pwd() {
            return is_initial_pwd;
        }

        public void setIs_initial_pwd(String is_initial_pwd) {
            this.is_initial_pwd = is_initial_pwd;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getDepartment_name() {
            return department_name;
        }

        public void setDepartment_name(String department_name) {
            this.department_name = department_name;
        }

        public String getDepartment_id() {
            return department_id;
        }

        public void setDepartment_id(String department_id) {
            this.department_id = department_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getPost_title() {
            return post_title;
        }

        public void setPost_title(String post_title) {
            this.post_title = post_title;
        }

        public String getYx_token() {
            return yx_token;
        }

        public void setYx_token(String yx_token) {
            this.yx_token = yx_token;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getHiredate() {
            return hiredate;
        }

        public void setHiredate(String hiredate) {
            this.hiredate = hiredate;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getNeedYzm() {
            return needYzm;
        }

        public void setNeedYzm(String needYzm) {
            this.needYzm = needYzm;
        }
    }
}
