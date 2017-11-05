package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.util.List;

/**
 * {
 * "code": "000000",
 * "message": "查询成功",
 * "data": {
 * "userName": "赵赟",
 * "psnname": "赵贇",
 * "billCode": "XJ201705190748320861",
 * "orgname": "善林（上海）金融信息服务有限公司",
 * "deptname": "善贷宝",
 * "jobname": null,
 * "postname": "Android开发",
 * "applyDate": "2017-05-19",
 * "type": "1002Z710000000021ZM1",
 * "nchrfurloughApplyDetail": [
 * {
 * "startTime": "2017-05-31 09:00:00",
 * "endTime": "2017-06-01 17:30:00",
 * "timeDifference": "16",
 * "furloughRemark": "岳父60大寿",
 * "handOverPepole": "~",
 * "psnname": null
 * }
 * ],
 * "nchrapplyWorkFlow": [
 * {
 * "sendUserName": "赵赟",
 * "sendDate": "2017-05-19 07:49:51",
 * "checkUserName": "王涛",
 * "dealDate": "2017-05-23 00:00:45",
 * "approveResult": "Y",
 * "approveResultCH": "批准",
 * "isCheck": "Y",
 * "isCheckCH": "已审批",
 * "checkNote": "同意"
 * }
 * ]
 * }
 * }
 */
public class RestResultBean {

    /**
     * code : 000000
     * data : {"applyDate":"2017-11-05","billCode":"XJ20171105001806","deptname":"善贷宝","jobname":"000000","nchrapplyWorkFlow":[{"approveResult":"000000","approveResultCH":"000000","checkNote":"000000","checkUserName":"王涛","dealDate":"000000","isCheck":"N","isCheckCH":"未审批","sendDate":"2017-11-05 18:18:13","sendUserName":"周威"}],"nchrfurloughApplyDetail":[{"endTime":"2017-11-06 18:16:00","furloughRemark":"null","handOverPepole":"0001A11000000014R6MF","psnname":"王度宇","startTime":"2017-11-05 18:16:00","timeDifference":"1"}],"orgname":"善林（上海）金融信息服务有限公司","postname":"Java开发工程师","psnname":"周威","type":"1002Z710000000021ZLJ","userName":"周威"}
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
         * applyDate : 2017-11-05
         * billCode : XJ20171105001806
         * deptname : 善贷宝
         * jobname : 000000
         * nchrapplyWorkFlow : [{"approveResult":"000000","approveResultCH":"000000","checkNote":"000000","checkUserName":"王涛","dealDate":"000000","isCheck":"N","isCheckCH":"未审批","sendDate":"2017-11-05 18:18:13","sendUserName":"周威"}]
         * nchrfurloughApplyDetail : [{"endTime":"2017-11-06 18:16:00","furloughRemark":"null","handOverPepole":"0001A11000000014R6MF","psnname":"王度宇","startTime":"2017-11-05 18:16:00","timeDifference":"1"}]
         * orgname : 善林（上海）金融信息服务有限公司
         * postname : Java开发工程师
         * psnname : 周威
         * type : 1002Z710000000021ZLJ
         * userName : 周威
         */

        private String applyDate;
        private String                            billCode;
        private String                            deptname;
        private String                            jobname;
        private String                            orgname;
        private String                            postname;
        private String                            psnname;
        private String                            type;
        private String                            userName;
        private List<NchrapplyWorkFlowBean>       nchrapplyWorkFlow;
        private List<NchrfurloughApplyDetailBean> nchrfurloughApplyDetail;

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

        public List<NchrfurloughApplyDetailBean> getNchrfurloughApplyDetail() {
            return nchrfurloughApplyDetail;
        }

        public void setNchrfurloughApplyDetail(List<NchrfurloughApplyDetailBean> nchrfurloughApplyDetail) {
            this.nchrfurloughApplyDetail = nchrfurloughApplyDetail;
        }

        public static class NchrapplyWorkFlowBean {
            /**
             * approveResult : 000000
             * approveResultCH : 000000
             * checkNote : 000000
             * checkUserName : 王涛
             * dealDate : 000000
             * isCheck : N
             * isCheckCH : 未审批
             * sendDate : 2017-11-05 18:18:13
             * sendUserName : 周威
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

        public static class NchrfurloughApplyDetailBean {
            /**
             * endTime : 2017-11-06 18:16:00
             * furloughRemark : null
             * handOverPepole : 0001A11000000014R6MF
             * psnname : 王度宇
             * startTime : 2017-11-05 18:16:00
             * timeDifference : 1
             */

            private String endTime;
            private String furloughRemark;
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

            public String getFurloughRemark() {
                return furloughRemark;
            }

            public void setFurloughRemark(String furloughRemark) {
                this.furloughRemark = furloughRemark;
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
