package com.shanlinjinrong.oa.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lvdinghao on 2017/8/24.
 * java部署的服务器的接口
 */
public interface ApiJava {
    /**
     * 返回成功code
     */
    String REQUEST_CODE_OK = "000000";
    /**
     * 不存在的用户
     */
    String NOT_EXIST_USER = "010003";
    /**
     * token 失效
     */
    String REQUEST_TOKEN_OUT_TIME = "010004";
    /**
     * 用户未登录
     */
    String REQUEST_NOT_LOGIN = "010005";
    /**
     * token 不存在
     */
    String REQUEST_TOKEN_NOT_EXIST = "010006";
    /**
     * token不存在或者用户不存在
     */
    String ERROR_TOKEN = "020005";
    /**
     * 上传文件异常
     */
    String REQUEST_EXCEL_ERROR = "010007";
    /**
     * 请求为空
     */
    String REQUEST_NULL = "010008";
    /**
     * 查询无结果
     */
    String REQUEST_NO_RESULT = "020000";
    /**
     * 该天日报以填写
     */
    String REQUEST_HAD_REPORTED = "030000";
    /**
     * 日报id空
     */
    String REQUEST_REPORT_ID_NULL = "010009";
    /**
     * 未知异常
     */
    String REQUEST_UNKNOW_EX = "999999";

    /************   接口给的code  ************/

    /**
     * 发起日报
     */
    String DAILY_REPORT = "dailyreport";

    /**
     * 周报
     */
    String WEEK_REPORT = "weekreport";

    /**
     *
     */
    String WEEK_REPORT_LIST = "weekreport/weekList";

    /**
     * 当前登录用户获取其leader
     */
    String GET_CURRENT_LEADER = "user/getCurrentLeader";

    /**
     * 根据当前登录用户获取当前部门用户列表，并支持姓名模糊搜索
     */
    String SAME_ORGANIZATION = "user/query/sameOrganization";

    /**
     * 根据当前登录用户获取当前部门用户列表，并支持姓名模糊搜索
     */
    String SEND_WEEK_REPORT = "weekreport/add";

    /**
     * 发起周报
     */
    String LOOK_LAST_WEEK = "weekreport/lastWeek";

    /**
     * 审核日报列表
     */
    String LEADER_READ_DAILY_REPORT = "dailyreport/leader";

    /**
     * 审核周报列表
     */
    String LEADER_READ_WEEK_REPORT = "weekreport/leader";

    /**
     * 退回日报接口
     */
    String REJECT_DAILY_REPORT = "dailyreport/reject/";

    /**
     * 退回周报接口
     */
    String REJECT_WEEK_REPORT = "weekreport/reject/";


    /**
     * 更新日报
     */
    String UPDATE_DAILY_REPORT = "upddailyreport";

    /**
     * 更新周报
     */
    String UPDATE_WEEK_REPORT = "weekreport/upd";

    /**
     * 获取出差类别接口
     */
    String EVENCTIONTYPE = "nchrEvection/queryBilltype";

    /**
     * 获取时长接口
     */
    String QUERYDURATION = "nchrcommon/queryDuration";

    /**
     * 申请出差接口
     */
    String EVECTIONAPPLY = "nchrEvection/submitEvectionApply";

    /**
     * 我的申请接口
     */
    String MYAPPLY_QUERY_APPROVE = "myApply/queryApproveByAll";

    /**
     * 申请加班接口
     */
    String ADDWEORKAPPLY = "WorkApply/addWorkApply";

    /**
     * 申请休假接口
     */
    String SUBMITFURLOUGH = "nchrFurlough/submitFurlough";

    /**
     * 获取签卡编码
     */
    String SIGNREASON = "nchrSign/findSignReason";

    /**
     * 获取签卡编码
     */
    String SAVESIGN = "nchrSign/saveSign";

    /**
     * 获取工作交接人
     */
    String HANDOVERUSER = "HandoverUser/getHandoverUser";

    /**
     * 查看申请详情
     */
    String MYAPPLY_QUERY_APPROVE_INFO = "myApply/queryApproveDetailByBillCode";

