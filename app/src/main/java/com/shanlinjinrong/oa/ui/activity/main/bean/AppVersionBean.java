package com.shanlinjinrong.oa.ui.activity.main.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/5.
 */

public class AppVersionBean implements Serializable {

    /**
     * code : 000000
     * message : success
     * data : {"iosIsForceUpdate":"1","iosVersion":"1.6.2.0","iosUrl":"https://www.pgyer.com/4qja","androidIsForceUpdate":"1","androidVersion":"1.6.2.0","androidUrl":"https://www.pgyer.com/qSxK"}
     */

    private String code;
    private String message;
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
         * iosIsForceUpdate : 1
         * iosVersion : 1.6.2.0
         * iosUrl : https://www.pgyer.com/4qja
         * androidIsForceUpdate : 1
         * androidVersion : 1.6.2.0
         * androidUrl : https://www.pgyer.com/qSxK
         */

        private String iosIsForceUpdate;
        private String iosVersion;
        private String iosUrl;
        private String androidIsForceUpdate;
        private String androidVersion;
        private String androidUrl;

        public String getIosIsForceUpdate() {
            return iosIsForceUpdate;
        }

        public void setIosIsForceUpdate(String iosIsForceUpdate) {
            this.iosIsForceUpdate = iosIsForceUpdate;
        }

        public String getIosVersion() {
            return iosVersion;
        }

        public void setIosVersion(String iosVersion) {
            this.iosVersion = iosVersion;
        }

        public String getIosUrl() {
            return iosUrl;
        }

        public void setIosUrl(String iosUrl) {
            this.iosUrl = iosUrl;
        }

        public String getAndroidIsForceUpdate() {
            return androidIsForceUpdate;
        }

        public void setAndroidIsForceUpdate(String androidIsForceUpdate) {
            this.androidIsForceUpdate = androidIsForceUpdate;
        }

        public String getAndroidVersion() {
            return androidVersion;
        }

        public void setAndroidVersion(String androidVersion) {
            this.androidVersion = androidVersion;
        }

        public String getAndroidUrl() {
            return androidUrl;
        }

        public void setAndroidUrl(String androidUrl) {
            this.androidUrl = androidUrl;
        }
    }
}
