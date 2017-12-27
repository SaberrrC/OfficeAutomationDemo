package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import java.util.List;

/**
 * Created by tonny on 2017/10/11.
 */

public class MeetingRoomsBean {
    /**
     * code : 000000
     * message : success
     * data : [{"room_id":2,"roomname":"火星","address":"星创5F","device":"投影仪","nop":20,"isuse":1,"roomimg":"/group1/M00/00/01/rBARm1owwZuEav9JAAAAAFj8hFU100.png"},{"room_id":111,"roomname":"金星","address":"星创5F","device":"投影仪","nop":32,"isuse":1,"roomimg":"/group1/M00/00/01/rBARm1owwiqEM83nAAAAALOqaZI654.jpg"},{"room_id":113,"roomname":"会议室113","address":"星创5F","device":"投影","nop":112,"isuse":0,"roomimg":""},{"room_id":115,"roomname":"会议室3","address":"星创5F","device":"投影","nop":10,"isuse":0,"roomimg":""}]
     */

    private String code;
    private String message;
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

    public static class DataBean {
        /**
         * room_id : 2
         * roomname : 火星
         * address : 星创5F
         * device : 投影仪
         * nop : 20
         * isuse : 1
         * roomimg : /group1/M00/00/01/rBARm1owwZuEav9JAAAAAFj8hFU100.png
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
