package com.shanlinjinrong.oa.net.remote;

public class HttpConfig {

    //生产
    public static final String JAVA_RELEASE_HOST = "http://oa.shanlinjinrong.com/oa-api/";
    //测试
//    public static final String JAVA_TEST_HOST = "http://testoa.shanlinjinrong.com/oa-api/";
    //开发
    public static final String JAVA_DEVELOP_HOST = "http://118.31.18.67:8090/";
    //新外网
    //public static final String JAVA_TEST_HOST = "http://116.62.216.129:9105/";
    //新内网
    public static final String JAVA_TEST_HOST = "http://10.5.202.122:9105/";


    public static final long CONNECT_TIMEOUT = 10 * 1000;
    public static final long IO_TIMEOUT = 20 * 1000;

    public static final String HTTP_HEADER_TOKEN_KEY = "token";
    public static final String HTTP_HEADER_PLATFORM_KEY = "platform";
    public static final String HTTP_HEADER_PLATFORM_VALUE = "android";
    public static final String HTTP_HEADER_DEVICEID_KEY = "deviceId";
    public static final String HTTP_HEADER_SIGN = "sign";
    public static final String HTTP_USER_ID = "id";

    //请求结果状态妈
    public static final String REQUEST_CODE_OK = "000000"; //返回成功code
    public static final String REQUEST_USER_NOT_EXIST = "010003"; //不存在的用户
    public static final String REQUEST_NOT_LOGIN = "010005"; //用户未登录
    public static final String TOKEN_PAST = "010004";         //token过期
    public static final String REQUEST_TOKEN_NOT_EXIST = "010006"; //token 不存在
    public static final String ERROR_TOKEN = "020005";//token不存在或者用户不存在
    public static final String REQUEST_EXCEL_ERROR = "010007"; //上传文件异常
    public static final String REQUEST_NULL = "010008"; //请求为空
    public static final String REQUEST_NO_RESULT = "020000"; //查询无结果
    public static final String REQUEST_HAD_REPORTED = "030000"; //该天日报以填写
    public static final String REQUEST_REPORT_ID_NULL = "010009"; //日报id空
    public static final String REQUEST_UNKNOW_EX = "999999"; //未知异常
    public static final int CONTENT_EMPETY = -1; //内容为null
}
