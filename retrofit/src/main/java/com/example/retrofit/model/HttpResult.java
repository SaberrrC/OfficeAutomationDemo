package com.example.retrofit.model;

public class HttpResult<T>  extends BaseResult<T>  {

    private String code;
    private String message;
    private T data;

    public String getCode() {
        return code;
    }



    public void setCode(String code) {
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
