package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import java.util.List;

/**
 * Created by tonny on 2017/10/11.
 */

public class MeetingRoomsBean {
    /**
     * code : 200
     * info : success
     * data : [{"room_id":2,"roomname":"1","address":"1","device":"1","nop":1,"isuse":101,"roomimg":"/group1/M00/00/00/rBDJOFnPW0mAffEXAAvqH_kipG8765.jpg"},{"room_id":3,"roomname":"3","address":"32","device":"23","nop":3332,"isuse":123,"roomimg":null},{"room_id":4,"roomname":"3","address":"32","device":"23","nop":3332,"isuse":123,"roomimg":null},{"room_id":5,"roomname":"3","address":"32","device":"23","nop":3332,"isuse":32,"roomimg":null},{"room_id":6,"roomname":"3","address":"32","device":"23","nop":3332,"isuse":32,"roomimg":null}]
     */

    private int code;
    private String info;
    private List<DataBean> data;

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
         * room_id : 2
         * roomname : 1
         * address : 1
         * device : 1
         * nop : 1
         * isuse : 101
         * roomimg : /group1/M00/00/00/rBDJOFnPW0mAffEXAAvqH_kipG8765.jpg
         */

        private int room_id;
        private String roomname;
        private String address;
        private String device;
        private int nop;
        private int isuse;
        private String roomimg;

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

        public String getRoomimg() {
            return roomimg;
        }

        public void setRoomimg(String roomimg) {
            this.roomimg = roomimg;
        }
    }
}
