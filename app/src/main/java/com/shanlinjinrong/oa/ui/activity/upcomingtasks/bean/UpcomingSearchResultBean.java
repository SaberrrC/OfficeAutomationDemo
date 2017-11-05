package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Auther: SaberrrC
 * @Email: saberrrc@163.com
 */
public class UpcomingSearchResultBean {

    /**
     * code : 000000
     * data : {"data":[{"billNo":"QK201706171004117167","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-17 10:04:32","userName":"邢朝华"},{"billNo":"QK201706200902047543","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-20 09:02:25","userName":"王曦"},{"billNo":"QK201706202151247764","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-20 21:53:15","userName":"刘文秀"},{"billNo":"QK201706221308228113","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-22 13:08:43","userName":"王宇"},{"billNo":"QK201706230931588298","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 09:32:29","userName":"唐露"},{"billNo":"QK201706231024458337","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 10:25:13","userName":"姜晔"},{"billNo":"QK201706231053518345","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 10:54:54","userName":"赵建浩"},{"billNo":"QK201706231713588439","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 17:14:29","userName":"王成"},{"billNo":"QK201706231730128446","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 17:31:08","userName":"周雅雯"},{"billNo":"QK201706241148008526","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-24 11:48:34","userName":"邢朝华"},{"billNo":"QK201706270936348977","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-27 09:37:29","userName":"徐小璐"},{"billNo":"QK201706270937328980","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-27 09:37:56","userName":"徐小璐"},{"billNo":"QK201706271336489069","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-27 13:37:21","userName":"郭晓伟"},{"billNo":"QK201706271609409161","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-27 16:10:05","userName":"陆卫佳"},{"billNo":"QK201706281649459468","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:50:26","userName":"张益"},{"billNo":"QK201706281653319474","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:54:32","userName":"刘玉乐"},{"billNo":"QK201706281655319479","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:55:51","userName":"周丁"},{"billNo":"QK201706281656139486","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:56:46","userName":"刘虎"},{"billNo":"QK201706281657109487","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:58:23","userName":"王娟"},{"billNo":"QK201706281700129495","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 17:00:37","userName":"熊亮"}],"pageNum":1,"pageSize":20,"total":199}
     * message : success
     */

    private String    code;
    private DataBeanX data;
    private String    message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBeanX implements Serializable {
        /**
         * data : [{"billNo":"QK201706171004117167","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-17 10:04:32","userName":"邢朝华"},{"billNo":"QK201706200902047543","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-20 09:02:25","userName":"王曦"},{"billNo":"QK201706202151247764","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-20 21:53:15","userName":"刘文秀"},{"billNo":"QK201706221308228113","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-22 13:08:43","userName":"王宇"},{"billNo":"QK201706230931588298","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 09:32:29","userName":"唐露"},{"billNo":"QK201706231024458337","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 10:25:13","userName":"姜晔"},{"billNo":"QK201706231053518345","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 10:54:54","userName":"赵建浩"},{"billNo":"QK201706231713588439","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 17:14:29","userName":"王成"},{"billNo":"QK201706231730128446","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-23 17:31:08","userName":"周雅雯"},{"billNo":"QK201706241148008526","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-24 11:48:34","userName":"邢朝华"},{"billNo":"QK201706270936348977","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-27 09:37:29","userName":"徐小璐"},{"billNo":"QK201706270937328980","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-27 09:37:56","userName":"徐小璐"},{"billNo":"QK201706271336489069","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-27 13:37:21","userName":"郭晓伟"},{"billNo":"QK201706271609409161","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-27 16:10:05","userName":"陆卫佳"},{"billNo":"QK201706281649459468","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:50:26","userName":"张益"},{"billNo":"QK201706281653319474","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:54:32","userName":"刘玉乐"},{"billNo":"QK201706281655319479","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:55:51","userName":"周丁"},{"billNo":"QK201706281656139486","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:56:46","userName":"刘虎"},{"billNo":"QK201706281657109487","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 16:58:23","userName":"王娟"},{"billNo":"QK201706281700129495","billTypeName":"签卡申请","deptName":"善林财富","isCheck":"未审批","orgName":"善林（上海）金融信息服务有限公司","pkBillType":"6402","sendDate":"06-28 17:00:37","userName":"熊亮"}]
         * pageNum : 1
         * pageSize : 20
         * total : 199
         */

        private int            pageNum;
        private int            pageSize;
        private int            total;
        private List<DataBean> data;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {
            /**
             * billNo : QK201706171004117167
             * billTypeName : 签卡申请
             * deptName : 善林财富
             * isCheck : 未审批
             * orgName : 善林（上海）金融信息服务有限公司
             * pkBillType : 6402
             * sendDate : 06-17 10:04:32
             * userName : 邢朝华
             */

            private String billNo;
            private String billTypeName;
            private String deptName;
            private String isCheck;
            private String orgName;
            private String pkBillType;
            private String sendDate;
            private String userName;
            public boolean isChecked = false;

            public boolean getIsChecked() {
                return isChecked;
            }

            public void setIsChecked(boolean checked) {
                isChecked = checked;
            }

            public String getBillNo() {
                return billNo;
            }

            public void setBillNo(String billNo) {
                this.billNo = billNo;
            }

            public String getBillTypeName() {
                return billTypeName;
            }

            public void setBillTypeName(String billTypeName) {
                this.billTypeName = billTypeName;
            }

            public String getDeptName() {
                return deptName;
            }

            public void setDeptName(String deptName) {
                this.deptName = deptName;
            }

            public String getIsCheck() {
                return isCheck;
            }

            public void setIsCheck(String isCheck) {
                this.isCheck = isCheck;
            }

            public String getOrgName() {
                return orgName;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }

            public String getPkBillType() {
                return pkBillType;
            }

            public void setPkBillType(String pkBillType) {
                this.pkBillType = pkBillType;
            }

            public String getSendDate() {
                return sendDate;
            }

            public void setSendDate(String sendDate) {
                this.sendDate = sendDate;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}
