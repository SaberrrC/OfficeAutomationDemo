package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tonny on 2017/10/11.
 */

public class MeetingRoomsBean {

    /**
     * room_id : 会议室ID
     * roomname : 会议室名字
     * address : 会议室地址
     * device : 设备
     * nop : 3332 人数
     * isuse : 123 是否使用
     * roomimg : null
     */

    private int room_id;
    private String roomname;
    private String address;
    private String device;
    private int nop;
    private int isuse;
    private Object roomimg;
    /**
     * code : 200
     * info : success
     * data : [{"room_id":3,"roomname":"3","address":"32","device":"23","nop":3332,"isuse":123,"roomimg":null},{"room_id":4,"roomname":"3","address":"32","device":"23","nop":3332,"isuse":123,"roomimg":null},{"room_id":5,"roomname":"3","address":"32","device":"23","nop":3332,"isuse":32,"roomimg":null},{"room_id":6,"roomname":"3","address":"32","device":"23","nop":3332,"isuse":32,"roomimg":null}]
     */

    private int code;
    private String info;
    private List<DataBean> data;

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getNop() {
        return nop;
    }

    public void setNop(int nop) {
        this.nop = nop;
    }

    public int getIsuse() {
        return isuse;
    }

    public void setIsuse(int isuse) {
        this.isuse = isuse;
    }

    public Object getRoomimg() {
        return roomimg;
    }

    public void setRoomimg(Object roomimg) {
        this.roomimg = roomimg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * room_id : 3
         * roomname : 3
         * address : 32
         * device : 23
         * nop : 3332
         * isuse : 123
         * roomimg : null
         */

        @SerializedName("room_id")
        private int room_idX;
        @SerializedName("roomname")
        private String roomnameX;
        @SerializedName("address")
        private String addressX;
        @SerializedName("device")
        private String deviceX;
        @SerializedName("nop")
        private int nopX;
        @SerializedName("isuse")
        private int isuseX;
        @SerializedName("roomimg")
        private Object roomimgX;

        public int getRoom_idX() {
            return room_idX;
        }

        public void setRoom_idX(int room_idX) {
            this.room_idX = room_idX;
        }

        public String getRoomnameX() {
            return roomnameX;
        }

        public void setRoomnameX(String roomnameX) {
            this.roomnameX = roomnameX;
        }

        public String getAddressX() {
            return addressX;
        }

        public void setAddressX(String addressX) {
            this.addressX = addressX;
        }

        public String getDeviceX() {
            return deviceX;
        }

        public void setDeviceX(String deviceX) {
            this.deviceX = deviceX;
        }

        public int getNopX() {
            return nopX;
        }

        public void setNopX(int nopX) {
            this.nopX = nopX;
        }

        public int getIsuseX() {
            return isuseX;
        }

        public void setIsuseX(int isuseX) {
            this.isuseX = isuseX;
        }

        public Object getRoomimgX() {
            return roomimgX;
        }

        public void setRoomimgX(Object roomimgX) {
            this.roomimgX = roomimgX;
        }
    }
}
