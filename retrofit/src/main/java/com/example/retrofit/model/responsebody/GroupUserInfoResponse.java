package com.example.retrofit.model.responsebody;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class GroupUserInfoResponse {

    /**
     * uid : 535
     * code : 010109224
     * username : 周威
     * email : zhouwei@shanlinjinrong.com
     * sex : 男
     * img : /group1/M00/00/04/rBDJzVolJZGAZN3xAAK0r9sQbf8527.jpg
     */

    private int uid;
    private String code;
    private String username;
    private String email;
    private String sex;
    private String img;

    public GroupUserInfoResponse() {
    }

    public GroupUserInfoResponse(int uid, String code, String username, String email, String sex, String img) {
        this.uid = uid;
        this.code = code;
        this.username = username;
        this.email = email;
        this.sex = sex;
        this.img = img;
    }

    public GroupUserInfoResponse(String username) {
        this.username = username;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCode() {
        if (code == null)
            return "";
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        if (username == null)
            username = "";
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        if (email == null)
            return "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        if (sex == null)
            return "";
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImg() {
        if (img == null)
            return "";
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
