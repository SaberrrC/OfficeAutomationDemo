package com.shanlin.oa.ui.helper;

import java.util.List;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.ui
 * Author:Created by CXP on Date: 2016/9/6 10:11.
 * Description:获得联系人帮助类
 */
public class getContactHelper {

    public getContactHelper() {
    }

    /**
     * @return 返回外界一个联系人
     */
    public contact getSingleContact(){
        return null;
    }
    /**
     * @return 返回外界多个联系人
     */
    public List<contact> getMultiContact(){
        return null;
    }

    class contact{
        /** 头像 */
        private String portraits;
        /** UID */
        private String uid;
        /** 用户名称 */
        private String username;
        /** 是否已经选择**/
        private boolean isChecked=false;
    }


}
