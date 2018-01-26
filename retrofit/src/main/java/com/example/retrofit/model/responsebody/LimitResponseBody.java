package com.example.retrofit.model.responsebody;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SaberrrC on 2018-1-25.
 {
 "code": "000000",
 "message": "查询用户权限成功",
 "data": [
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "1",
 "name": "查看邮箱",
 "level": "1",
 "parentId": null,
 "url": "http://mail.shanlinjinrong.com/"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "10",
 "name": "会议管理",
 "level": "1",
 "parentId": null,
 "url": "meeting_admin"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "101",
 "name": "会议室预定",
 "level": "2",
 "parentId": "10",
 "url": "add"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "102",
 "name": "会议室设置",
 "level": "2",
 "parentId": "10",
 "url": "meetRoomEdit"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "105",
 "name": "我的会议",
 "level": "2",
 "parentId": "10",
 "url": "myMeeting"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "106",
 "name": "会议纪要",
 "level": "2",
 "parentId": "10",
 "url": "meetSummary"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "11",
 "name": "考勤管理",
 "level": "1",
 "parentId": null,
 "url": "attend_admin"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "111",
 "name": "我的考勤",
 "level": "2",
 "parentId": "11",
 "url": "work_attend"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "112",
 "name": "假期查询",
 "level": "2",
 "parentId": "11",
 "url": "leave_query"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "113",
 "name": "薪资查询",
 "level": "2",
 "parentId": "11",
 "url": "salary_query"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "12",
 "name": "人员及其权限设置",
 "level": "1",
 "parentId": null,
 "url": "organization/essential"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "13",
 "name": "审批设置",
 "level": "1",
 "parentId": null,
 "url": "approval/approvalsetting"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "15",
 "name": "用户反馈",
 "level": "1",
 "parentId": null,
 "url": "list"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "2",
 "name": "工作汇报",
 "level": "1",
 "parentId": null,
 "url": "work_report"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "201",
 "name": "发起日报",
 "level": "2",
 "parentId": "2",
 "url": "daily"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "202",
 "name": "发起周报",
 "level": "2",
 "parentId": "2",
 "url": "weekly"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "203",
 "name": "我发起的",
 "level": "2",
 "parentId": "2",
 "url": "my_report"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "204",
 "name": "待评分",
 "level": "2",
 "parentId": "2",
 "url": "no_rate"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "207",
 "name": "历史记录",
 "level": "2",
 "parentId": "2",
 "url": "history"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "208",
 "name": "模板管理",
 "level": "2",
 "parentId": "2",
 "url": "template"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "21",
 "name": "汇报管理",
 "level": "1",
 "parentId": null,
 "url": "report_admin"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "2101",
 "name": "查看全员汇报",
 "level": "2",
 "parentId": "21",
 "url": "whole"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "2102",
 "name": "查看部门汇报",
 "level": "2",
 "parentId": "21",
 "url": "department"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "3",
 "name": "审批流程",
 "level": "1",
 "parentId": null,
 "url": "process"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "301",
 "name": "发起申请",
 "level": "2",
 "parentId": "3",
 "url": "launchIndex"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "302",
 "name": "我的申请",
 "level": "2",
 "parentId": "3",
 "url": "myLaunch"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "303",
 "name": "待办事宜",
 "level": "2",
 "parentId": "3",
 "url": "todo"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "304",
 "name": "已办事宜",
 "level": "2",
 "parentId": "3",
 "url": "haveTodo"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "4",
 "name": "新闻管理",
 "level": "1",
 "parentId": null,
 "url": "news_admin"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "401",
 "name": "发布新闻",
 "level": "2",
 "parentId": "4",
 "url": "add"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "402",
 "name": "新闻列表",
 "level": "2",
 "parentId": "4",
 "url": "all"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "5",
 "name": "公告管理",
 "level": "1",
 "parentId": null,
 "url": "notice"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "501",
 "name": "接收列表",
 "level": "2",
 "parentId": "5",
 "url": "from"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "502",
 "name": "发起列表",
 "level": "2",
 "parentId": "5",
 "url": "to"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "503",
 "name": "部门公告",
 "level": "2",
 "parentId": "5",
 "url": "department"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "504",
 "name": "公司公告",
 "level": "2",
 "parentId": "5",
 "url": "company"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "7",
 "name": "权限设置",
 "level": "1",
 "parentId": null,
 "url": ""
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "701",
 "name": "权限设置",
 "level": "2",
 "parentId": "7",
 "url": "report/reportday?type=7"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "702",
 "name": "人员设置",
 "level": "2",
 "parentId": "7",
 "url": "report/reportday?type=8"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "8",
 "name": "个人中心",
 "level": "1",
 "parentId": null,
 "url": "user_center"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "801",
 "name": "个人中心",
 "level": "2",
 "parentId": "8",
 "url": "basic"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "802",
 "name": "修改密码",
 "level": "2",
 "parentId": "8",
 "url": "password"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "803",
 "name": "修改邮箱",
 "level": "2",
 "parentId": "8",
 "url": "email"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "9",
 "name": "日志管理",
 "level": "1",
 "parentId": null,
 "url": "log_admin"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "901",
 "name": "WEB日志导出",
 "level": "2",
 "parentId": "9",
 "url": "webLogList"
 },
 {
 "userId": "52661",
 "roleId": "73fb27c0-bb53-4e3c-89ef-603d1b6fc871",
 "rightId": "902",
 "name": "API日志导出",
 "level": "2",
 "parentId": "9",
 "url": "apiLogList"
 }
 ]
 }
 */

public class LimitResponseBody implements Serializable{

