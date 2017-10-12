package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import java.util.List;

/**
 * Created by tonny on 2017/9/30.
 */

public class ReservationRecordBean {

    /**
     * code : 200
     * info : success
     * data : [{"id":10,"uid":1,"title":"1","content":"1","start_time":1,"end_time":1,"meeting_place":"1","room_id":1,"send_type":"1","created":1,"part_name":false},{"id":1,"uid":2,"title":"2","content":"2","start_time":2,"end_time":2,"meeting_place":"2","room_id":2,"send_type":"2","created":2,"part_name":false}]
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
         * id : 10
         * uid : 1
         * title : 1
         * content : 1
         * start_time : 1
         * end_time : 1
         * meeting_place : 1
         * room_id : 1
         * send_type : 1
         * created : 1
         * part_name : false
         */

        private int id;
        private int uid;
        private String title;
        private String content;
        private int start_time;
        private int end_time;
        private String meeting_place;
        private int room_id;
        private String send_type;
        private int created;
        private boolean part_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
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

        public String getMeeting_place() {
            return meeting_place;
        }

        public void setMeeting_place(String meeting_place) {
            this.meeting_place = meeting_place;
        }

        public int getRoom_id() {
            return room_id;
        }

        public void setRoom_id(int room_id) {
            this.room_id = room_id;
        }

        public String getSend_type() {
            return send_type;
        }

        public void setSend_type(String send_type) {
            this.send_type = send_type;
        }

        public int getCreated() {
            return created;
        }

        public void setCreated(int created) {
            this.created = created;
        }

        public boolean isPart_name() {
            return part_name;
        }

        public void setPart_name(boolean part_name) {
            this.part_name = part_name;
        }
    }
}
