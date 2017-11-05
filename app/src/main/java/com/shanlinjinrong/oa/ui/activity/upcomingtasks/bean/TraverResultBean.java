package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.util.List;

public class TraverResultBean {

    /**
     * code : 000000
     * data : {"applyDate":"2017-06-08","billCode":"CC201706081353205418","deptname":"融资租赁部","jobname":"Y","nchrapplyWorkFlow":[{"approveResult":"Y","approveResultCH":"批准","checkNote":"Y","checkUserName":"庄振康","dealDate":"2017-06-14 14:12:26","isCheck":"Y","isCheckCH":"已审批","sendDate":"2017-06-08 13:53:45","sendUserName":"储耀明"}],"nchrevectionApplyDetail":[{"endTime":"2017-06-08 17:30:00","evectionAddress":"上海","evectionRemark":"办公","handOverPepole":"Y","psnname":"Y","startTime":"2017-06-08 13:53:00","timeDifference":"4"}],"orgname":"善林（上海）金融信息服务有限公司","postname":"市场运营","psnname":"储耀明","type":"1001A1100000000154IU","typeName":"外出","userName":"储耀明"}
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
         * applyDate : 2017-06-08
         * billCode : CC201706081353205418
         * deptname : 融资租赁部
         * jobname : Y
         * nchrapplyWorkFlow : [{"approveResult":"Y","approveResultCH":"批准","checkNote":"Y","checkUserName":"庄振康","dealDate":"2017-06-14 14:12:26","isCheck":"Y","isCheckCH":"已审批","sendDate":"2017-06-08 13:53:45","sendUserName":"储耀明"}]
         * nchrevectionApplyDetail : [{"endTime":"2017-06-08 17:30:00","evectionAddress":"上海","evectionRemark":"办公","handOverPepole":"Y","psnname":"Y","startTime":"2017-06-08 13:53:00","timeDifference":"4"}]
         * orgname : 善林（上海）金融信息服务有限公司
         * postname : 市场运营
         * psnname : 储耀明
         * type : 1001A1100000000154IU
         * typeName : 外出
         * userName : 储耀明
         */

        private String applyDate;
        private String                            billCode;
        private String                            deptname;
        private String                            jobname;
        private String                            orgname;
        private String                            postname;
        private String                            psnname;
        private String                            type;
        private String                            typeName;
        private String                            userName;
        private List<NchrapplyWorkFlowBean>       nchrapplyWorkFlow;
        private List<NchrevectionApplyDetailBean> nchrevectionApplyDetail;

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

        public String getDeptname() {
            return deptname;
        }

        public void setDeptname(String deptname) {
            this.deptname = deptname;
        }

        public String getJobname() {
            return jobname;
        }

        public void setJobname(String jobname) {
            this.jobname = jobname;
        }

        public String getOrgname() {
            return orgname;
        }

        public void setOrgname(String orgname) {
            this.orgname = orgname;
        }

        public String getPostname() {
            return postname;
        }

        public void setPostname(String postname) {
            this.postname = postname;
        }

        public String getPsnname() {
            return psnname;
        }

        public void setPsnname(String psnname) {
            this.psnname = psnname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
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

        public List<NchrevectionApplyDetailBean> getNchrevectionApplyDetail() {
            return nchrevectionApplyDetail;
        }

        public void setNchrevectionApplyDetail(List<NchrevectionApplyDetailBean> nchrevectionApplyDetail) {
            this.nchrevectionApplyDetail = nchrevectionApplyDetail;
        }

        public static class NchrapplyWorkFlowBean {
            /**
             * approveResult : Y
             * approveResultCH : 批准
             * checkNote : Y
             * checkUserName : 庄振康
             * dealDate : 2017-06-14 14:12:26
             * isCheck : Y
             * isCheckCH : 已审批
             * sendDate : 2017-06-08 13:53:45
             * sendUserName : 储耀明
             */

            private String approveResult;
            private String approveResultCH;
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

        public static class NchrevectionApplyDetailBean {
            /**
             * endTime : 2017-06-08 17:30:00
             * evectionAddress : 上海
             * evectionRemark : 办公
             * handOverPepole : Y
             * psnname : Y
             * startTime : 2017-06-08 13:53:00
             * timeDifference : 4
             */

            private String endTime;
            private String evectionAddress;
            private String evectionRemark;
            private String handOverPepole;
            private String psnname;
            private String startTime;
            private String timeDifference;

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

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

            public String getHandOverPepole() {
                return handOverPepole;
            }

            public void setHandOverPepole(String handOverPepole) {
                this.handOverPepole = handOverPepole;
            }

            public String getPsnname() {
                return psnname;
            }

            public void setPsnname(String psnname) {
                this.psnname = psnname;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getTimeDifference() {
                return timeDifference;
            }

            public void setTimeDifference(String timeDifference) {
                this.timeDifference = timeDifference;
            }
        }
    }
}
