package com.shanlinjinrong.oa.common;

import android.os.Environment;

/**
 * 概述：常量类
 * Created by KevinMeng on 2016/5/9.
 */
public class Constants {

    //TODO 当前版本为测试环境，baseurl写死了，没用，先放这
    public final static String GETBASEURLCID = "6";

    public final static String CID = "sl";//公司ID

    //TODO 打包时候更改 善林图片前缀:public.sl.s1.zhitongoa.com
    public final static String SLPicBaseUrl = "http://";


    /**
     * 日程安排的会议安排标识
     */
    public final static String MEETING_PLAN = "meeting_plan";
    /**
     * 日程安排的我发起的会议标识
     */
    public final static String ME_LAUNCH_MEETING = "me_launch_meeting";
    /**
     * 推送消息进入会议详情的标识
     */
    public final static String PUSH_CONFORM_MEETING = "push_conform_meeting";

    /**
     * 记事本编辑标识
     */
    public static final String EDIT_NOTE = "edit_note";
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "voice";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "video";


    /****************6.0权限*********************/
    /**
     * 拨打电话权限码
     */
    public static final int PERMISSIONS_REQUECT_CODE_CALL = 1;
    /**
     * 录音权限码
     */
    public static final int PERMISSIONS_REQUECT_RECORD_AUDIO = 2;

    /****************权限end*********************/

    /**
     * 下载文件、缓存文件地址
     */
    public static class FileUrl {
        /**
         * APP缓存文件根目录
         */
        public static final String BASE = Environment.getExternalStorageDirectory() +
                "/GroupInformationPlatform/";

        /**
         * Apk下载地址
         */
        public static final String UPDATE_APP = BASE + "update/";

        /**
         * 缓存文件
         */
        public static final String TEMP = BASE + "temp/";
    }

    /**
     * 正则表达式
     */
    public class Regex {
        /**
         * 登录电子邮件
         */
        public static final String REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\." +
                "\\w+([-.]\\w+)*";

        /**
         * 登录密码
         */
        public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,15}";
        /**
         * 登录工号
         */
        public static final String REGEX_CODE = "^[a-zA-Z0-9]{6,10}";

        /**
         * 手机号码
         */
        public static final String REGEX_PHONE = "^1[34578]\\d{9}$";
    }
}