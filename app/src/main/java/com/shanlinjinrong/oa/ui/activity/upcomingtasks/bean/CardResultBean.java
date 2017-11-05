package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.util.List;

public class CardResultBean {

    /**
     * code : 000000
     * message : success
     * data : {"billCode":"QK201706050924394105","userName":"赵赟","psnName":"赵贇","orgName":"善林（上海）金融信息服务有限公司","deptName":"善贷宝","jobName":null,"postName":"Android开发","applyDate":"2017-06-05","nchrSignDetails":[{"signCauseId":"1001A110000000014XET","signCause":"忘记打卡","signRemark":"忘记打卡","signTime":"2017-06-05 09:00:00"}],"applyWorkFlows":[{"billCode":"QK201706050924394105","sendUserName":"赵赟","sendDate":"2017-06-05 09:25:09","checkUserName":"王涛","dealDate":"2017-06-06 18:29:11","approveResult":"Y","approveResultCH":"批准","isCheck":"Y","isCheckCH":"已审批","checkNote":"同意"}]}
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
         * billCode : QK201706050924394105
         * userName : 赵赟
         * psnName : 赵贇
         * orgName : 善林（上海）金融信息服务有限公司
         * deptName : 善贷宝
         * jobName : null
         * postName : Android开发
         * applyDate : 2017-06-05
         * nchrSignDetails : [{"signCauseId":"1001A110000000014XET","signCause":"忘记打卡","signRemark":"忘记打卡","signTime":"2017-06-05 09:00:00"}]
         * applyWorkFlows : [{"billCode":"QK201706050924394105","sendUserName":"赵赟","sendDate":"2017-06-05 09:25:09","checkUserName":"王涛","dealDate":"2017-06-06 18:29:11","approveResult":"Y","approveResultCH":"批准","isCheck":"Y","isCheckCH":"已审批","checkNote":"同意"}]
         */

        private String billCode;
        private String                    userName;
        private String                    psnName;
        private String                    orgName;
        private String                    deptName;
        private String                    jobName;
        private String                    postName;
        private String                    applyDate;
        private List<NchrSignDetailsBean> nchrSignDetails;
        private List<ApplyWorkFlowsBean>  applyWorkFlows;

        public String getBillCode() {
            return billCode;
        }

        public void setBillCode(String billCode) {
            this.billCode = billCode;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPsnName() {
            return psnName;
        }

        public void setPsnName(String psnName) {
            this.psnName = psnName;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
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

        public String getPostName() {
            return postName;
        }

        public void setPostName(String postName) {
            this.postName = postName;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public List<NchrSignDetailsBean> getNchrSignDetails() {
            return nchrSignDetails;
        }

        public void setNchrSignDetails(List<NchrSignDetailsBean> nchrSignDetails) {
            this.nchrSignDetails = nchrSignDetails;
        }

        public List<ApplyWorkFlowsBean> getApplyWorkFlows() {
            return applyWorkFlows;
        }

        public void setApplyWorkFlows(List<ApplyWorkFlowsBean> applyWorkFlows) {
            this.applyWorkFlows = applyWorkFlows;
        }

        public static class NchrSignDetailsBean {
            /**
             * signCauseId : 1001A110000000014XET
             * signCause : 忘记打卡
             * signRemark : 忘记打卡
             * signTime : 2017-06-05 09:00:00
             */

            private String signCauseId;
            private String signCause;
            private String signRemark;
            private String signTime;

            public String getSignCauseId() {
                return signCauseId;
            }

            public void setSignCauseId(String signCauseId) {
                this.signCauseId = signCauseId;
            }

            public String getSignCause() {
                return signCause;
            }

            public void setSignCause(String signCause) {
                this.signCause = signCause;
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

        public static class ApplyWorkFlowsBean {
            /**
             * billCode : QK201706050924394105
             * sendUserName : 赵赟
             * sendDate : 2017-06-05 09:25:09
             * checkUserName : 王涛
             * dealDate : 2017-06-06 18:29:11
             * approveResult : Y
             * approveResultCH : 批准
             * isCheck : Y
             * isCheckCH : 已审批
             * checkNote : 同意
             */

            private String billCode;
            private String sendUserName;
            private String sendDate;
            private String checkUserName;
            private String dealDate;
            private String approveResult;
            private String approveResultCH;
            private String isCheck;
            private String isCheckCH;
            private String checkNote;

            public String getBillCode() {
                return billCode;
            }

            public void setBillCode(String billCode) {
                this.billCode = billCode;
            }

            public String getSendUserName() {
                return sendUserName;
            }

            public void setSendUserName(String sendUserName) {
                this.sendUserName = sendUserName;
            }

            public String getSendDate() {
                return sendDate;
            }

            public void setSendDate(String sendDate) {
                this.sendDate = sendDate;
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

            public String getCheckNote() {
                return checkNote;
            }

            public void setCheckNote(String checkNote) {
                this.checkNote = checkNote;
            }
        }
    }
}
