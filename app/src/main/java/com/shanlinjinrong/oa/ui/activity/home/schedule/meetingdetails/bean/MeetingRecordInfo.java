package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import java.util.List;

/**
 * Created by tonny on 2017/10/15.
 */

public class MeetingRecordInfo {

    /**
     * code : 200
     * info : success
     * data : {"id":"41","uid":"618","title":"3","content":"吧","start_time":"1508227200","end_time":"1508230800","meeting_place":"32","room_id":"3","send_type":"邮件,消息","created":"1508228436","send_user":"赵贇","part_name":[{"id":"67874","username":"舒培培"}],"roomname":"3","address":"32","device":"23","nop":"3332","isuse":"123","roomimg":null}
     */

    private int code;
    private String info;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 41
         * uid : 618
         * title : 3
         * content : 吧
         * start_time : 1508227200
         * end_time : 1508230800
         * meeting_place : 32
         * room_id : 3
         * send_type : 邮件,消息
         * created : 1508228436
         * send_user : 赵贇
         * part_name : [{"id":"67874","username":"舒培培"}]
         * roomname : 3
         * address : 32
         * device : 23
         * nop : 3332
         * isuse : 123
         * roomimg : null
         */

        private String id;
        private String uid;
        private String title;
        private String content;
        private String start_time;
        private String end_time;
        private String meeting_place;
        private String room_id;
        private String send_type;
        private String created;
        private String send_user;
        private String roomname;
        private String address;
        private String device;
        private String nop;
        private String isuse;
        private Object roomimg;
        private List<PartNameBean> part_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getMeeting_place() {
            return meeting_place;
        }

        public void setMeeting_place(String meeting_place) {
            this.meeting_place = meeting_place;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getSend_type() {
            return send_type;
        }

        public void setSend_type(String send_type) {
            this.send_type = send_type;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getSend_user() {
            return send_user;
        }

        public void setSend_user(String send_user) {
            this.send_user = send_user;
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

        public String getNop() {
            return nop;
        }

        public void setNop(String nop) {
            this.nop = nop;
        }

        public String getIsuse() {
            return isuse;
        }

        public void setIsuse(String isuse) {
            this.isuse = isuse;
        }

        public Object getRoomimg() {
            return roomimg;
        }

        public void setRoomimg(Object roomimg) {
            this.roomimg = roomimg;
        }

        public List<PartNameBean> getPart_name() {
            return part_name;
        }

        public void setPart_name(List<PartNameBean> part_name) {
            this.part_name = part_name;
        }

        public static class PartNameBean {
            /**
             * id : 67874
             * username : 舒培培
             */

            private String id;
            private String username;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}
