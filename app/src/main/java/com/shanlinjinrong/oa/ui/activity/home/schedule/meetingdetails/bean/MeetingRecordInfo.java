package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import java.util.List;

/**
 * Created by tonny on 2017/10/15.
 */

public class MeetingRecordInfo {


    /**
     * code : 000000
     * message : success
     * data : {"id":440,"title":"￦ﾵﾋ￨ﾯﾕ","content":"￦ﾚﾂ￦ﾗﾠ","created":1514518644,"start_time":1514516400,"end_time":1514520000,"room_id":2,"roomname":"火星","meeting_place":"星创5F","nop":20,"device":"投影仪","isuse":1,"send_user":"杨佳晨","uid":50363,"send_type":"￦ﾶﾈ￦ﾁﾯ","part_name":[{"id":46636,"username":"崔娜"}]}
     */

    private String code;
    private String   message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 440
         * title : ￦ﾵﾋ￨ﾯﾕ
         * content : ￦ﾚﾂ￦ﾗﾠ
         * created : 1514518644
         * start_time : 1514516400
         * end_time : 1514520000
         * room_id : 2
         * roomname : 火星
         * meeting_place : 星创5F
         * nop : 20
         * device : 投影仪
         * isuse : 1
         * send_user : 杨佳晨
         * uid : 50363
         * send_type : ￦ﾶﾈ￦ﾁﾯ
         * part_name : [{"id":46636,"username":"崔娜"}]
         */

        private int id;
        private String             title;
        private String             content;
        private int                created;
        private int                start_time;
        private int                end_time;
        private int                room_id;
        private String             roomname;
        private String             meeting_place;
        private int                nop;
        private String             device;
        private int                isuse;
        private String             send_user;
        private int                uid;
        private String             send_type;
        private List<PartNameBean> part_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getCreated() {
            return created;
        }

        public void setCreated(int created) {
            this.created = created;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

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

        public String getMeeting_place() {
            return meeting_place;
        }

        public void setMeeting_place(String meeting_place) {
            this.meeting_place = meeting_place;
        }

        public int getNop() {
            return nop;
        }

        public void setNop(int nop) {
            this.nop = nop;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public int getIsuse() {
            return isuse;
        }

        public void setIsuse(int isuse) {
            this.isuse = isuse;
        }

        public String getSend_user() {
            return send_user;
        }

        public void setSend_user(String send_user) {
            this.send_user = send_user;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getSend_type() {
            return send_type;
        }

        public void setSend_type(String send_type) {
            this.send_type = send_type;
        }

        public List<PartNameBean> getPart_name() {
            return part_name;
        }

        public void setPart_name(List<PartNameBean> part_name) {
            this.part_name = part_name;
        }

        public static class PartNameBean {
            /**
             * id : 46636
             * username : 崔娜
             */

            private int id;
            private String username;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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
