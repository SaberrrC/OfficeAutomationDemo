package com.example.retrofit.model.responsebody;

import java.io.Serializable;
import java.util.List;

public class MyAttandanceResponse implements Serializable{

    /**
     * allWorkAttendanceList : [{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-06","onebegintime":"2017-09-06 10:00:00","twoendtime":"2017-09-06 17:00:00","tbmstatus":"[迟到]","importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-07","onebegintime":"2017-09-07 08:00:00","twoendtime":"2017-09-07 15:00:00","tbmstatus":"[早退]","importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-08","onebegintime":"2017-09-08 08:00:00","twoendtime":"2017-09-08 17:00:00","tbmstatus":"[出差]","importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-11","onebegintime":"2017-09-11 08:00:00","twoendtime":"2017-09-07 21:00:00","tbmstatus":"[转调休加班]","importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-12","onebegintime":"2017-09-12 08:00:00","twoendtime":"2017-09-12 17:00:00","tbmstatus":null,"importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-13","onebegintime":"2017-09-13 08:00:00","twoendtime":"2017-09-13 17:00:00","tbmstatus":null,"importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-14","onebegintime":"2017-09-14 08:00:00","twoendtime":"2017-09-14 17:00:00","tbmstatus":null,"importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-15","onebegintime":"2017-09-15 08:00:00","twoendtime":"2017-09-15 17:00:00","tbmstatus":null,"importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-18","onebegintime":"2017-09-18 08:00:00","twoendtime":"2017-09-18 17:00:00","tbmstatus":null,"importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-19","onebegintime":"2017-09-19 08:00:00","twoendtime":"2017-09-19 17:00:00","tbmstatus":null,"importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-20","onebegintime":"2017-09-20 08:00:00","twoendtime":"2017-09-20 17:00:00","tbmstatus":null,"importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-21","onebegintime":"2017-09-21 08:00:00","twoendtime":"2017-09-21 17:00:00","tbmstatus":"[加班转调休]","importsignflag":"9","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-22","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-23","onebegintime":null,"twoendtime":null,"tbmstatus":null,"importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-24","onebegintime":null,"twoendtime":null,"tbmstatus":null,"importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-25","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-26","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-27","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-28","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-29","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-30","onebegintime":null,"twoendtime":null,"tbmstatus":null,"importsignflag":"0","flg":null,"signCause":null}]
     * ccWorkAttendanceList : []
     * cdWorkAttendanceList : [{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-06","onebegintime":"2017-09-06 10:00:00","twoendtime":"2017-09-06 17:00:00","tbmstatus":"[迟到]","importsignflag":"9","flg":null,"signCause":null}]
     * kgWorkAttendanceList : [{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-22","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-25","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-26","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-27","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-28","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-29","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null}]
     * ztWorkAttendanceList : [{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-07","onebegintime":"2017-09-07 08:00:00","twoendtime":"2017-09-07 15:00:00","tbmstatus":"[早退]","importsignflag":"9","flg":null,"signCause":null}]
     * jbWorkAttendanceList : [{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-11","onebegintime":"2017-09-11 08:00:00","twoendtime":"2017-09-07 21:00:00","tbmstatus":"[转调休加班]","importsignflag":"9","flg":null,"signCause":null}]
     * xjWorkAttendanceList : [{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-21","onebegintime":"2017-09-21 08:00:00","twoendtime":"2017-09-21 17:00:00","tbmstatus":"[加班转调休]","importsignflag":"9","flg":null,"signCause":null}]
     * ycWorkAttendanceList : [[{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-06","onebegintime":"2017-09-06 10:00:00","twoendtime":"2017-09-06 17:00:00","tbmstatus":"[迟到]","importsignflag":"9","flg":null,"signCause":null}],[{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-22","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-25","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-26","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-27","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-28","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null},{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-29","onebegintime":null,"twoendtime":null,"tbmstatus":"[旷工]","importsignflag":"0","flg":null,"signCause":null}],[{"psname":"荣令山","pk_psndoc":"0001A31000000010WWJZ","calendar":"2017-09-07","onebegintime":"2017-09-07 08:00:00","twoendtime":"2017-09-07 15:00:00","tbmstatus":"[早退]","importsignflag":"9","flg":null,"signCause":null}]]
     * billCount : 0
     * ccCount : 0
     * kgCount : 6
     * cdCount : 1
     * ztCount : 1
     * jbCount : 1
     * xjCount : 1
     */

