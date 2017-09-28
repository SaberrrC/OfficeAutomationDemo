package com.shanlinjinrong.oa.common;

/**
 * Created by lvdinghao on 2017/8/24.
 * java部署的服务器的接口
 */
public class ApiJava {
    //新接口的域名
    public static final String BASE_URL = "http://106.15.205.215:8084/";

    public static final String REQUEST_CODE_OK = "000000"; //返回成功code
    public static final String REQUEST_USER_NOT_EXIST = "010003"; //不存在的用户
    public static final String REQUEST_TOKEN_OUT_TIME = "010004"; //token 失效
    public static final String REQUEST_NOT_LOGIN = "010005"; //用户未登录
    public static final String REQUEST_TOKEN_NOT_EXIST = "010006"; //token 不存在
    public static final String REQUEST_EXCEL_ERROR = "010007"; //上传文件异常
    public static final String REQUEST_NULL = "010008"; //请求为空
    public static final String REQUEST_NO_RESULT = "020000"; //查询无结果
    public static final String REQUEST_HAD_REPORTED = "030000"; //该天日报以填写
    public static final String REQUEST_REPORT_ID_NULL = "010009"; //日报id空
    public static final String REQUEST_UNKNOW_EX = "999999"; //未知异常


    //发起日报
    public static final String DAILY_REPORT = "dailyreport";

    public static final String WEEK_REPORT = "weekreport";

    public static final String WEEK_REPORT_LIST = "weekreport/weekList";
    //当前登录用户获取其leader
    public static final String GET_CURRENT_LEADER = "user/getCurrentLeader";

    //根据当前登录用户获取当前部门用户列表，并支持姓名模糊搜索
    public static final String SAME_ORGANIZATION = "user/query/sameOrganization";

    //获取公司所有联系人
    public static final String ALL_ORGANIZATION = "organization/queryAllOrganiza";

    //发起周报
    public static final String SEND_WEEK_REPORT = "weekreport/add";

    //发起周报
    public static final String LOOK_LAST_WEEK = "weekreport/lastWeek";


    //审核日报列表
    public static final String LEADER_READ_DAILY_REPORT = "dailyreport/leader";

    //审核周报列表
    public static final String LEADER_READ_WEEK_REPORT = "weekreport/leader";

    //退回日报接口
    public static final String REJECT_DAILY_REPORT = "dailyreport/reject/";

    //退回周报接口
    public static final String REJECT_WEEK_REPORT = "weekreport/reject/";


    // 更新日报
    public static final String UPDATE_DAILY_REPORT = "upddailyreport";

    // 更新周报
    public static final String UPDATE_WEEK_REPORT = "weekreport/upd";


}
