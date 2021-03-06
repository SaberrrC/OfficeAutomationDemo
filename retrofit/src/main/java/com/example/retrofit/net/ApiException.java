package com.example.retrofit.net;


public class ApiException extends Exception {

    public static final String USER_NOT_EXIST = "sd";
    public static final String WRONG_PASSWORD = "sd";
    int mType = 0;
    public static String mMessage = "";
    public static String code = "";

    public ApiException(String resultCode,int type,String message) {
        mType=type;
        mMessage=message;
    }

    public  ApiException(){
    }



    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @return
     */
    public  String getMessage(){
        return mMessage;
    }

    public   String getCode(){
        return code;
    }

}

