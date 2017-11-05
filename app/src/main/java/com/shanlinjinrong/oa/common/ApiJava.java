package com.shanlinjinrong.oa.common;

/**
 * Created by lvdinghao on 2017/8/24.
 * java部署的服务器的接口
 */
public class ApiJava {

    public static final String REQUEST_CODE_OK = "000000"; //返回成功code
    public static final String REQUEST_USER_NOT_EXIST = "010003"; //不存在的用户
    public static final String REQUEST_TOKEN_OUT_TIME = "010004"; //token 失效
    public static final String REQUEST_NOT_LOGIN = "010005"; //用户未登录
    public static final String REQUEST_TOKEN_NOT_EXIST = "010006"; //token 不存在
    public static final String ERROR_TOKEN = "020005";//token不存在或者用户不存在
    public static final String REQUEST_EXCEL_ERROR = "010007"; //上传文件异常
    public static final String REQUEST_NULL = "010008"; //请求为空
    public static final String REQUEST_NO_RESULT = "020000"; //查询无结果
    public static final String REQUEST_HAD_REPORTED = "030000"; //该天日报以填写
    public static final String REQUEST_REPORT_ID_NULL = "010009"; //日报id空
    public static final String REQUEST_UNKNOW_EX = "999999"; //未知异常


    /************   接口给的code  ************/
    public static final String SUCCESS = "000000";// 表示成功
    public static final String NOT_EXIST_USER = "010003"; // 不存在的用户
    public static final String OUT_TIME_TOKEN = "010004";// token 失效
    public static final String NOT_LOGIN = "010005"; // 用户未登录
    public static final String NOT_EXIST_TOKEN = "010006";// token 不存在
    public static final String UNKNOW_EXCEPTION = "999999"; // 未知异常
    public static final String EXCEL_ERROR = "010007";//上传文件异常
    public static final String NULL_DATE_ERROR = "010008";//请求为空
    public static final String NO_CONTENT = "020000";//查询无结果
    public static final String EXISTENCE_REPORT_DAY = "030000";//该天日报以填写
    public static final String NULL_REPORT_ID = "010009";//日报id为空
    public static final String RATE_OVER = "010011";//日报以评，不能改
    public static final String ERROR_OPERATION = "020008";//操作失败
    public static final String FAST_DFS_CONNECTION_ERROR = "010011";//文件存储异常
    public static final String NULL_DATE_DISTANCE = "010020";//"周报选择的时间跨度必须为7天"
    public static final String NOT_ROLE = "000001";//权限不足
    public static final String EXIST_NAME = "000002";//角色名已存在


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


    // 获取单据编号接口
    public static final String GET_MONOCODE = "nchrcommon/getBillCode";

    // 获取出差类别接口
    public static final String EVENCTIONTYPE = "nchrEvection/queryBilltype";

    //获取时长接口
    public static final String QUERYDURATION = "nchrcommon/queryDuration";

    //申请出差接口
    public static final String EVECTIONAPPLY = "nchrEvection/submitEvectionApply";

    //我的申请接口
    public static final String MYAPPLY_QUERY_APPROVE = "myApply/queryApproveByAll";
    //申请加班接口
    public static final String ADDWEORKAPPLY = "WorkApply/addWorkApply";

    //申请休假接口
    public static final String SUBMITFURLOUGH = "nchrFurlough/submitFurlough";

    //查看申请详情
    public static final String MYAPPLY_QUERY_APPROVE_INFO = "myApply/queryApproveDetailByBillCode";

    //查看申请详情
    public static final String TACK_BACK = "myApply/approveCallBack";

    //待办事宜
    public static final String SEARCH_APPLICATION = "MyAplication/selectMyAplication";
}