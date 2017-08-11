package com.shanlin.oa.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.shanlin.oa.model.User;


/**
 * <h3>Description: 全局配置文件管理类 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/30.<br />
 */
public class AppConfig {

    private static final String IS_AUTO_LOGIN = "is_auto_login";
    public static final String PREF_KEY_YX_TOKEN = "yx_token";

    private Context context;
    private static AppConfig appConfig;

    private static final String APP_CONFIG = "app_config";

    /**
     * 字符串参数默认值，判断需要判断此值，而不是""
     */
    public static final String DEFAULT_ARGUMENTS_VALUE = "";

    /**
     * 进入BI系统tag
     */
    public static final String USER_TYPE_BI = "1";

    /**
     * 网络请求baseUrl
     */
    public static final String BASE_URL = "base_url";

    //oid
    public static final String PREF_KEY_OID = "pref_key_user_oid";
    //isleader
    public static final String PREF_KEY_ISLEADER = "pref_key_isLeader";
    //user uuid
    public static final String PREF_KEY_USER_UID = "pref_key_user_uid";
    //user token
    public static final String PREF_KEY_TOKEN = "pref_key_private_token";
    //user email
    public static final String PREF_KEY_USER_EMAIL = "pref_key_user_email";
    //头像
    public static final String PREF_KEY_PORTRAITS = "pref_key_private_portraits";
    //用户名
    public static final String PREF_KEY_USERNAME = "pref_key_private_username";
    //是否是领导
    public static final String PREF_KEY_IS_LEADER = "pref_key_is_leader";
    //性别
    public static final String PREF_KEY_SEX = "pref_key_private_sex";
    //手机号码
    public static final String PREF_KEY_PHONE = "pref_key_private_phone";
    //入职日期
    public static final String PREF_KEY_HIREDATE = "pref_key_private_hiredate";
    //公司名称
    public static final String PREF_KEY_COMPANY_NAME = "pref_key_private_company_name";
    //岗位名称
    public static final String PREF_KEY_POST_NAME = "pref_key_private_post_name";
    //岗位工号
    public static final String PREF_KEY_CODE = "pref_key_private_code";
    //部门名称
    public static final String PREF_KEY_DEPARTMENT_NAME = "pref_key_private_department_name";
    //是否是原始密码
    public static final String PREF_KEY_IS_INIT_PWD = "pref_key_private_is_init_pwd";
    //1-进入bi, 0进入首页
    public static final String PREF_KEY_TYPE = "pref_key_private_type";
    //即时通讯未读条数
    public static final String COMMUNICATION_COUNT = "communication_count";


    /**
     * 岗位ID
     */
    private static final String PREF_KEY_POST_ID = "pref_key_post_id";
    /**
     * 部门ID
     */
    public static final String PREF_KEY_DEPARTMENT_ID = "pref_key_department_id";

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.context = context;
        }
        return appConfig;
    }

    /**
     * 根据key获取参数值
     *
     * @param key eg:PREF_KEY_PRIVATE_TOKEN, PREF_KEY_USER_UID
     * @return 参数值
     */
    public String get(String key) {
        return context.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE)
                .getString(key, DEFAULT_ARGUMENTS_VALUE);
    }


    /**
     * 获取用户Token
     *
     * @return User Token
     */
    public String getPrivateToken() {
        return get(PREF_KEY_TOKEN);
    }

    /**
     * 获取用户部门Id
     *
     * @return 部门Id
     */
    public String getDepartmentId() {
        return get(PREF_KEY_DEPARTMENT_ID);
    }

    /**
     * 获取用户uid
     *
     * @return User uid
     */
    public String getPrivateUid() {
        return get(PREF_KEY_USER_UID);
    }

    /**
     * @return 工号
     */
    public String getPrivateCode() {
        return get(PREF_KEY_CODE);
    }

    public void set(User user, Boolean isAutoLogin) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_CONFIG,
                Context.MODE_PRIVATE).edit();
        editor.putString(PREF_KEY_TOKEN, user.getToken());
        editor.putString(PREF_KEY_CODE, user.getCode());
        editor.putString(PREF_KEY_USER_UID, user.getUid());
        editor.putString(PREF_KEY_USER_EMAIL, user.getEmail());
        editor.putString(PREF_KEY_DEPARTMENT_ID, user.getDepartmentId());
        editor.putString(PREF_KEY_POST_ID, user.getPostId());
        editor.putString(PREF_KEY_USERNAME, user.getUsername());
        editor.putString(PREF_KEY_SEX, user.getSex());
        editor.putString(PREF_KEY_PHONE, user.getPhone());
        editor.putString(PREF_KEY_HIREDATE, user.getHiredate());
        editor.putString(PREF_KEY_COMPANY_NAME, user.getCompanyName());
        editor.putString(PREF_KEY_DEPARTMENT_NAME, user.getDepartmentName());
        editor.putString(PREF_KEY_PORTRAITS, "http://" + user.getPortraits());
        editor.putString(PREF_KEY_POST_NAME, user.getPostName());
        editor.putString(PREF_KEY_IS_LEADER, user.getIsleader());
        editor.putBoolean(IS_AUTO_LOGIN, isAutoLogin);
        editor.putString(PREF_KEY_IS_INIT_PWD, user.getIs_initial_pwd());
        editor.putString(PREF_KEY_YX_TOKEN, user.getYx_token());
        editor.putString(PREF_KEY_ISLEADER, user.getIsleader());
        editor.putString(PREF_KEY_OID, user.getOid());
        editor.apply();
    }

    /**
     * 保存配置信息
     *
     * @param key   eg:PREF_KEY_PRIVATE_TOKEN PREF_KEY_USER_UUID
     * @param value
     */
    public void set(String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_CONFIG,
                Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 设置自动登录
     *
     * @param autoLogin
     */
    public void setAutoLogin(boolean autoLogin) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_CONFIG,
                Context.MODE_PRIVATE).edit();
        editor.putBoolean(IS_AUTO_LOGIN, autoLogin);
        editor.apply();
    }

    /**
     * 获取当前是否是自动登录
     *
     * @return
     */
    public boolean isAutoLogin() {
        return context.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE)
                .getBoolean(IS_AUTO_LOGIN, false);
    }

    /**
     * 登出需要调用此方式
     */
    public void clearLoginInfo() {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_CONFIG,
                Context.MODE_PRIVATE).edit();
        editor.putString(PREF_KEY_TOKEN, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_USER_UID, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_DEPARTMENT_ID, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_POST_ID, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_USERNAME, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_SEX, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_PHONE, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_HIREDATE, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_COMPANY_NAME, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_DEPARTMENT_NAME, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_PORTRAITS, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_POST_NAME, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_TYPE, DEFAULT_ARGUMENTS_VALUE);
        editor.putString(PREF_KEY_YX_TOKEN, DEFAULT_ARGUMENTS_VALUE);
        editor.putBoolean(IS_AUTO_LOGIN, false);
        //关心存入结果则使用commit如果不关心存入结果则使用apply
        //editor.apply();
        editor.commit();
    }
}