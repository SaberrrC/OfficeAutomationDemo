package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

/**
 * @Description:
 * @Auther: SaberrrC
 * @Email: saberrrc@163.com
 */
public class DeleteBean {

    /**
     * code : 000000
     * message : success
     * data : {"reason":"ok","status":"1"}
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
         * reason : ok
         * status : 1
         */

        private String reason;
        private String status;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
