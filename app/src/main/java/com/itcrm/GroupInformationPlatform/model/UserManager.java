package com.itcrm.GroupInformationPlatform.model;

import java.io.Serializable;

/**
 * Created by gaobin on 2016/11/15.
 */

public class UserManager implements Serializable {

    public String getPartID() {
        return partID;
    }
    public void setPartID(String partID) {
        this.partID = partID;
    }
    private String partID;





    private UserManager() {
    }
    private static UserManager userManager;
    public static UserManager getInstance() {
        if (userManager == null) {
            synchronized (UserManager.class) {
                if (userManager == null) {
                    userManager = new UserManager();
                }
            }
        }
        return userManager;
    }


}
