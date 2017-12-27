package com.shanlinjinrong.oa.common;

/**
 * Created by lvdinghao on 2017/8/24.
 * java部署的服务器的接口
 */
public interface ApiJava {

    String REQUEST_CODE_OK         = "000000"; //返回成功code
    String REQUEST_USER_NOT_EXIST  = "010003"; //不存在的用户
    String REQUEST_TOKEN_OUT_TIME  = "010004"; //token 失效
    String REQUEST_NOT_LOGIN       = "010005"; //用户未登录
    String REQUEST_TOKEN_NOT_EXIST = "010006"; //token 不存在
    String ERROR_TOKEN             = "020005";//token不存在或者用户不存在
    String REQUEST_EXCEL_ERROR     = "010007"; //上传文件异常
    String REQUEST_NULL            = "010008"; //请求为空
    String REQUEST_NO_RESULT       = "020000"; //查询无结果
    String REQUEST_HAD_REPORTED    = "030000"; //该天日报以填写
    String REQUEST_REPORT_ID_NULL  = "010009"; //日报id空
    String REQUEST_UNKNOW_EX       = "999999"; //未知异常

    /************   接口给的code  ************/
    String SUCCESS                   = "000000";// 表示成功
    String OUT_TIME_TOKEN            = "010004";// token 失效
    String NOT_LOGIN                 = "010005"; // 用户未登录
    String NOT_EXIST_TOKEN           = "010006";// token 不存在
    String UNKNOW_EXCEPTION          = "999999"; // 未知异常
    String EXCEL_ERROR               = "010007";//上传文件异常
    String NULL_DATE_ERROR           = "010008";//请求为空
    String NO_CONTENT                = "020000";//查询无结果
    String EXISTENCE_REPORT_DAY      = "030000";//该天日报以填写
    String NULL_REPORT_ID            = "010009";//日报id为空
    String RATE_OVER                 = "010011";//日报以评，不能改
    String ERROR_OPERATION           = "020008";//操作失败
    String FAST_DFS_CONNECTION_ERROR = "010011";//文件存储异常
    String NULL_DATE_DISTANCE        = "010020";//"周报选择的时间跨度必须为7天"
    String NOT_ROLE                  = "000001";//权限不足
    String EXIST_NAME                = "000002";//角色名已存在
    /************   登陆接口 code  ************/
    String NOT_EXIST_USER            = "010003"; // 用户名或密码不存在

    //发起日报
    String DAILY_REPORT = "dailyreport";

    String WEEK_REPORT = "weekreport";

    String WEEK_REPORT_LIST   = "weekreport/weekList";
    //当前登录用户获取其leader
    String GET_CURRENT_LEADER = "user/getCurrentLeader";

    //根据当前登录用户获取当前部门用户列表，并支持姓名模糊搜索
    String SAME_ORGANIZATION = "user/query/sameOrganization";

    //获取公司所有联系人
    String ALL_ORGANIZATION = "organization/queryAllOrganiza";

    //发起周报
    String SEND_WEEK_REPORT = "weekreport/add";

    //发起周报
    String LOOK_LAST_WEEK = "weekreport/lastWeek";

    //审核日报列表
    String LEADER_READ_DAILY_REPORT = "dailyreport/leader";

    //审核周报列表
    String LEADER_READ_WEEK_REPORT = "weekreport/leader";

    //退回日报接口
    String REJECT_DAILY_REPORT = "dailyreport/reject/";

    //退回周报接口
    String REJECT_WEEK_REPORT = "weekreport/reject/";


    // 更新日报
    String UPDATE_DAILY_REPORT = "upddailyreport";

    // 更新周报
    String UPDATE_WEEK_REPORT = "weekreport/upd";

    // 获取单据编号接口
    String GET_MONOCODE = "nchrcommon/getBillCode";

    // 获取出差类别接口
    String EVENCTIONTYPE = "nchrEvection/queryBilltype";

    //获取时长接口
    String QUERYDURATION = "nchrcommon/queryDuration";

    //申请出差接口
    String EVECTIONAPPLY = "nchrEvection/submitEvectionApply";

    //我的申请接口
    String MYAPPLY_QUERY_APPROVE = "myApply/queryApproveByAll";

    //申请加班接口
    String ADDWEORKAPPLY = "WorkApply/addWorkApply";

    //申请休假接口
    String SUBMITFURLOUGH = "nchrFurlough/submitFurlough";

    //获取签卡编码
    String SIGNREASON = "nchrSign/findSignReason";

    //获取签卡编码
    String SAVESIGN = "nchrSign/saveSign";

    //获取工作交接人
    String HANDOVERUSER = "HandoverUser/getHandoverUser";

    //薪资查询
    String QUERYSALART = "nchrsalary/querySalary";

    //查看申请详情
    String MYAPPLY_QUERY_APPROVE_INFO = "myApply/queryApproveDetailByBillCode";

    //查看申请详情
    String TACK_BACK = "myApply/approveCallBack";

    //待办事宜
    String SEARCH_APPLICATION = "MyAplication/selectMyAplication";

    //同意和驳回 Approve/allApprove
    String ARGEE_DISAGREE_APPROVE = "Approve/allApprove";

    //同意和驳回 Approve/allApprove
    String DELETE_APPROVEL = "nchrSign/deleteBillByCode";

    //群组员头像查询
    String QUERY_USER_LIST_INFO = "user/queryUserByCodes";
}