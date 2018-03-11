package com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean;

import java.util.List;

/**
 * 作者：王凤旭
 * 创建时间：2018/3/8
 * 功能描述：
 */

public class OfficeSuppliesListBean {


    /**
     * code : 000000
     * message : success
     * data : {"pageSize":"10","total":"0","list":[{"typeName":"办公用品申请","globalStatus":"3","startTime":"2018-03-08 10:44:54","id":"187748"},{"typeName":"办公用品申请","globalStatus":"3","startTime":"2018-03-08 11:04:43","id":"187806"},{"typeName":"办公用品申请","globalStatus":"3","startTime":"2018-03-08 11:10:37","id":"187828"},{"typeName":"办公用品申请","globalStatus":"3","startTime":"2018-03-08 11:19:41","id":"187852"}],"pageNum":"0"}
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
         * pageSize : 10
         * total : 0
         * list : [{"typeName":"办公用品申请","globalStatus":"3","startTime":"2018-03-08 10:44:54","id":"187748"},{"typeName":"办公用品申请","globalStatus":"3","startTime":"2018-03-08 11:04:43","id":"187806"},{"typeName":"办公用品申请","globalStatus":"3","startTime":"2018-03-08 11:10:37","id":"187828"},{"typeName":"办公用品申请","globalStatus":"3","startTime":"2018-03-08 11:19:41","id":"187852"}]
         * pageNum : 0
         */

        private String         pageSize;
        private String         total;
        private String         pageNum;
        private List<ListBean> list;

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPageNum() {
            return pageNum;
        }

        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * typeName : 办公用品申请
             * globalStatus : 3
             * startTime : 2018-03-08 10:44:54
             * id : 187748
             */

            private String typeName;
            private String globalStatus;
            private String startTime;
            private String id;
            private String startedBy;
            private String taskId;
            private int    totalCount;
            private String processInstanceId;
            private String processDefinitionName;

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getGlobalStatus() {
                return globalStatus;
            }

            public void setGlobalStatus(String globalStatus) {
                this.globalStatus = globalStatus;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getStartedBy() {
                return startedBy;
            }

            public void setStartedBy(String startedBy) {
                this.startedBy = startedBy;
            }

            public String getTaskId() {
                return taskId;
            }

            public void setTaskId(String taskId) {
                this.taskId = taskId;
            }

            public int getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(int totalCount) {
                this.totalCount = totalCount;
            }

            public String getProcessInstanceId() {
                return processInstanceId;
            }

            public void setProcessInstanceId(String processInstanceId) {
                this.processInstanceId = processInstanceId;
            }

            public String getProcessDefinitionName() {
                return processDefinitionName;
            }

            public void setProcessDefinitionName(String processDefinitionName) {
                this.processDefinitionName = processDefinitionName;
            }
        }
    }
}