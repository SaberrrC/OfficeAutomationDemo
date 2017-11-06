package com.example.retrofit.model.responsebody;

import java.util.List;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

public class MyAttandanceResponse {


    /**
     * allWorkAttendanceList : [{"calendar":"string","flg":"string","importsignflag":"string","onebegintime":"string","pk_psndoc":"string","psname":"string","signCause":"string","tbmstatus":"string","twoendtime":"string"}]
     * billCount : string
     * ccCount : string
     * ccWorkAttendanceList : [{"calendar":"string","flg":"string","importsignflag":"string","onebegintime":"string","pk_psndoc":"string","psname":"string","signCause":"string","tbmstatus":"string","twoendtime":"string"}]
     * cdCount : string
     * cdWorkAttendanceList : [{"calendar":"string","flg":"string","importsignflag":"string","onebegintime":"string","pk_psndoc":"string","psname":"string","signCause":"string","tbmstatus":"string","twoendtime":"string"}]
     * kgCount : string
     * kgWorkAttendanceList : [{"calendar":"string","flg":"string","importsignflag":"string","onebegintime":"string","pk_psndoc":"string","psname":"string","signCause":"string","tbmstatus":"string","twoendtime":"string"}]
     * ztCount : string
     * ztWorkAttendanceList : [{"calendar":"string","flg":"string","importsignflag":"string","onebegintime":"string","pk_psndoc":"string","psname":"string","signCause":"string","tbmstatus":"string","twoendtime":"string"}]
     */

    private String billCount;
    private String ccCount;
    private String cdCount;
    private String kgCount;
    private String ztCount;
    private List<AllWorkAttendanceListBean> allWorkAttendanceList;
    private List<CcWorkAttendanceListBean> ccWorkAttendanceList;
    private List<CdWorkAttendanceListBean> cdWorkAttendanceList;
    private List<KgWorkAttendanceListBean> kgWorkAttendanceList;
    private List<ZtWorkAttendanceListBean> ztWorkAttendanceList;

    public String getBillCount() {
        return billCount;
    }

    public void setBillCount(String billCount) {
        this.billCount = billCount;
    }

    public String getCcCount() {
        return ccCount;
    }

    public void setCcCount(String ccCount) {
        this.ccCount = ccCount;
    }

    public String getCdCount() {
        return cdCount;
    }

    public void setCdCount(String cdCount) {
        this.cdCount = cdCount;
    }

    public String getKgCount() {
        return kgCount;
    }

    public void setKgCount(String kgCount) {
        this.kgCount = kgCount;
    }

    public String getZtCount() {
        return ztCount;
    }

    public void setZtCount(String ztCount) {
        this.ztCount = ztCount;
    }

    public List<AllWorkAttendanceListBean> getAllWorkAttendanceList() {
        return allWorkAttendanceList;
    }

    public void setAllWorkAttendanceList(List<AllWorkAttendanceListBean> allWorkAttendanceList) {
        this.allWorkAttendanceList = allWorkAttendanceList;
    }

    public List<CcWorkAttendanceListBean> getCcWorkAttendanceList() {
        return ccWorkAttendanceList;
    }

    public void setCcWorkAttendanceList(List<CcWorkAttendanceListBean> ccWorkAttendanceList) {
        this.ccWorkAttendanceList = ccWorkAttendanceList;
    }

    public List<CdWorkAttendanceListBean> getCdWorkAttendanceList() {
        return cdWorkAttendanceList;
    }

    public void setCdWorkAttendanceList(List<CdWorkAttendanceListBean> cdWorkAttendanceList) {
        this.cdWorkAttendanceList = cdWorkAttendanceList;
    }

    public List<KgWorkAttendanceListBean> getKgWorkAttendanceList() {
        return kgWorkAttendanceList;
    }

    public void setKgWorkAttendanceList(List<KgWorkAttendanceListBean> kgWorkAttendanceList) {
        this.kgWorkAttendanceList = kgWorkAttendanceList;
    }

    public List<ZtWorkAttendanceListBean> getZtWorkAttendanceList() {
        return ztWorkAttendanceList;
    }

    public void setZtWorkAttendanceList(List<ZtWorkAttendanceListBean> ztWorkAttendanceList) {
        this.ztWorkAttendanceList = ztWorkAttendanceList;
    }

    public static class AllWorkAttendanceListBean {
        /**
         * calendar : string
         * flg : string
         * importsignflag : string
         * onebegintime : string
         * pk_psndoc : string
         * psname : string
         * signCause : string
         * tbmstatus : string
         * twoendtime : string
         */