    private String billCount;
    private String ccCount;
    private String kgCount;
    private String cdCount;
    private String ztCount;
    private String jbCount;
    private String xjCount;
    private List<AllWorkAttendanceListBean> allWorkAttendanceList;
    private List<CcWorkAttendanceListBean> ccWorkAttendanceList;
    private List<CdWorkAttendanceListBean> cdWorkAttendanceList;
    private List<KgWorkAttendanceListBean> kgWorkAttendanceList;
    private List<ZtWorkAttendanceListBean> ztWorkAttendanceList;
    private List<JbWorkAttendanceListBean> jbWorkAttendanceList;
    private List<XjWorkAttendanceListBean> xjWorkAttendanceList;
    private List<List<YcWorkAttendanceListBean>> ycWorkAttendanceList;

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

    public String getKgCount() {
        return kgCount;
    }

    public void setKgCount(String kgCount) {
        this.kgCount = kgCount;
    }

    public String getCdCount() {
        return cdCount;
    }

    public void setCdCount(String cdCount) {
        this.cdCount = cdCount;
    }

    public String getZtCount() {
        return ztCount;
    }

    public void setZtCount(String ztCount) {
        this.ztCount = ztCount;
    }

    public String getJbCount() {
        return jbCount;
    }

    public void setJbCount(String jbCount) {
        this.jbCount = jbCount;
    }

    public String getXjCount() {
        return xjCount;
    }

