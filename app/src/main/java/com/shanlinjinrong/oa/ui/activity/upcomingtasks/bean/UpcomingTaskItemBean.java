package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.util.List;

/**
 * Created by SaberrrC on 2017-10-26.
 {
 "code": 32501,
 "data": {
 "examinationList": [
 {
 "examination": {
 "date": "测试内容k8e6",
 "status": 51157,
 "type": 62244,
 "typeName": "测试内容ov17",
 "userId": 46134,
 "userName": "测试内容oy7q"
 }
 }
 ]
 },
 "msg": "测试内容1t1x"
 }
 */

public class UpcomingTaskItemBean {

    private int code;
    private DataBean data;
    private String   msg;
    private boolean isChecked = false;

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean checked) {
        this.isChecked = checked;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<ExaminationListBean> examinationList;

        public List<ExaminationListBean> getExaminationList() {
            return examinationList;
        }

        public void setExaminationList(List<ExaminationListBean> examinationList) {
            this.examinationList = examinationList;
        }

        public static class ExaminationListBean {

            private ExaminationBean examination;

            public ExaminationBean getExamination() {
                return examination;
            }

            public void setExamination(ExaminationBean examination) {
                this.examination = examination;
            }

            public static class ExaminationBean {

                private String date;
                private int    status;
                private int    type;
                private String typeName;
                private int    userId;
                private String userName;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getTypeName() {
                    return typeName;
                }

                public void setTypeName(String typeName) {
                    this.typeName = typeName;
                }

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
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
}