        private String calendar;
        private String flg;
        private String importsignflag;
        private String onebegintime;
        private String pk_psndoc;
        private String psname;
        private String signCause;
        private String tbmstatus;
        private String twoendtime;

        public String getCalendar() {
            return calendar;
        }

        public void setCalendar(String calendar) {
            this.calendar = calendar;
        }

        public String getFlg() {
            return flg;
        }

        public void setFlg(String flg) {
            this.flg = flg;
        }

        public String getImportsignflag() {
            return importsignflag;
        }

        public void setImportsignflag(String importsignflag) {
            this.importsignflag = importsignflag;
        }

        public String getOnebegintime() {
            return onebegintime;
        }

        public void setOnebegintime(String onebegintime) {
            this.onebegintime = onebegintime;
        }

        public String getPk_psndoc() {
            return pk_psndoc;
        }

        public void setPk_psndoc(String pk_psndoc) {
            this.pk_psndoc = pk_psndoc;
        }

        public String getPsname() {
            return psname;
        }

        public void setPsname(String psname) {
            this.psname = psname;
        }

        public String getSignCause() {
            return signCause;
        }

        public void setSignCause(String signCause) {
            this.signCause = signCause;
        }

        public String getTbmstatus() {
            return tbmstatus;
        }

        public void setTbmstatus(String tbmstatus) {
            this.tbmstatus = tbmstatus;
        }

        public String getTwoendtime() {
            return twoendtime;
        }

        public void setTwoendtime(String twoendtime) {
            this.twoendtime = twoendtime;
        }
    }

    public static class CcWorkAttendanceListBean {
        /**
         * calendar : string
         * flg : string
         * importsignflag : string
         * onebegintime : string
         * pk_psndoc : string
         * psname : string
         * signCause : string
         * tbmstatus : string
         * twoendtime : string
         */

        private String calendar;
        private String flg;
        private String importsignflag;
        private String onebegintime;
        private String pk_psndoc;
        private String psname;
        private String signCause;
        private String tbmstatus;
        private String twoendtime;

        public String getCalendar() {
            return calendar;
        }

        public void setCalendar(String calendar) {
            this.calendar = calendar;
        }

        public String getFlg() {
            return flg;
        }

        public void setFlg(String flg) {
            this.flg = flg;
        }

        public String getImportsignflag() {
            return importsignflag;
        }

        public void setImportsignflag(String importsignflag) {
            this.importsignflag = importsignflag;
        }

        public String getOnebegintime() {
            return onebegintime;
        }

        public void setOnebegintime(String onebegintime) {
            this.onebegintime = onebegintime;
        }

        public String getPk_psndoc() {
            return pk_psndoc;
        }

        public void setPk_psndoc(String pk_psndoc) {
            this.pk_psndoc = pk_psndoc;
        }

        public String getPsname() {
            return psname;
        }

        public void setPsname(String psname) {
            this.psname = psname;
        }

        public String getSignCause() {
            return signCause;
        }

        public void setSignCause(String signCause) {
            this.signCause = signCause;
        }

        public String getTbmstatus() {
            return tbmstatus;
        }

        public void setTbmstatus(String tbmstatus) {
            this.tbmstatus = tbmstatus;
        }

        public String getTwoendtime() {
            return twoendtime;
        }

        public void setTwoendtime(String twoendtime) {
            this.twoendtime = twoendtime;
        }
    }

    public static class CdWorkAttendanceListBean {
        /**
         * calendar : string
         * flg : string
         * importsignflag : string
         * onebegintime : string
         * pk_psndoc : string
         * psname : string
         * signCause : string
         * tbmstatus : string
         * twoendtime : string
         */

        private String calendar;
        private String flg;
        private String importsignflag;
        private String onebegintime;
        private String pk_psndoc;
        private String psname;
        private String signCause;
        private String tbmstatus;
        private String twoendtime;

        public String getCalendar() {
            return calendar;
        }

        public void setCalendar(String calendar) {
            this.calendar = calendar;
        }

        public String getFlg() {
            return flg;
        }

        public void setFlg(String flg) {
            this.flg = flg;
        }

        public String getImportsignflag() {
            return importsignflag;
        }

        public void setImportsignflag(String importsignflag) {
            this.importsignflag = importsignflag;
        }

        public String getOnebegintime() {
            return onebegintime;
        }

        public void setOnebegintime(String onebegintime) {
            this.onebegintime = onebegintime;
        }

