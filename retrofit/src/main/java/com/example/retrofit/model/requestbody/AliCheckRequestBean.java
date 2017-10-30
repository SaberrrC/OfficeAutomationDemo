package com.example.retrofit.model.requestbody;

/**
 * Created by ${GaoBin} on 2017/8/30 0030.
 */

public class AliCheckRequestBean {

    /**
     * token : 2H5HC0J31R8M2QS4JMK9T147UK
     * deviceId : 123456
     * platform : IOS
     * data : {"outTradeNo":"2017082665656565"}
     */

    private String token;
    private String deviceId;
    private String platform;
    private DataBean data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * outTradeNo : 2017082665656565
         */

        private String outTradeNo;

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }
    }
}
