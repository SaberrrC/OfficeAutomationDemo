package com.shanlinjinrong.oa.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 概述：后台数据接口管理类 <br />
 * Created by KevinMeng on 2016/7/28.
 */
public class Api {
    public static final String PHP_DEBUG_URL = "http://118.31.18.67:86";

    public static final String PHP_URL = "";

    private static final String RESPONSES_KEY_CODE = "code";

    private static final String RESPONSES_KEY_INFO = "info";

    private static final String RESPONSES_KEY_DATA = "data";
    /**
     * 所有接口返回code=200表示成功
     */
    public static final int RESPONSES_CODE_OK = 200;

    /**
     * 用户名错误
     */
    public static final int RESPONSES_CODE_ACCOUNT_USERNAME_NOT_EXIST = 302;
    /**
     * 用户密码错误
     */
    public static final int RESPONSES_CODE_ACCOUNT_PASSWORD_ERROR = 303;
    /**
     * 用户被冻结
     */
    public static final int RESPONSES_CODE_ACCOUNT_USER_FREEZE = 610;
    /**
     * 所有接口返回code=304表示Token不匹配
     */
    public static final int RESPONSES_CODE_TOKEN_NO_MATCH = 304;
    /**
     * 所有接口返回code=362 表示反馈失败
     */
    public static final int RESPONSES_CODE_TOKEN_FEEDBACK_FAILURE = 362;
    /**
     * 所有接口返回code=305表示Uid为空
     */
    public static final int RESPONSES_CODE_UID_NULL = 305;
    /**
     * 所有接口返回code=364表示审批详情不存在
     */
    public static final int APPROVAL_DETAIL_NOT_EXIST = 364;
    /**
     * 所有接口返回code=201表示返回数据为空
     */
    public static final int RESPONSES_CODE_DATA_EMPTY = 201;
    /**
     * 所有KjFrame onFailure返回的Error Code，-1表示无网络
     */
    public static final int RESPONSES_CODE_NO_NETWORK = -1;
    /**
     * 所有KjFrame onFailure返回的Error Code，500表示无响应
     */
    public static final int RESPONSES_CODE_NO_RESPONSE = 500;
    /**
     * 所有接口返回code=365表示返回数据为空
     */
    public static final int RESPONSES_CONTENT_EMPTY = 365;

    /**
     * 没有这个用户
     */
    public static final int RESPONSES_CODE_NO_ACCOUNT = 823;

    /**
     * {"code":"393","info":"分页为空"}
     */
    public static final int LIMIT_CONTENT_EMPTY = 393;

    /***
     * 回复工作汇报一系列
     ***/
    public static final int REPORT_ID_CANT_NULL = 604;
    public static final int YOU_HAVE_NOT_REPORT = 703;
    public static final int you_hava_reply_report = 704;
    public static final int report_content_cant_null = 705;

    /***
     * 找回密码
     ***/
    public static final int SEND_FAILD_TRY_AGIN = 631;

    //审批驳回失败
    public static final int REJECT_FAIL = 373;

    //*****************************************************************//
    /**
     * 用户信息接口
     */
    public static final String LOGIN = "site/login";

    /**
     * 获取通讯录接口1
     */
    //public static final String GET_CONTACTS = "phonebook/list";

    /**
     * 获取通讯录接口2
     */
    public static final String GET_CONTACTS = "organization/queryAllOrganiza";
    /**
     * 搜索联系人
     */
    public static final String PHONEBOOK_SEARCHPHONEBOOK = "phonebook/searchphonebook";
    /**
     * 登录时间判断接口
     */
    public static final String  SITE_TIMEOUT="site/timeout";
    /**
     * 选择接收人
     */
    public static final String PUBLIC_CONTACTS = "report/contact";
    /**
     * 我的邮箱
     */
    public static final String MY_MAIL = "http://mail.shanlinjinrong.com/";
    /**
     * 选择抄送人
     */
    public static final String PUBLIC_COPY_CONTACTS = "report/cclist";
    /**
     * 选择会议室新接口
     */
    public static final String CONFERENCE_SELECTMEETINGROOMNEW="conference/selectmeetingroomnew";
    /**
     * 选择参加人
     */
    public static final String CONFERENCE_CCLIST = "conference/cclist";
    /**
     * 根据开始时间获取可使用时间
     */
    public static final String  CONFERENCE_GETOCCUPYTIMEBYBEGINTIME="conference/getoccupytimebybegintime";
    /**
     * 获取我发起的工作汇报
     */
    public static final String REPORT_SEND = "report/launched";

    /**
     * 获取发送给我的工作汇报
     */
    public static final String REPORT_SEND_TO_ME = "report/sendtome";
    /**
     * 抄送搜索联系人更新
     */
    public static final String REPORT_CCLIST = "report/cclist";

