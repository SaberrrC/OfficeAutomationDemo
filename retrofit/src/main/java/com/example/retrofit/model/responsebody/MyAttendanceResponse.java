package com.example.retrofit.model.responsebody;

//考勤日统计
public class MyAttendanceResponse {

    /**
     * psname : 荣令山
     * pkPsndoc : 0001A31000000010WWJZ
     * calendar : 2017-11-06
     * onebegintime : null
     * twoendtime : null
     * tbmstatus : [旷工]
     */

    private String psname;
    private String pkPsndoc;
    private String calendar;
    private Object onebegintime;
    private Object twoendtime;
    private String tbmstatus;

    public String getPsname() {
        return psname;
    }

    public void setPsname(String psname) {
        this.psname = psname;
    }

    public String getPkPsndoc() {
        return pkPsndoc;
    }

    public void setPkPsndoc(String pkPsndoc) {
        this.pkPsndoc = pkPsndoc;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public Object getOnebegintime() {
        return onebegintime;
    }

    public void setOnebegintime(Object onebegintime) {
        this.onebegintime = onebegintime;
    }

    public Object getTwoendtime() {
        return twoendtime;
    }

    public void setTwoendtime(Object twoendtime) {
        this.twoendtime = twoendtime;
    }

    public String getTbmstatus() {
        return tbmstatus;
    }

    public void setTbmstatus(String tbmstatus) {
        this.tbmstatus = tbmstatus;
    }
}
