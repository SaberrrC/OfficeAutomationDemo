package com.example.retrofit.net;

/**
 * Created by zhaojian on 2017/8/14.
 */
public class RetrofitConfig {
    private String privateKey;
    private String apiEnv; // api环境:beta | ga等
    private String apiSignAlgo = "HmacSHA256";// API签名算法
    private String host;
    private String version;
    private String cookieVersion;
    private static String authToken;
    private static String userId;
    private static String cid;

    private boolean isGson;

    // header
    public static final String KYE_SIGNATURE = "signature";// 签名信息
    public static final String KEY_AUTH = "token";// 用户认证信息
    // 数据使用驼峰形式的键
    public static final String KEY_STRINGIFY = "Stringify";// JSON
    public static final String KEY_API_KEY = "apiKey";
    public static final String KEY_ACCEPT = "Accept";
    public static final String KEY_CONTENT_TYPE = "Content-Type";
    public static final String KEY_CONNECTION = "Connection";
    public static final String KEY_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String KEY_CID = "cid";
    public static final String KEY_AGENT = "User-Agent";

    public static final String VALUE_RETURN_JSON = "application/json";
    public static final String VALUE_CONNECTION = "close";
    public static final String VALUE_GZIP = "gzip";

    public static final String KEY_SESSION_TOKEN = "x-sljr-session-token";
    public static final String VALUE_SESSION_TOKEN = "e3c28746c04646808ce22ca03625855a";
    public static final String KEY_PLATFORM = "platform";
    public static final String VALUE_PLATFORM = "android";
    public static final String KEY_IP = "ip";
    public static final String VALUE_IP = "";
    public static final String KEY_DEVICEID = "deviceId";
    public static final String VALUE_DEVICEID = "";

    /**
     * Creates a new instance of Config.
     *
     * @param host
     * @param version
     * @param privateKey API加密的秘钥
     * @param apiEnv     API的使用环境：beta | GA等
     */
    public RetrofitConfig(String host, String version, String privateKey, String apiEnv, boolean isGson) {
        this.privateKey = privateKey;
        this.apiEnv = apiEnv;
        this.host = host;
        this.version = version;
        this.isGson = isGson;
    }

    public String getPrivateKey() {
        return privateKey == null ? "" : privateKey;
    }

    public String getApiEnv() {
        return apiEnv == null ? "" : apiEnv;
    }

    public String getApiAlgo() {
        return this.apiSignAlgo == null ? "" : this.apiSignAlgo;
    }

    public static String getAuthToken() {
        return authToken == null ? "" : authToken;
    }

    public static String getUserId() {
        return userId == null ? "" : userId;
    }

    public static void setAuthToken(String authToken) {
        RetrofitConfig.authToken = authToken;
    }

    public static void setUserId(String userId) {
        RetrofitConfig.userId = userId;
    }

    public String getHost() {
        return host == null ? "" : host;
    }

    public String getVersion() {
        return version == null ? "" : version;
    }

    public static String getCid() {
        return cid == null ? "" : cid;
    }

    public static void setCid(String cid) {
        RetrofitConfig.cid = cid;
    }


    public boolean isGson() {
        return isGson;
    }

    public void setGson(boolean gson) {
        isGson = gson;
    }
}
