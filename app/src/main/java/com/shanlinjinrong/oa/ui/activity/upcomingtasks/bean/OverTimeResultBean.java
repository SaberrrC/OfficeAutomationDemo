package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.util.List;

/**
 {
 "code": "000000",
 "message": "查询成功",
 "data": {
 "billCode": "JB201705062145126832",
 "userName": "赵赟",
 "psnName": "赵贇",
 "orgName": "善林（上海）金融信息服务有限公司",
 "deptName": "善贷宝",
 "jobName": null,
 "postName": "Android开发",
 "applyDate": "2017-05-06",
 "nchroverTimeApplyDetail": [
 {
 "billCode": "JB201705062145126832",
 "overTimeBeginTime": "2017-05-06 09:40:00",
 "overTimeEndTime": "2017-05-06 18:00:00",
 "overTimeHour": "8",
 "actHour": "8",
 "overTimeRemark": "房贷二抵项目"
 }
 ],
 "nchrapplyWorkFlow": [
 {
 "billCode": "JB201705062145126832",
 "sendUserName": "赵赟",
 "sendDate": "2017-05-06 21:46:08",
 "checkUserName": "王涛",
 "dealDate": "2017-05-08 11:17:59",
 "approveResult": "Y",
 "approveResultCH": "批准",
 "isCheck": "Y",
 "isCheckCH": "已审批",
 "checkNote": "同意"
 }
 ]
 }
 }
 */
public class OverTimeResultBean {

    /**
     * code : 000000
     * message : 查询成功
     * data : {"billCode":"JB201705062145126832","userName":"赵赟","psnName":"赵贇","orgName":"善林（上海）金融信息服务有限公司","deptName":"善贷宝","jobName":null,"postName":"Android开发","applyDate":"2017-05-06","nchroverTimeApplyDetail":[{"billCode":"JB201705062145126832","overTimeBeginTime":"2017-05-06 09:40:00","overTimeEndTime":"2017-05-06 18:00:00","overTimeHour":"8","actHour":"8","overTimeRemark":"房贷二抵项目"}],"nchrapplyWorkFlow":[{"billCode":"JB201705062145126832","sendUserName":"赵赟","sendDate":"2017-05-06 21:46:08","checkUserName":"王涛","dealDate":"2017-05-08 11:17:59","approveResult":"Y","approveResultCH":"批准","isCheck":"Y","isCheckCH":"已审批","checkNote":"同意"}]}
     */

    private String   code;
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
         * billCode : JB201705062145126832
         * userName : 赵赟
         * psnName : 赵贇
         * orgName : 善林（上海）金融信息服务有限公司
         * deptName : 善贷宝
         * jobName : null
         * postName : Android开发
         * applyDate : 2017-05-06
         * nchroverTimeApplyDetail : [{"billCode":"JB201705062145126832","overTimeBeginTime":"2017-05-06 09:40:00","overTimeEndTime":"2017-05-06 18:00:00","overTimeHour":"8","actHour":"8","overTimeRemark":"房贷二抵项目"}]
         * nchrapplyWorkFlow : [{"billCode":"JB201705062145126832","sendUserName":"赵赟","sendDate":"2017-05-06 21:46:08","checkUserName":"王涛","dealDate":"2017-05-08 11:17:59","approveResult":"Y","approveResultCH":"批准","isCheck":"Y","isCheckCH":"已审批","checkNote":"同意"}]
         */

        private String                            billCode;
        private String                            userName;
        private String                            psnName;
        private String                            orgName;
        private String                            deptName;
        private String                            jobName;
        private String                            postName;
        private String                            applyDate;
        private List<NchroverTimeApplyDetailBean> nchroverTimeApplyDetail;
        private List<NchrapplyWorkFlowBean>       nchrapplyWorkFlow;

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

        public List<NchroverTimeApplyDetailBean> getNchroverTimeApplyDetail() {
            return nchroverTimeApplyDetail;
        }

        public void setNchroverTimeApplyDetail(List<NchroverTimeApplyDetailBean> nchroverTimeApplyDetail) {
            this.nchroverTimeApplyDetail = nchroverTimeApplyDetail;
        }

        public List<NchrapplyWorkFlowBean> getNchrapplyWorkFlow() {
            return nchrapplyWorkFlow;
        }

        public void setNchrapplyWorkFlow(List<NchrapplyWorkFlowBean> nchrapplyWorkFlow) {
            this.nchrapplyWorkFlow = nchrapplyWorkFlow;
        }

        public static class NchroverTimeApplyDetailBean {
            /**
             * billCode : JB201705062145126832
             * overTimeBeginTime : 2017-05-06 09:40:00
             * overTimeEndTime : 2017-05-06 18:00:00
             * overTimeHour : 8
             * actHour : 8
             * overTimeRemark : 房贷二抵项目
             */

            private String billCode;
            private String overTimeBeginTime;
            private String overTimeEndTime;
            private String overTimeHour;
            private String actHour;
            private String overTimeRemark;

            public String getBillCode() {
                return billCode;
            }

            public void setBillCode(String billCode) {
                this.billCode = billCode;
            }

            public String getOverTimeBeginTime() {
                return overTimeBeginTime;
            }

            public void setOverTimeBeginTime(String overTimeBeginTime) {
                this.overTimeBeginTime = overTimeBeginTime;
            }

            public String getOverTimeEndTime() {
                return overTimeEndTime;
            }

            public void setOverTimeEndTime(String overTimeEndTime) {
                this.overTimeEndTime = overTimeEndTime;
            }

            public String getOverTimeHour() {
                return overTimeHour;
            }

            public void setOverTimeHour(String overTimeHour) {
                this.overTimeHour = overTimeHour;
            }

            public String getActHour() {
                return actHour;
            }

            public void setActHour(String actHour) {
                this.actHour = actHour;
            }

            public String getOverTimeRemark() {
                return overTimeRemark;
            }

            public void setOverTimeRemark(String overTimeRemark) {
                this.overTimeRemark = overTimeRemark;
            }
        }

        public static class NchrapplyWorkFlowBean {
            /**
             * billCode : JB201705062145126832
             * sendUserName : 赵赟
             * sendDate : 2017-05-06 21:46:08
             * checkUserName : 王涛
             * dealDate : 2017-05-08 11:17:59
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
