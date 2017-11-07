package com.example.retrofit.model.responsebody;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/5 0005.
 */

public class CountResponse1 implements Serializable{

    /**
     * psname : 李煜
     * mobile : 1360000000
     * orgname : 善林（上海）金融信息服务有限公司
     * deptname : 公关部
     * code : 011000052
     * ifSelf : false
     */

    private String psname;
    private String mobile;
    private String orgname;
    private String deptname;
    private String code;
    private boolean ifSelf;
    public boolean isSelected = false;

    public String getPsname() {
        return psname;
    }

    public void setPsname(String psname) {
        this.psname = psname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isIfSelf() {
        return ifSelf;
    }

    public void setIfSelf(boolean ifSelf) {
        this.ifSelf = ifSelf;
    }
}
