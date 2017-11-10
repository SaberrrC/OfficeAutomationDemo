package com.example.retrofit.model.requestbody;

import java.util.List;

public class EvectionBody {

    /**
     * nchrevectionApplyDetail : [{"evectionAddress":"图","evectionRemark":"一样","endTime":"2017-11-07 20:54:00","startTime":"2017-11-06 20:54:00","handOverPepole":"0001A31000000017NVJ2","timeDifference":"1"},{"evectionAddress":"图","evectionRemark":"一样","endTime":"2017-11-07 20:54:00","startTime":"2017-11-06 20:54:00","handOverPepole":"0001A31000000017NVJ2","timeDifference":"1"}]
     * billCode : CC20171108005921
     * type : 1001A1100000000154IR
     * applyDate : 2017-11-08
     */

    private String billCode;
    private String type;
    private String applyDate;
    private List<NchrevectionApplyDetailBean> nchrevectionApplyDetail;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public List<NchrevectionApplyDetailBean> getNchrevectionApplyDetail() {
        return nchrevectionApplyDetail;
    }

    public void setNchrevectionApplyDetail(List<NchrevectionApplyDetailBean> nchrevectionApplyDetail) {
        this.nchrevectionApplyDetail = nchrevectionApplyDetail;
    }

    public static class NchrevectionApplyDetailBean {
        /**
         * evectionAddress : 图
         * evectionRemark : 一样
         * endTime : 2017-11-07 20:54:00
         * startTime : 2017-11-06 20:54:00
         * handOverPepole : 0001A31000000017NVJ2
         * timeDifference : 1
         */

        private String evectionAddress;
        private String evectionRemark;
        private String endTime;
        private String startTime;
        private String handOverPepole;
        private String timeDifference;

        public String getEvectionAddress() {
            return evectionAddress;
        }

        public void setEvectionAddress(String evectionAddress) {
            this.evectionAddress = evectionAddress;
        }

        public String getEvectionRemark() {
            return evectionRemark;
        }

        public void setEvectionRemark(String evectionRemark) {
            this.evectionRemark = evectionRemark;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getHandOverPepole() {
            return handOverPepole;
        }

        public void setHandOverPepole(String handOverPepole) {
            this.handOverPepole = handOverPepole;
        }

        public String getTimeDifference() {
            return timeDifference;
        }

        public void setTimeDifference(String timeDifference) {
            this.timeDifference = timeDifference;
        }
    }
}