    /**
     * 查看申请详情
     */
    String TACK_BACK = "myApply/approveCallBack";

    /**
     * 待办事宜
     */
    String SEARCH_APPLICATION = "MyAplication/selectMyAplication";

    /**
     * 同意和驳回 Approve/allApprove
     */
    String ARGEE_DISAGREE_APPROVE = "Approve/allApprove";

    /**
     * 同意和驳回 Approve/allApprove
     */
    String DELETE_APPROVEL = "nchrSign/deleteBillByCode";

    /**
     * 修改邮箱
     */
    String CHANGE_EMAIL = "user/updateEmail";


    String RESPONSES_KEY_CODE = "code";

    String RESPONSES_KEY_INFO = "info";

    String RESPONSES_KEY_DATA = "data";

    //*****************************************************************//
    /**
     * 用户信息接口
     */
    String LOGIN = "site/login";

    /**
     * 获取通讯录接口2
     */
    String GET_CONTACTS = "organization/queryOrgAndUser";

    /**
     * 搜索联系人
     */
    String PHONEBOOK_SEARCHPHONEBOOK = "organization/queryUserByName";

    /**
     * 我的邮箱
     */
    String MY_MAIL = "http://mail.shanlinjinrong.com/";

    /**
     * 修改密码
     */
    String PASSWORD_UPDATE = "user/changepwd";

    /**
     * 修改电话
     */
    String PHONENUMBER_UPDATE = "user/changePhone";

    /**
     * 用户反馈
     */
    String FEEDBACK = "feedback/feedback";

    /**
     * 通知公告已读接口
     */
    String NOTICE_HAD_READ = "message/read";

    /**
     * 获取验证码
     */
    String SENDS_CAPTCHA = "user/webCode";

    /**
     * 查询工号信息
     */
    String USERS_SEARCH = "user/search";


    /**
     * 新会议室信息
     */
    String NEW_MEETINGROOMS = "newMeetingRooms";

    /**
     * 新会议室记录信息
     */
    String NEW_MEETING_RECORD = "newMeetings/reserve";


    /**
     * 新会议室预定时间段情况
     */
    String NEW_MEETING_ALR_MEETING = "/newMeetings/alrMeeting";


    /**
     * 添加新会议室
     */
    String ADD_NEW_MEETING = "newMeetings/save";

    /**
     * 取消新会议室
     */
    String DELETE_NEW_MEETING = "newMeetings/deleteMeeting";
    /**
     * 新会议室调期
     */
    String MODIFY_NEW_MEETING = "newMeetings/updateMeeting";

    /**
     * 获取会议详情
     */
    String LOOK_NEW_MEETING_INFO = "newMeetings/meetingInfo";

    /**
     * 找回密码，确认邮箱
     */
    String USERS_REPWD = "user/repwd";

    /**
     * 获取个人信息
     */
    String ID_SEARCH_USER_DETAILS = "user/getUserInfoById";

    /**
     * 获取个人信息
     */
    String CODE_SEARCH_USER_DETAILS = "user/queryUserByCodes";

    //*****************************************************************//

    /**
     * 获取接口返回Code的值
     *
     * @param jsonObject 接口返回数据
     * @return code
     * @throws JSONException
     */
    static int getCode(JSONObject jsonObject) throws JSONException {
        return Integer.parseInt(jsonObject.getString(RESPONSES_KEY_CODE));
    }

    /**
     * 获取接口返回info的值-字符串
     *
     * @param jsonObject 接口返回数据
     * @return info
     * @throws JSONException
     */
    static String getInfo(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(RESPONSES_KEY_INFO);
    }

    /**
     * 获取接口返回data的值-JSONObject
     *
     * @param jsonObject 接口返回数据
     * @return data
     * @throws JSONException
     */
    static JSONObject getDataToJSONObject(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(RESPONSES_KEY_DATA);
    }

    /**
     * 获取接口返回data的值-JSONArray
     *
     * @param jsonObject 接口返回数据
     * @return data
     * @throws JSONException
     */
    static JSONArray getDataToJSONArray(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONArray(RESPONSES_KEY_DATA);
    }

}