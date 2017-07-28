package com.hyphenate.easeui;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.hyphenate.easeui
 * Author:Created by Tsui on Date:2017/2/16 10:06
 * Description:6.0权限接口
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
