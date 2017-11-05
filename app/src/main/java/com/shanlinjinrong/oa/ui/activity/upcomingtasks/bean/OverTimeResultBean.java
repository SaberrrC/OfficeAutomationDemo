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
     * data : {"applyDate":"2017-06-04","billCode":"JB201706041837434058","deptName":"善贷宝","jobName":"7.5","nchrapplyWorkFlow":[{"approveResult":"Y","approveResultCH":"批准","billCode":"JB201706041837434058","checkNote":"同意","checkUserName":"王涛","dealDate":"2017-06-06 18:29:19","isCheck":"Y","isCheckCH":"已审批","sendDate":"2017-06-04 18:38:19","sendUserName":"周威"}],"nchroverTimeApplyDetail":[{"actHour":"7.5","billCode":"JB201706041837434058","overTimeBeginTime":"2017-06-04 11:00:00","overTimeEndTime":"2017-06-04 18:30:00","overTimeHour":"7.5","overTimeRemark":"房二贷项目"}],"orgName":"善林（上海）金融信息服务有限公司","postName":"Java开发工程师","psnName":"周威","userName":"周威"}
     * message : 查询成功
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
         * applyDate : 2017-06-04
         * billCode : JB201706041837434058
         * deptName : 善贷宝
         * jobName : 7.5
         * nchrapplyWorkFlow : [{"approveResult":"Y","approveResultCH":"批准","billCode":"JB201706041837434058","checkNote":"同意","checkUserName":"王涛","dealDate":"2017-06-06 18:29:19","isCheck":"Y","isCheckCH":"已审批","sendDate":"2017-06-04 18:38:19","sendUserName":"周威"}]
         * nchroverTimeApplyDetail : [{"actHour":"7.5","billCode":"JB201706041837434058","overTimeBeginTime":"2017-06-04 11:00:00","overTimeEndTime":"2017-06-04 18:30:00","overTimeHour":"7.5","overTimeRemark":"房二贷项目"}]
         * orgName : 善林（上海）金融信息服务有限公司
         * postName : Java开发工程师
         * psnName : 周威
         * userName : 周威
         */

        private String applyDate;
        private String                            billCode;
        private String                            deptName;
        private String                            jobName;
        private String                            orgName;
        private String                            postName;
        private String                            psnName;
        private String                            userName;
        private List<NchrapplyWorkFlowBean>       nchrapplyWorkFlow;
        private List<NchroverTimeApplyDetailBean> nchroverTimeApplyDetail;

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

        public List<NchrapplyWorkFlowBean> getNchrapplyWorkFlow() {
            return nchrapplyWorkFlow;
        }

        public void setNchrapplyWorkFlow(List<NchrapplyWorkFlowBean> nchrapplyWorkFlow) {
            this.nchrapplyWorkFlow = nchrapplyWorkFlow;
        }

        public List<NchroverTimeApplyDetailBean> getNchroverTimeApplyDetail() {
            return nchroverTimeApplyDetail;
        }

        public void setNchroverTimeApplyDetail(List<NchroverTimeApplyDetailBean> nchroverTimeApplyDetail) {
            this.nchroverTimeApplyDetail = nchroverTimeApplyDetail;
        }

        public static class NchrapplyWorkFlowBean {
            /**
             * approveResult : Y
             * approveResultCH : 批准
             * billCode : JB201706041837434058
             * checkNote : 同意
             * checkUserName : 王涛
             * dealDate : 2017-06-06 18:29:19
             * isCheck : Y
             * isCheckCH : 已审批
             * sendDate : 2017-06-04 18:38:19
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

        public static class NchroverTimeApplyDetailBean {
            /**
             * actHour : 7.5
             * billCode : JB201706041837434058
             * overTimeBeginTime : 2017-06-04 11:00:00
             * overTimeEndTime : 2017-06-04 18:30:00
             * overTimeHour : 7.5
             * overTimeRemark : 房二贷项目
             */

            private String actHour;
            private String billCode;
            private String overTimeBeginTime;
            private String overTimeEndTime;
            private String overTimeHour;
            private String overTimeRemark;

            public String getActHour() {
                return actHour;
            }

            public void setActHour(String actHour) {
                this.actHour = actHour;
            }

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

            public String getOverTimeRemark() {
                return overTimeRemark;
            }

            public void setOverTimeRemark(String overTimeRemark) {
                this.overTimeRemark = overTimeRemark;
            }
        }
    }
}
