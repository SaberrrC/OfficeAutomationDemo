package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean;

import java.util.List;

public class BusinessTypeBean {
    /**
     * code : 000000
     * message : success
     * data : [{"name":"出差","id":"1001A1100000000154IR"},{"name":"外出","id":"1001A1100000000154IU"}]
     */

    private String code;
    private String message;
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
         * name : 出差
         * id : 1001A1100000000154IR
         */

        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
