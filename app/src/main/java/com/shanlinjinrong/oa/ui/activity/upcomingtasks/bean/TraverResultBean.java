package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.util.List;

/**
 {
 "code": "000000",
 "message": "查询成功",
 "data": {
 "userName": "许贞",
 "psnname": "许贞",
 "billCode": "CC20171107005775",
 "orgname": "善林（上海）金融信息服务有限公司",
 "deptname": "公关部",
 "jobname": "",
 "postname": "媒介副经理",
 "applyDate": "2017-11-07",
 "type": "1001A1100000000154IR",
 "typeName": "出差",
 "nchrapplyWorkFlow": [
 {
 "sendUserName": "许贞",
 "sendDate": "2017-11-07 14:44:01",
 "checkUserName": "李煜",
 "dealDate": "2017-11-07 16:24:04",
 "approveResult": "Y",
 "approveResultCH": "批准",
 "isCheck": "Y",
 "isCheckCH": "已审批",
 "checkNote": ""
 }
 ],
 "nchrevectionApplyDetail": [
 {
 "evectionAddress": "赋给",
 "evectionRemark": "发生过",
 "startTime": "2017-08-28 14:43:10",
 "endTime": "2017-09-01 14:43:15",
 "timeDifference": "4",
 "handOverPepole": "0001A11000000014GFGB",
 "psnname": "姚瑶"
 }
 ]
 }
 }
 */
public class TraverResultBean {

    /**
     * code : 000000
     * message : 查询成功
     * data : {"userName":"许贞","psnname":"许贞","billCode":"CC20171107005775","orgname":"善林（上海）金融信息服务有限公司","deptname":"公关部","jobname":"","postname":"媒介副经理","applyDate":"2017-11-07","type":"1001A1100000000154IR","typeName":"出差","nchrapplyWorkFlow":[{"sendUserName":"许贞","sendDate":"2017-11-07 14:44:01","checkUserName":"李煜","dealDate":"2017-11-07 16:24:04","approveResult":"Y","approveResultCH":"批准","isCheck":"Y","isCheckCH":"已审批","checkNote":""}],"nchrevectionApplyDetail":[{"evectionAddress":"赋给","evectionRemark":"发生过","startTime":"2017-08-28 14:43:10","endTime":"2017-09-01 14:43:15","timeDifference":"4","handOverPepole":"0001A11000000014GFGB","psnname":"姚瑶"}]}
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
         * userName : 许贞
         * psnname : 许贞
         * billCode : CC20171107005775
         * orgname : 善林（上海）金融信息服务有限公司
         * deptname : 公关部
         * jobname :
         * postname : 媒介副经理
         * applyDate : 2017-11-07
         * type : 1001A1100000000154IR
         * typeName : 出差
         * nchrapplyWorkFlow : [{"sendUserName":"许贞","sendDate":"2017-11-07 14:44:01","checkUserName":"李煜","dealDate":"2017-11-07 16:24:04","approveResult":"Y","approveResultCH":"批准","isCheck":"Y","isCheckCH":"已审批","checkNote":""}]
         * nchrevectionApplyDetail : [{"evectionAddress":"赋给","evectionRemark":"发生过","startTime":"2017-08-28 14:43:10","endTime":"2017-09-01 14:43:15","timeDifference":"4","handOverPepole":"0001A11000000014GFGB","psnname":"姚瑶"}]
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
        private String                            typeName;
        private List<NchrapplyWorkFlowBean>       nchrapplyWorkFlow;
        private List<NchrevectionApplyDetailBean> nchrevectionApplyDetail;

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

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
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
             * sendUserName : 许贞
             * sendDate : 2017-11-07 14:44:01
             * checkUserName : 李煜
             * dealDate : 2017-11-07 16:24:04
             * approveResult : Y
             * approveResultCH : 批准
             * isCheck : Y
             * isCheckCH : 已审批
             * checkNote :
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

        public static class NchrevectionApplyDetailBean {
            /**
             * evectionAddress : 赋给
             * evectionRemark : 发生过
             * startTime : 2017-08-28 14:43:10
             * endTime : 2017-09-01 14:43:15
             * timeDifference : 4
             * handOverPepole : 0001A11000000014GFGB
             * psnname : 姚瑶
             */

            private String evectionAddress;
            private String evectionRemark;
            private String startTime;
            private String endTime;
            private String timeDifference;
            private String handOverPepole;
            private String psnname;

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
    }
}
