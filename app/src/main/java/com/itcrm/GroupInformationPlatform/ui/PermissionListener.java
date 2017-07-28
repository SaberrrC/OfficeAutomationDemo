package com.itcrm.GroupInformationPlatform.ui;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui
 * Author:Created by Tsui on Date:2017/1/20 9:49
 * Description:6.0权限监听接口回调
 */
public interface PermissionListener {
    /**
     * 权限授予
     */
    void onGranted();

    /**
     * 权限拒绝
     */
    void onDenied();
}