    public void setXjCount(String xjCount) {
        this.xjCount = xjCount;
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

    public List<JbWorkAttendanceListBean> getJbWorkAttendanceList() {
        return jbWorkAttendanceList;
    }

    public void setJbWorkAttendanceList(List<JbWorkAttendanceListBean> jbWorkAttendanceList) {
        this.jbWorkAttendanceList = jbWorkAttendanceList;
    }

    public List<XjWorkAttendanceListBean> getXjWorkAttendanceList() {
        return xjWorkAttendanceList;
    }

    public void setXjWorkAttendanceList(List<XjWorkAttendanceListBean> xjWorkAttendanceList) {
        this.xjWorkAttendanceList = xjWorkAttendanceList;
    }

    public List<List<YcWorkAttendanceListBean>> getYcWorkAttendanceList() {
        return ycWorkAttendanceList;
    }

    public void setYcWorkAttendanceList(List<List<YcWorkAttendanceListBean>> ycWorkAttendanceList) {
        this.ycWorkAttendanceList = ycWorkAttendanceList;
    }

    public static class CcWorkAttendanceListBean{
        /**
         * psname : 荣令山
         * pk_psndoc : 0001A31000000010WWJZ
         * calendar : 2017-09-06
         * onebegintime : 2017-09-06 10:00:00
         * twoendtime : 2017-09-06 17:00:00
         * tbmstatus : [迟到]
         * importsignflag : 9
         * flg : null
         * signCause : null
         */

        private String psname;
        private String pk_psndoc;
        private String calendar;
        private String onebegintime;
        private String twoendtime;
        private String tbmstatus;
        private String importsignflag;
        private Object flg;
        private Object signCause;

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

        public Object getFlg() {
            return flg;
        }

        public void setFlg(Object flg) {
            this.flg = flg;
        }

        public Object getSignCause() {
            return signCause;
        }

        public void setSignCause(Object signCause) {
            this.signCause = signCause;
        }
    }

    public static class AllWorkAttendanceListBean {
        /**
         * psname : 荣令山
         * pk_psndoc : 0001A31000000010WWJZ
         * calendar : 2017-09-06
         * onebegintime : 2017-09-06 10:00:00
         * twoendtime : 2017-09-06 17:00:00
         * tbmstatus : [迟到]
         * importsignflag : 9
         * flg : null
         * signCause : null
         */

        private String psname;
        private String pk_psndoc;
        private String calendar;
        private String onebegintime;
        private String twoendtime;
        private String tbmstatus;
        private String importsignflag;
        private Object flg;
        private Object signCause;

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

        public Object getFlg() {
            return flg;
        }

        public void setFlg(Object flg) {
            this.flg = flg;
        }

        public Object getSignCause() {
            return signCause;
        }

        public void setSignCause(Object signCause) {
            this.signCause = signCause;
        }
    }

    public static class CdWorkAttendanceListBean {
        /**
         * psname : 荣令山
         * pk_psndoc : 0001A31000000010WWJZ
         * calendar : 2017-09-06
         * onebegintime : 2017-09-06 10:00:00
         * twoendtime : 2017-09-06 17:00:00
         * tbmstatus : [迟到]
         * importsignflag : 9
         * flg : null
         * signCause : null
         */

        private String psname;
        private String pk_psndoc;
        private String calendar;
        private String onebegintime;
        private String twoendtime;
        private String tbmstatus;
        private String importsignflag;
        private Object flg;
        private Object signCause;

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

        public Object getFlg() {
            return flg;
        }

        public void setFlg(Object flg) {
            this.flg = flg;
        }

        public Object getSignCause() {
            return signCause;
        }

        public void setSignCause(Object signCause) {
            this.signCause = signCause;
        }
    }

    public static class KgWorkAttendanceListBean {
        /**
         * psname : 荣令山
         * pk_psndoc : 0001A31000000010WWJZ
         * calendar : 2017-09-22
         * onebegintime : null
         * twoendtime : null
         * tbmstatus : [旷工]
         * importsignflag : 0
         * flg : null
         * signCause : null
         */

        private String psname;
        private String pk_psndoc;
        private String calendar;
        private Object onebegintime;
        private Object twoendtime;
        private String tbmstatus;
        private String importsignflag;
        private Object flg;
        private Object signCause;

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

        public String getImportsignflag() {
            return importsignflag;
        }

        public void setImportsignflag(String importsignflag) {
            this.importsignflag = importsignflag;
        }

        public Object getFlg() {
            return flg;
        }

        public void setFlg(Object flg) {
            this.flg = flg;
        }

        public Object getSignCause() {
            return signCause;
        }

        public void setSignCause(Object signCause) {
            this.signCause = signCause;
        }
    }

    public static class ZtWorkAttendanceListBean {
        /**
         * psname : 荣令山
         * pk_psndoc : 0001A31000000010WWJZ
         * calendar : 2017-09-07
         * onebegintime : 2017-09-07 08:00:00
         * twoendtime : 2017-09-07 15:00:00
         * tbmstatus : [早退]
         * importsignflag : 9
         * flg : null
         * signCause : null
         */

        private String psname;
        private String pk_psndoc;
        private String calendar;
        private String onebegintime;
        private String twoendtime;
        private String tbmstatus;
        private String importsignflag;
        private Object flg;
        private Object signCause;

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

        public Object getFlg() {
            return flg;
        }

        public void setFlg(Object flg) {
            this.flg = flg;
        }

        public Object getSignCause() {
            return signCause;
        }

        public void setSignCause(Object signCause) {
            this.signCause = signCause;
        }
    }

    public static class JbWorkAttendanceListBean {
        /**
         * psname : 荣令山
         * pk_psndoc : 0001A31000000010WWJZ
         * calendar : 2017-09-11
         * onebegintime : 2017-09-11 08:00:00
         * twoendtime : 2017-09-07 21:00:00
         * tbmstatus : [转调休加班]
         * importsignflag : 9
         * flg : null
         * signCause : null
         */

        private String psname;
        private String pk_psndoc;
        private String calendar;
        private String onebegintime;
        private String twoendtime;
        private String tbmstatus;
        private String importsignflag;
        private Object flg;
        private Object signCause;

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

        public Object getFlg() {
            return flg;
        }

        public void setFlg(Object flg) {
            this.flg = flg;
        }

        public Object getSignCause() {
            return signCause;
        }

        public void setSignCause(Object signCause) {
            this.signCause = signCause;
        }
    }

    public static class XjWorkAttendanceListBean {
        /**
         * psname : 荣令山
         * pk_psndoc : 0001A31000000010WWJZ
         * calendar : 2017-09-21
         * onebegintime : 2017-09-21 08:00:00
         * twoendtime : 2017-09-21 17:00:00
         * tbmstatus : [加班转调休]
         * importsignflag : 9
         * flg : null
         * signCause : null
         */

        private String psname;
        private String pk_psndoc;
        private String calendar;
        private String onebegintime;
        private String twoendtime;
        private String tbmstatus;
        private String importsignflag;
        private Object flg;
        private Object signCause;

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

        public Object getFlg() {
            return flg;
        }

        public void setFlg(Object flg) {
            this.flg = flg;
        }

        public Object getSignCause() {
            return signCause;
        }

        public void setSignCause(Object signCause) {
            this.signCause = signCause;
        }
    }

    public static class YcWorkAttendanceListBean {
        /**
         * psname : 荣令山
         * pk_psndoc : 0001A31000000010WWJZ
         * calendar : 2017-09-06
         * onebegintime : 2017-09-06 10:00:00
         * twoendtime : 2017-09-06 17:00:00
         * tbmstatus : [迟到]
         * importsignflag : 9
         * flg : null
         * signCause : null
         */

        private String psname;
        private String pk_psndoc;
        private String calendar;
        private String onebegintime;
        private String twoendtime;
        private String tbmstatus;
        private String importsignflag;
        private Object flg;
        private Object signCause;

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

        public Object getFlg() {
            return flg;
        }

        public void setFlg(Object flg) {
            this.flg = flg;
        }

        public Object getSignCause() {
            return signCause;
        }

        public void setSignCause(Object signCause) {
            this.signCause = signCause;
        }
    }
}
