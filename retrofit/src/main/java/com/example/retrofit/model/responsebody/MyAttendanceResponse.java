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
    private String onebegintime;
    private String twoendtime;
    private String tbmstatus;
    private String signCause;

    public String getSignCause() {
        return signCause;
    }

    public void setSignCause(String signCause) {
        this.signCause = signCause;
    }

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

    public String getOnebegintime() {
        return onebegintime;
    }

    public void setOnebegintime(String onebegintime) {
        this.onebegintime = onebegintime;
    }

    public String getTwoendtime() {
        return twoendtime;
    }

    public void setTwoendtime(String twoendtime) {
        this.twoendtime = twoendtime;
    }

    public String getTbmstatus() {
        return tbmstatus;
    }

    public void setTbmstatus(String tbmstatus) {
        this.tbmstatus = tbmstatus;
    }
}