    /**
     * 工作汇报详情（已读）接口
     */
    public static final String REPORT_DETAIL = "report/detail";
    /**
     * 发送工作汇报（获得直属领导）
     */
    public static final String GET_LEADER = "report/getleader";
    /**
     * 工作汇报回复接口
     */
    public static final String REPORT_REPLY = "report/reply";
    /**
     * 推送已读接口（新增）
     */
    public static final String MESSAGE_READPUSH = "message/readpush";


    /**
     * 获取抄送给我的工作汇报
     */
    public static final String REPORT_COPY_TO_ME = "report/copytome";

    /**
     * 工作汇报发送给后台
     */
    public static final String REPORT_SEND_TO_BACKGROUND = "report/add";

    /**
     * 主界面未读消息URL
     */
    public static final String TAB_UN_READ_MSG_COUNT = "message/getunread";

    /**
     * 工作汇报上传照片
     */
    public static final String PIC_UPLOAD = "upload/ossupload";
    /**
     * 根据日期获取当天可使用时间接口
     */
    public static final String  CONFERENCE_GETOCCUPYTIME="conference/getoccupytime";
    /**
     * 头像
     */
    public static final String PERSON_UPLOAD = "person/upload";
    /**
     * 读取周报
     */
    public static final String GET_WEEKLY_REPORT = "report/load";

    /**
     * 修改密码
     */
    public static final String PASSWORD_UPDATE = "person/changepassword";
    /**
     * 找回密码   forgetpassword/forgetpassword
     */
    public static final String FIND_PASSWORD = "forgetpassword/forgetpassword";

    /**
     * 修改电话
     */
    public static final String PHONENUMBER_UPDATE = "person/changephone";

    /**
     * 用户反馈
     */
    public static final String FEEDBACK = "person/feedback";
    /**
     * 通知公告
     */
    public static final String MESSAGE_NOTICE = "message/notices";
    /**
     * 审批列表：我发起的
     */
    public static final String APPROVAL_LIST_ME_LAUNCH = "approval/getsendlist";
    /**
     * 审批列表：待我审批
     */
    public static final String APPROVAL_LIST_WAIT_APPROVAL = "approval/getapprivallist";
    /**
     * 审批列表：我审批的
     */
    public static final String APPROVAL_LIST_ME_APPROVALED = "approval/getapprovedlist";
    /**
     * 待我审批：驳回(请假)
     */
    public static final String APPROVAL_TURNED_DOWN_OFFWORK = "approval/turneddownoffwork";
    /**
     * 待我审批：驳回(出差)
     */
    public static final String APPROVAL_TURNED_DOWN_TRIP = "approval/turneddowntrip";
    /**
     * 待我审批：驳回(加班)
     */
    public static final String APPROVAL_REJECTOVER_TIME = "approval/rejectovertime";
    /**
     * 待我审批：驳回(公出)
     */
    public static final String APPROVAL_TURNEDDOWN_BUSINESS = "approval/turneddownbusiness";
    /**
     * 待我审批：驳回（办公用品）
     */
    public static final String APPROVAL_TURNED_DOWN_OFFICE = "approval/turneddownoffice";
    /**
     * 待我审批：驳回（公告）
     */
    public static final String APPROVAL_TURNED_DOWN_NOTICES = "approval/turneddownnotices";
    /**
     * 待我审批：通过(请假)
     */
    public static final String APPROVAL_ACCESS_OFFWORK = "approval/accessoffwork";
    /**
     * 待我审批：通过（办公用品）
     */
    public static final String APPROVAL_ACCESS_OFFICE = "approval/accessoffice";
    /**
     * 待我审批：通过（出差）
     */
    public static final String APPROVAL_ACCESS_TRIP = "approval/accesstrip";
    /**
     * 待我审批：通过（公告）
     */
    public static final String APPROVAL_ACCESS_NOTICE = "approval/accessnotices";
    /**
     * 待我审批：通过（加班）
     */
    public static final String APPROVAL_ACCESSOVER_TIME = "approval/accessovertime";
    /**
     * 待我审批：通过（公出）
     */
    public static final String APPROVAL_ACCESS_BUSINESS = "approval/accessbusiness";
    /**
     * 会议室详情接口
     */
    public static final String  CONFERENCE_CONFERENCEINFO="conference/conferenceinfo";
    /**
     * 获取审批人
     */
    public static final String GET_APPROVERS = "approval/getapprovers";
    /**
     * 审批详情(请假、差旅、办公用品、公出)
     */
    public static final String APPROVAL_INFO = "approval/approvalinfo";
    /**
     * 审批详情（公告）
     */
    public static final String APPROVAL_NOTICE_INFO = "approval/detailnotice";
    /**
     * 审批详情（加班）
     */
    public static final String APPROVAL_APPROVALINFO = "approval/approvalinfo";
    /**
     * 记事本详情
     */
    public static final String NOTE_DETAIL = "note/detail";
    /**
     * 修改记事本
     */
    public static final String NOTE_EDIT = "note/edit";
    /**
     * 创建记事本
     */
    public static final String NOTE_ADD = "note/add";
    /**
     * 记事本详情
     */
    public static final String NOTE_DELETE = "note/delete";
    /**
     * 添加会议
     */
    public static final String CONFERENCE_SETCONF = "conference/setconf";
    /**
     * 选择会议
     */
    public static final String CONFERENCE_SELECTMEETINGROOM = "conference/selectmeetingroom";

