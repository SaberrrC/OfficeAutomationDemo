package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.util.List;

/**
 {
 "code": "000000",
 "message": "查询成功",
 "data": {
 "userName": "赵赟",
 "psnname": "赵贇",
 "billCode": "XJ201705190748320861",
 "orgname": "善林（上海）金融信息服务有限公司",
 "deptname": "善贷宝",
 "jobname": null,
 "postname": "Android开发",
 "applyDate": "2017-05-19",
 "type": "1002Z710000000021ZM1",
 "nchrfurloughApplyDetail": [
 {
 "startTime": "2017-05-31 09:00:00",
 "endTime": "2017-06-01 17:30:00",
 "timeDifference": "16",
 "furloughRemark": "岳父60大寿",
 "handOverPepole": "~",
 "psnname": null
 }
 ],
 "nchrapplyWorkFlow": [
 {
 "sendUserName": "赵赟",
 "sendDate": "2017-05-19 07:49:51",
 "checkUserName": "王涛",
 "dealDate": "2017-05-23 00:00:45",
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
public class RestResultBean {

    /**
     * code : 000000
     * message : 查询成功
     * data : {"userName":"赵赟","psnname":"赵贇","billCode":"XJ201705190748320861","orgname":"善林（上海）金融信息服务有限公司","deptname":"善贷宝","jobname":null,"postname":"Android开发","applyDate":"2017-05-19","type":"1002Z710000000021ZM1","nchrfurloughApplyDetail":[{"startTime":"2017-05-31 09:00:00","endTime":"2017-06-01 17:30:00","timeDifference":"16","furloughRemark":"岳父60大寿","handOverPepole":"~","psnname":null}],"nchrapplyWorkFlow":[{"sendUserName":"赵赟","sendDate":"2017-05-19 07:49:51","checkUserName":"王涛","dealDate":"2017-05-23 00:00:45","approveResult":"Y","approveResultCH":"批准","isCheck":"Y","isCheckCH":"已审批","checkNote":"同意"}]}
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
         * userName : 赵赟
         * psnname : 赵贇
         * billCode : XJ201705190748320861
         * orgname : 善林（上海）金融信息服务有限公司
         * deptname : 善贷宝
         * jobname : null
         * postname : Android开发
         * applyDate : 2017-05-19
         * type : 1002Z710000000021ZM1
         * nchrfurloughApplyDetail : [{"startTime":"2017-05-31 09:00:00","endTime":"2017-06-01 17:30:00","timeDifference":"16","furloughRemark":"岳父60大寿","handOverPepole":"~","psnname":null}]
         * nchrapplyWorkFlow : [{"sendUserName":"赵赟","sendDate":"2017-05-19 07:49:51","checkUserName":"王涛","dealDate":"2017-05-23 00:00:45","approveResult":"Y","approveResultCH":"批准","isCheck":"Y","isCheckCH":"已审批","checkNote":"同意"}]
         */

        private String userName;
        private String                            psnname;
        private String                            billCode;
        private String                            orgname;
        private String                            deptname;
        private String                            jobname;
        private String                            postname;
        private String                            applyDate;
        private String                            type;
        private List<NchrfurloughApplyDetailBean> nchrfurloughApplyDetail;
        private List<NchrapplyWorkFlowBean>       nchrapplyWorkFlow;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPsnname() {
            return psnname;
        }

        public void setPsnname(String psnname) {
            this.psnname = psnname;
        }

        public String getBillCode() {
            return billCode;
        }

        public void setBillCode(String billCode) {
            this.billCode = billCode;
        }

        public String getOrgname() {
            return orgname;
        }

        public void setOrgname(String orgname) {
            this.orgname = orgname;
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

        public String getPostname() {
            return postname;
        }

        public void setPostname(String postname) {
            this.postname = postname;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<NchrfurloughApplyDetailBean> getNchrfurloughApplyDetail() {
            return nchrfurloughApplyDetail;
        }

        public void setNchrfurloughApplyDetail(List<NchrfurloughApplyDetailBean> nchrfurloughApplyDetail) {
            this.nchrfurloughApplyDetail = nchrfurloughApplyDetail;
        }

        public List<NchrapplyWorkFlowBean> getNchrapplyWorkFlow() {
            return nchrapplyWorkFlow;
        }

        public void setNchrapplyWorkFlow(List<NchrapplyWorkFlowBean> nchrapplyWorkFlow) {
            this.nchrapplyWorkFlow = nchrapplyWorkFlow;
        }

        public static class NchrfurloughApplyDetailBean {
            /**
             * startTime : 2017-05-31 09:00:00
             * endTime : 2017-06-01 17:30:00
             * timeDifference : 16
             * furloughRemark : 岳父60大寿
             * handOverPepole : ~
             * psnname : null
             */

            private String startTime;
            private String endTime;
            private String timeDifference;
            private String furloughRemark;
            private String handOverPepole;
            private String psnname;

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getTimeDifference() {
                return timeDifference;
            }

            public void setTimeDifference(String timeDifference) {
                this.timeDifference = timeDifference;
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
        }

        public static class NchrapplyWorkFlowBean {
            /**
             * sendUserName : 赵赟
             * sendDate : 2017-05-19 07:49:51
             * checkUserName : 王涛
             * dealDate : 2017-05-23 00:00:45
             * approveResult : Y
             * approveResultCH : 批准
             * isCheck : Y
             * isCheckCH : 已审批
             * checkNote : 同意
             */

            private String sendUserName;
            private String sendDate;
            private String checkUserName;
            private String dealDate;
            private String approveResult;
            private String approveResultCH;
            private String isCheck;
            private String isCheckCH;
            private String checkNote;

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
