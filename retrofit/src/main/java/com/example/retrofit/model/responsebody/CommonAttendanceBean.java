package com.example.retrofit.model.responsebody;

public class CommonAttendanceBean {

    private String psname;
    private String pk_psndoc;
    private String calendar;
    private String onebegintime;
    private String twoendtime;
    private String tbmstatus;
    private String importsignflag;
    private String flg;
    private String signCause;

    public String getPsname() {
        return psname;
    }

    public void setPsname(String psname) {
        this.psname = psname;
    }

    public String getPk_psndoc() {
        return pk_psndoc;
    }

    public void setPk_psndoc(String pk_psndoc) {
        this.pk_psndoc = pk_psndoc;
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

    public String getImportsignflag() {
        return importsignflag;
    }

    public void setImportsignflag(String importsignflag) {
        this.importsignflag = importsignflag;
    }

    public String getFlg() {
        return flg;
    }

    public void setFlg(String flg) {
        this.flg = flg;
    }

    public String getSignCause() {
        return signCause;
    }

    public void setSignCause(String signCause) {
        this.signCause = signCause;
    }
}
