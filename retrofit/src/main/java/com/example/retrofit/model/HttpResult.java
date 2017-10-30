package com.example.retrofit.model;

/**
 * Created by gaobin on 2017/8/14.
 */
public class HttpResult<T>  extends BaseResult<T>  {

    private int t;
    private int code;
    private String message;
    private T data;
    private T payload;

    public int getCode() {
        return code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("code=" + code + " msg=" + message);
        if (null != data) {
            sb.append(" result:" + data.toString());
        }
        return sb.toString();
    }
}
