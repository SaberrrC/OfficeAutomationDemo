package com.shanlinjinrong.oa.common;

import android.os.Environment;

import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;

/**
 * 概述：常量类
 * Created by KevinMeng on 2016/5/9.
 */
public class Constants {


    /***************环信错误码***************/
    /**
     * 服务器繁忙
     */
    public final static int SERVER_BUSY               = 302;
    /**
     * 无法访问到服务器
     */
    public final static int SERVER_NOT_REACHABLE      = 300;
    /**
     * 聊天功能限制
     */
    public final static int SERVER_SERVICE_RESTRICTED = 305;
    /**
     * 服务器响应超时
     */
    public final static int SERVER_TIMEOUT            = 301;
    /**
     * 未知的服务异常
     */
    public final static int SERVER_UNKNOWN_ERROR      = 303;
    /**
     * 群组不存在
     */
    public final static int GROUP_NOT_EXIST           = 605;
    /**
     * 尚未加入此群组
     */
    public final static int GROUP_NOT_JOINED          = 602;
    /**
     * 群id不正确
     */
    public final static int GROUP_INVALID_ID          = 600;


    /**
     * 公司ID
     */
    public final static String CID = "sl";

    //TODO 打包时候更改 善林图片前缀:public.sl.s1.zhitongoa.com
    //public final static String SLPicBaseUrl = "http://public.testoa.shanlinjinrong.com";

    //public final static String PHPSLPicBaseUrl = "http://";

    /**
     * 日报临时数据
     */
    public final static String WORK_REPORT_TEMP_DATA = "work_report_temp_data";

    /**
     * 周报临时数据
     */
    public final static String WORK_WEEKLY_TEMP_DATA = "work_weekly_temp_data";

    /**
     * 找回密码
     */
    public final static String PHONE_STATUS = "phone_status";
    /**
     * 查询手机号
     */
    public final static String PHONE_NUMBER = "phone_number";
    /**
     * 工号
     */
    public final static String USER_CODE    = "user_code";

    public final static String USER_NAME = "user_name";


    /**
     * 记事本编辑标识
     */
    public static final String EDIT_NOTE                  = "edit_note";
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "voice";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "video";

    // ---------------------------------- 选择通讯录 ----------------------------------

    public static final String SELECTEDTYEPE = "selected_type";

    // ---------------------------------- 群组 ----------------------------------

    public static final String GROUPNAME    = "group_name";
    public static final String FINISH_GROUP = "finish_group";
    public static final String ISGROUPOWNER = "isOwner";

    /**
     * 群名称更改
     */
    public static final int MODIFICATIONNAME = 0;
    /**
     * 群解散
     */
    public static final int GROUPDISSOLVE    = 1;
    /**
     * 成员退出
     */
    public static final int GROUPMEMBERQUIT  = 2;
    /**
     * 成员加入
     */
    public static final int GROUPMEMBERADD   = 3;
    /**
     * 群主更改
     */
    public static final int GROUPOWNERCHANGE = 4;

    // ---------------------------------- 群组 详情 ----------------------------------
    public static final String MEMBERADD    = "add";
    public static final String MEMBERDELETE = "delete";

    // ---------------------------------- 月历 详情 ----------------------------------

    /**
     * 周历 类型
     */
    public static final String CALENDARTYPE   = "itemType";
    /**
     * 周历 状态
     */
    public static final String CALENDARSTATUS = "status";

    /**
     * 周历 开始时间
     */
    public static final String CALENDARSTARTTIME = "startTime";
    /**
     * 周历 结束时间
     */
    public static final String CALENDARENDTIME   = "endTime";
    /**
     * 周历 日期
     */
    public static final String CALENDARDATE      = "date";
    /**
     * 周历 日期
     */
    public static final String CALENDARID        = "id";
    /**
     * 周历 年
     */
    public static final String CALENDARYEAR      = "year";
    /**
     * 周历 年
     */
    public static final String CALENDARMONTH     = "month";
    /**
     * 周历 详情主题
     */
    public static final String CALENDARTITLE     = "title";
    /**
     * 周历 详情内容
     */
    public static final String CALENDARCONTENT   = "content";
    /**
     * 周历 选择时间
     */
    public static final String SELECTEDTIME      = "selectedTime";

    public static final String SELECTEDPOSITION = "position";

    /**
     * 编辑周历
     */
    public static final int WRITECALENDAR   = 0;
    /**
     * 查看周历
     */
    public static final int LOOKCALENDAR    = 2;
    /**
     * 查看会议室
     */
    public static final int MEETINGCALENDAR = 1;


    /****************6.0权限*********************/
    /**
     * 拨打电话权限码
     */
    public static final int PERMISSIONS_REQUECT_CODE_CALL    = 1;
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
        public static final String REGEX_CODE     = "^[a-zA-Z0-9]{6,10}";

        /**
         * 手机号码
         */
        public static final String REGEX_PHONE = "^1[34578]\\d{9}$";
    }
}