        public String getPk_psndoc() {
            return pk_psndoc;
        }

        public void setPk_psndoc(String pk_psndoc) {
            this.pk_psndoc = pk_psndoc;
        }

        public String getPsname() {
            return psname;
        }

        public void setPsname(String psname) {
            this.psname = psname;
        }

        public String getSignCause() {
            return signCause;
        }

        public void setSignCause(String signCause) {
            this.signCause = signCause;
        }

        public String getTbmstatus() {
            return tbmstatus;
        }

        public void setTbmstatus(String tbmstatus) {
            this.tbmstatus = tbmstatus;
        }

        public String getTwoendtime() {
            return twoendtime;
        }

        public void setTwoendtime(String twoendtime) {
            this.twoendtime = twoendtime;
        }
    }

    public static class KgWorkAttendanceListBean {
        /**
         * calendar : string
         * flg : string
         * importsignflag : string
         * onebegintime : string
         * pk_psndoc : string
         * psname : string
         * signCause : string
         * tbmstatus : string
         * twoendtime : string
         */

        private String calendar;
        private String flg;
        private String importsignflag;
        private String onebegintime;
        private String pk_psndoc;
        private String psname;
        private String signCause;
        private String tbmstatus;
        private String twoendtime;

        public String getCalendar() {
            return calendar;
        }

        public void setCalendar(String calendar) {
            this.calendar = calendar;
        }

        public String getFlg() {
            return flg;
        }

        public void setFlg(String flg) {
            this.flg = flg;
        }

        public String getImportsignflag() {
            return importsignflag;
        }

        public void setImportsignflag(String importsignflag) {
            this.importsignflag = importsignflag;
        }

        public String getOnebegintime() {
            return onebegintime;
        }

        public void setOnebegintime(String onebegintime) {
            this.onebegintime = onebegintime;
        }

        public String getPk_psndoc() {
            return pk_psndoc;
        }

        public void setPk_psndoc(String pk_psndoc) {
            this.pk_psndoc = pk_psndoc;
        }

        public String getPsname() {
            return psname;
        }

        public void setPsname(String psname) {
            this.psname = psname;
        }

        public String getSignCause() {
            return signCause;
        }

        public void setSignCause(String signCause) {
            this.signCause = signCause;
        }

        public String getTbmstatus() {
            return tbmstatus;
        }

        public void setTbmstatus(String tbmstatus) {
            this.tbmstatus = tbmstatus;
        }

        public String getTwoendtime() {
            return twoendtime;
        }

        public void setTwoendtime(String twoendtime) {
            this.twoendtime = twoendtime;
        }
    }

    public static class ZtWorkAttendanceListBean {
        /**
         * calendar : string
         * flg : string
         * importsignflag : string
         * onebegintime : string
         * pk_psndoc : string
         * psname : string
         * signCause : string
         * tbmstatus : string
         * twoendtime : string
         */

        private String calendar;
        private String flg;
        private String importsignflag;
        private String onebegintime;
        private String pk_psndoc;
        private String psname;
        private String signCause;
        private String tbmstatus;
        private String twoendtime;

        public String getCalendar() {
            return calendar;
        }

        public void setCalendar(String calendar) {
            this.calendar = calendar;
        }

        public String getFlg() {
            return flg;
        }

        public void setFlg(String flg) {
            this.flg = flg;
        }

        public String getImportsignflag() {
            return importsignflag;
        }

        public void setImportsignflag(String importsignflag) {
            this.importsignflag = importsignflag;
        }

        public String getOnebegintime() {
            return onebegintime;
        }

        public void setOnebegintime(String onebegintime) {
            this.onebegintime = onebegintime;
        }

        public String getPk_psndoc() {
            return pk_psndoc;
        }

        public void setPk_psndoc(String pk_psndoc) {
            this.pk_psndoc = pk_psndoc;
        }

        public String getPsname() {
            return psname;
        }

        public void setPsname(String psname) {
            this.psname = psname;
        }

        public String getSignCause() {
            return signCause;
        }

        public void setSignCause(String signCause) {
            this.signCause = signCause;
        }

        public String getTbmstatus() {
            return tbmstatus;
        }

        public void setTbmstatus(String tbmstatus) {
            this.tbmstatus = tbmstatus;
        }

        public String getTwoendtime() {
            return twoendtime;
        }

        public void setTwoendtime(String twoendtime) {
            this.twoendtime = twoendtime;
        }
    }


}