    /**
     * 查看会议详情的时间 _user字段
     */
    public static final String CONFERENCE_CONFVIEW_USERS = "conference/confview_users";
    /**
     * 返回会议室使用情况
     */
    public static final String CONFERENCE_SELECTMEETROOM = "conference/selectmeetroom";
    /**
     * 查看会议详情的时间 _会议室字段
     */
    public static final String CONFERENCE_CONFVIEW_DATETIME = "conference/confview_datetime";
    /**
     * 选择是否参会
     */
    public static final String CONFERENCE_IFATTENDANCE = "conference/ifattendance";
    /**
     * 判断
     */
    public static final String CONFERENCE_JUDGE = "conference/judge";
    /**
     * 取消会议
     */
    public static final String CONFERENCE_CANCELCONF = "conference/cancelconf";
    /**
     * 办公用品
     */
    public static final String POSTOFFICE_APPROVAL = "approval/postofficeapproval";
    /**
     * 加班申请
     */
    public static final String POST_OVERTIME = "approval/postovertime";
    /**
     * 请假申请
     */
    public static final String POSTOFF_WORK_APPROVAL = "approval/postoffworkapproval";
    /**
     * 公出申请
     */
    public static final String POSTOFF_POSTOUT_FORBUSINESS = "approval/postoutforbusiness";
    /**
     * 差旅请假  approval/posttripapproval
     */
    public static final String POST_TRIPA_PPROVAL = "approval/posttripapproval";
    /**
     * 系统消息接口
     */
    public static final String SYSTEM_NOTICE = "message/systemnotices";

    /**
     * 系统消息全部已读接口
     */
    public static final String SYSTEM_NOTICE_ALL_READ = "message/readallsystemnotices";
    /**
     * 工作汇报已读接口
     */
    public static final String REPORT_HAD_READ = "report/hide";
    /**
     * 通知公告已读接口
     */
    public static final String NOTICE_HAD_READ = "message/read";

    /**
     * 使用帮助
     */
    public static final String USINGHELP = "helper/index";

    /**
     * 更新APP
     */
    public static final String UPDATE_APP = "updatehelper/readlog";
    /**
     * 推送消息列表接口
     */
    public static final String MESSAGE_PUSHS = "message/pushs";
    /**
     * 日程安排接口
     */
    public static final String AGENDA_INDEX = "agenda/index";
    /**
     * 环信收到消息调用极光推送接口
     */
    public static final String PUSH_HUANXIN = "push/huanxinpush";
    /**
     * 变更加班申请
     */
    public static final String APPROVAL_EDITOVERTIME = "approval/editovertime";

    /**
     * 聊天个人信息
     */
    public static final String COMMUNICATION_USERINFO = "user/detail";

    // 获取验证码
    public static final String SENDS_CAPTCHA = "/sends/captcha?refresh";

    // 查询工号信息
    public static final String USERS_SEARCH = "/users/search?code=";

    //*****************************************************************//

    /**
     * 获取接口返回Code的值
     *
     * @param jsonObject 接口返回数据
     * @return code
     * @throws JSONException
     */
    public static int getCode(JSONObject jsonObject) throws JSONException {
        return Integer.parseInt(jsonObject.getString(RESPONSES_KEY_CODE));
    }

    /**
     * 获取接口返回info的值-字符串
     *
     * @param jsonObject 接口返回数据
     * @return info
     * @throws JSONException
     */
    public static String getInfo(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(RESPONSES_KEY_INFO);
    }

    /**
     * 获取接口返回data的值-JSONObject
     *
     * @param jsonObject 接口返回数据
     * @return data
     * @throws JSONException
     */
    public static JSONObject getDataToJSONObject(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(RESPONSES_KEY_DATA);
    }

    /**
     * 获取接口返回data的值-JSONArray
     *
     * @param jsonObject 接口返回数据
     * @return data
     * @throws JSONException
     */
    public static JSONArray getDataToJSONArray(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONArray(RESPONSES_KEY_DATA);
    }
}