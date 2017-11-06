package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean;

import java.util.List;

public class SingReasonBean {

    /**
     * code : 000000
     * message : success
     * data : [{"SIGNCAUSE":"忘记打卡","SIGNCAUSEID":"1001A110000000014XET"},{"SIGNCAUSE":"考勤机故障","SIGNCAUSEID":"1001A110000000014XEU"},{"SIGNCAUSE":"地铁故障","SIGNCAUSEID":"1001A110000000014XEW"}]
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
         * SIGNCAUSE : 忘记打卡
         * SIGNCAUSEID : 1001A110000000014XET
         */

        private String SIGNCAUSE;
        private String SIGNCAUSEID;

        public String getSIGNCAUSE() {
            return SIGNCAUSE;
        }

        public void setSIGNCAUSE(String SIGNCAUSE) {
            this.SIGNCAUSE = SIGNCAUSE;
        }

        public String getSIGNCAUSEID() {
            return SIGNCAUSEID;
        }

        public void setSIGNCAUSEID(String SIGNCAUSEID) {
            this.SIGNCAUSEID = SIGNCAUSEID;
        }
    }
}
