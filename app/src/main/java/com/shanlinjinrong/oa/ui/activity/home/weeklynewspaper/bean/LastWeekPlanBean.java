package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean;

import java.util.List;

/**
 * Created by tonny on 2017/9/28.
 */

public class LastWeekPlanBean {


    /**
     * code : 000000
     * message : success
     * data : [{"nextWorkPlan":"就会更好好闺蜜。 ","personLiable":"空间吗 ","remark":"给客户给"},{"nextWorkPlan":"","personLiable":"","remark":""},{"nextWorkPlan":"","personLiable":"","remark":""},{"nextWorkPlan":"","personLiable":"","remark":""}]
     */

    private String code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * nextWorkPlan : 就会更好好闺蜜。
         * personLiable : 空间吗
         * remark : 给客户给
         */

        private String nextWorkPlan;
        private String personLiable;
        private String remark;

        public String getNextWorkPlan() {
            return nextWorkPlan;
        }

        public void setNextWorkPlan(String nextWorkPlan) {
            this.nextWorkPlan = nextWorkPlan;
        }

        public String getPersonLiable() {
            return personLiable;
        }

        public void setPersonLiable(String personLiable) {
            this.personLiable = personLiable;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
