package com.shanlinjinrong.oa.common;

/**
 * Created by lvdinghao on 2017/8/24.
 * java部署的服务器的接口
 */
public class ApiJava {
    //新接口的域名
    public static final String BASE_URL = "http://106.15.205.215:8084/";

    //发起日报
    public static final String DAILY_REPORT = "dailyreport";

    //当前登录用户获取其leader
    public static final String GET_CURRENT_LEADER = "user/getCurrentLeader";

    //根据当前登录用户获取当前部门用户列表，并支持姓名模糊搜索
    public static final String SAME_ORGANIZATION = "user/query/sameOrganization";


}
