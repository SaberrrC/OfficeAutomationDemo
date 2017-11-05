package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.util.List;

/**
 {
 "code": "000000",
 "message": "success",
 "data": {
 "billCode": "QK20171105000064",
 "userName": "周威",
 "psnName": "周威",
 "orgName": "善林（上海）金融信息服务有限公司",
 "deptName": "善贷宝",
 "jobName": "周威",
 "postName": "Java开发工程师",
 "applyDate": "2017-11-05",
 "nchrSignDetails": [
 {
 "signCauseId": "1001A110000000014XEW",
 "signCause": "地铁故障",
 "signRemark": "吧",
 "signTime": "2017-11-05 14:21:00"
 },
 {
 "signCauseId": "1001A110000000014XEU",
 "signCause": "考勤机故障",
 "signRemark": "哦哦",
 "signTime": "2017-11-05 14:21:00"
 }
 ],
 "applyWorkFlows": [
 {
 "billCode": "QK20171105000064",
 "sendUserName": "周威",
 "sendDate": "2017-11-05 14:23:24",
 "checkUserName": "王涛",
 "dealDate": "周威",
 "approveResult": "周威",
 "approveResultCH": "周威",
 "isCheck": "N",
 "isCheckCH": "未审批",
 "checkNote": "周威"
 }
 ]
 }
 }
 */
public class CardResultBean {


    /**
     * code : 000000
     * data : {"applyDate":"2017-11-05","applyWorkFlows":[{"approveResult":"周威","approveResultCH":"周威","billCode":"QK20171105000064","checkNote":"周威","checkUserName":"王涛","dealDate":"周威","isCheck":"N","isCheckCH":"","sendDate":"2017-11-05 14:23:24","sendUserName":"周威"}],"billCode":"QK20171105000064","deptName":"善贷宝","jobName":"周威","nchrSignDetails":[{"signCause":"地铁故障","signCauseId":"1001A110000000014XEW","signRemark":"吧","signTime":"2017-11-05 14:21:00"},{"signCause":"考勤机故障","signCauseId":"1001A110000000014XEU","signRemark":"哦哦","signTime":"2017-11-05 14:21:00"}],"orgName":"善林（上海）金融信息服务有限公司","postName":"Java开发工程师","psnName":"周威","userName":"周威"}
     * message : success
     */

    private String code;
    private DataBean data;
    private String   message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * applyDate : 2017-11-05
         * applyWorkFlows : [{"approveResult":"周威","approveResultCH":"周威","billCode":"QK20171105000064","checkNote":"周威","checkUserName":"王涛","dealDate":"周威","isCheck":"N","isCheckCH":"","sendDate":"2017-11-05 14:23:24","sendUserName":"周威"}]
         * billCode : QK20171105000064
         * deptName : 善贷宝
         * jobName : 周威
         * nchrSignDetails : [{"signCause":"地铁故障","signCauseId":"1001A110000000014XEW","signRemark":"吧","signTime":"2017-11-05 14:21:00"},{"signCause":"考勤机故障","signCauseId":"1001A110000000014XEU","signRemark":"哦哦","signTime":"2017-11-05 14:21:00"}]
         * orgName : 善林（上海）金融信息服务有限公司
         * postName : Java开发工程师
         * psnName : 周威
         * userName : 周威
         */

        private String applyDate;
        private String                    billCode;
        private String                    deptName;
        private String                    jobName;
        private String                    orgName;
        private String                    postName;
        private String                    psnName;
        private String                    userName;
        private List<ApplyWorkFlowsBean>  applyWorkFlows;
        private List<NchrSignDetailsBean> nchrSignDetails;

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public String getBillCode() {
            return billCode;
        }

        public void setBillCode(String billCode) {
            this.billCode = billCode;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String jobName) {
            this.jobName = jobName;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public String getPsnName() {
            return psnName;
        }

        public void setPsnName(String psnName) {
            this.psnName = psnName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public List<ApplyWorkFlowsBean> getApplyWorkFlows() {
            return applyWorkFlows;
        }

        public void setApplyWorkFlows(List<ApplyWorkFlowsBean> applyWorkFlows) {
            this.applyWorkFlows = applyWorkFlows;
        }

        public List<NchrSignDetailsBean> getNchrSignDetails() {
            return nchrSignDetails;
        }

        public void setNchrSignDetails(List<NchrSignDetailsBean> nchrSignDetails) {
            this.nchrSignDetails = nchrSignDetails;
        }

        public static class ApplyWorkFlowsBean {
            /**
             * approveResult : 周威
             * approveResultCH : 周威
             * billCode : QK20171105000064
             * checkNote : 周威
             * checkUserName : 王涛
             * dealDate : 周威
             * isCheck : N
             * isCheckCH :
             * sendDate : 2017-11-05 14:23:24
             * sendUserName : 周威
             */

            private String approveResult;
            private String approveResultCH;
            private String billCode;
            private String checkNote;
            private String checkUserName;
            private String dealDate;
            private String isCheck;
            private String isCheckCH;
            private String sendDate;
            private String sendUserName;

            public String getApproveResult() {
                return approveResult;
            }

            public void setApproveResult(String approveResult) {
                this.approveResult = approveResult;
            }

            public String getApproveResultCH() {
                return approveResultCH;
            }

            public void setApproveResultCH(String approveResultCH) {
                this.approveResultCH = approveResultCH;
            }

            public String getBillCode() {
                return billCode;
            }

            public void setBillCode(String billCode) {
                this.billCode = billCode;
            }

            public String getCheckNote() {
                return checkNote;
            }

            public void setCheckNote(String checkNote) {
                this.checkNote = checkNote;
            }

            public String getCheckUserName() {
                return checkUserName;
            }

            public void setCheckUserName(String checkUserName) {
                this.checkUserName = checkUserName;
            }

            public String getDealDate() {
                return dealDate;
            }

            public void setDealDate(String dealDate) {
                this.dealDate = dealDate;
            }

            public String getIsCheck() {
                return isCheck;
            }

            public void setIsCheck(String isCheck) {
                this.isCheck = isCheck;
            }

            public String getIsCheckCH() {
                return isCheckCH;
            }

            public void setIsCheckCH(String isCheckCH) {
                this.isCheckCH = isCheckCH;
            }

            public String getSendDate() {
                return sendDate;
            }

            public void setSendDate(String sendDate) {
                this.sendDate = sendDate;
            }

            public String getSendUserName() {
                return sendUserName;
            }

            public void setSendUserName(String sendUserName) {
                this.sendUserName = sendUserName;
            }
        }

        public static class NchrSignDetailsBean {
            /**
             * signCause : 地铁故障
             * signCauseId : 1001A110000000014XEW
             * signRemark : 吧
             * signTime : 2017-11-05 14:21:00
             */

            private String signCause;
            private String signCauseId;
            private String signRemark;
            private String signTime;

            public String getSignCause() {
                return signCause;
            }

            public void setSignCause(String signCause) {
                this.signCause = signCause;
            }

            public String getSignCauseId() {
                return signCauseId;
            }

            public void setSignCauseId(String signCauseId) {
                this.signCauseId = signCauseId;
            }

            public String getSignRemark() {
                return signRemark;
            }

            public void setSignRemark(String signRemark) {
                this.signRemark = signRemark;
            }

            public String getSignTime() {
                return signTime;
            }

            public void setSignTime(String signTime) {
                this.signTime = signTime;
            }
        }
    }
}