    /**
     * code : 000000
     * data : [{"level":"1","name":"查看邮箱","rightId":"1","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"http://mail.shanlinjinrong.com/","userId":"52661"},{"level":"1","name":"会议管理","rightId":"10","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"meeting_admin","userId":"52661"},{"level":"2","name":"会议室预定","parentId":"10","rightId":"101","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"add","userId":"52661"},{"level":"2","name":"会议室设置","parentId":"10","rightId":"102","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"meetRoomEdit","userId":"52661"},{"level":"2","name":"我的会议","parentId":"10","rightId":"105","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"myMeeting","userId":"52661"},{"level":"2","name":"会议纪要","parentId":"10","rightId":"106","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"meetSummary","userId":"52661"},{"level":"1","name":"考勤管理","rightId":"11","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"attend_admin","userId":"52661"},{"level":"2","name":"我的考勤","parentId":"11","rightId":"111","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"work_attend","userId":"52661"},{"level":"2","name":"假期查询","parentId":"11","rightId":"112","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"leave_query","userId":"52661"},{"level":"2","name":"薪资查询","parentId":"11","rightId":"113","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"salary_query","userId":"52661"},{"level":"1","name":"人员及其权限设置","rightId":"12","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"organization/essential","userId":"52661"},{"level":"1","name":"审批设置","rightId":"13","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"approval/approvalsetting","userId":"52661"},{"level":"1","name":"用户反馈","rightId":"15","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"list","userId":"52661"},{"level":"1","name":"工作汇报","rightId":"2","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"work_report","userId":"52661"},{"level":"2","name":"发起日报","parentId":"2","rightId":"201","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"daily","userId":"52661"},{"level":"2","name":"发起周报","parentId":"2","rightId":"202","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"weekly","userId":"52661"},{"level":"2","name":"我发起的","parentId":"2","rightId":"203","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"my_report","userId":"52661"},{"level":"2","name":"待评分","parentId":"2","rightId":"204","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"no_rate","userId":"52661"},{"level":"2","name":"历史记录","parentId":"2","rightId":"207","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"history","userId":"52661"},{"level":"2","name":"模板管理","parentId":"2","rightId":"208","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"template","userId":"52661"},{"level":"1","name":"汇报管理","rightId":"21","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"report_admin","userId":"52661"},{"level":"2","name":"查看全员汇报","parentId":"21","rightId":"2101","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"whole","userId":"52661"},{"level":"2","name":"查看部门汇报","parentId":"21","rightId":"2102","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"department","userId":"52661"},{"level":"1","name":"审批流程","rightId":"3","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"process","userId":"52661"},{"level":"2","name":"发起申请","parentId":"3","rightId":"301","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"launchIndex","userId":"52661"},{"level":"2","name":"我的申请","parentId":"3","rightId":"302","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"myLaunch","userId":"52661"},{"level":"2","name":"待办事宜","parentId":"3","rightId":"303","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"todo","userId":"52661"},{"level":"2","name":"已办事宜","parentId":"3","rightId":"304","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"haveTodo","userId":"52661"},{"level":"1","name":"新闻管理","rightId":"4","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"news_admin","userId":"52661"},{"level":"2","name":"发布新闻","parentId":"4","rightId":"401","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"add","userId":"52661"},{"level":"2","name":"新闻列表","parentId":"4","rightId":"402","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"all","userId":"52661"},{"level":"1","name":"公告管理","rightId":"5","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"notice","userId":"52661"},{"level":"2","name":"接收列表","parentId":"5","rightId":"501","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"from","userId":"52661"},{"level":"2","name":"发起列表","parentId":"5","rightId":"502","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"to","userId":"52661"},{"level":"2","name":"部门公告","parentId":"5","rightId":"503","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"department","userId":"52661"},{"level":"2","name":"公司公告","parentId":"5","rightId":"504","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"company","userId":"52661"},{"level":"1","name":"权限设置","rightId":"7","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"","userId":"52661"},{"level":"2","name":"权限设置","parentId":"7","rightId":"701","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"report/reportday?type=7","userId":"52661"},{"level":"2","name":"人员设置","parentId":"7","rightId":"702","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"report/reportday?type=8","userId":"52661"},{"level":"1","name":"个人中心","rightId":"8","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"user_center","userId":"52661"},{"level":"2","name":"个人中心","parentId":"8","rightId":"801","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"basic","userId":"52661"},{"level":"2","name":"修改密码","parentId":"8","rightId":"802","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"password","userId":"52661"},{"level":"2","name":"修改邮箱","parentId":"8","rightId":"803","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"email","userId":"52661"},{"level":"1","name":"日志管理","rightId":"9","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"log_admin","userId":"52661"},{"level":"2","name":"WEB日志导出","parentId":"9","rightId":"901","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"webLogList","userId":"52661"},{"level":"2","name":"API日志导出","parentId":"9","rightId":"902","roleId":"73fb27c0-bb53-4e3c-89ef-603d1b6fc871","url":"apiLogList","userId":"52661"}]
     * message : 查询用户权限成功
     */

    private String code;
    private String         message;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * level : 1
         * name : 查看邮箱
         * rightId : 1
         * roleId : 73fb27c0-bb53-4e3c-89ef-603d1b6fc871
         * url : http://mail.shanlinjinrong.com/
         * userId : 52661
         * parentId : 10
         */

        private String level;
        private String name;
        private String rightId;
        private String roleId;
        private String url;
        private String userId;
        private String parentId;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRightId() {
            return rightId;
        }

        public void setRightId(String rightId) {
            this.rightId = rightId;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
    }
}
