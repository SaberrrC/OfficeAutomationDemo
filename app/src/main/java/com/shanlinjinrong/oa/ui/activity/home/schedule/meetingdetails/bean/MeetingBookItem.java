package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import java.util.List;

/**
 * Created by 丁 on 2017/10/16.
 */

public class MeetingBookItem {

    /**
     * code : 000000
     * message : success
     * data : [{"id":414,"uid":84,"title":"","content":"","start_time":1514685600,"end_time":1514692800,"meeting_place":"星创5F","room_id":2,"send_type":"","created":1514268555,"part_name":null,"username":null,"department":null}]
     */

    private String         code;
    private String         message;
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
         * id : 414
         * uid : 84
         * title :
         * content :
         * start_time : 1514685600
         * end_time : 1514692800
         * meeting_place : 星创5F
         * room_id : 2
         * send_type :
         * created : 1514268555
         * part_name : null
         * username : null
         * department : null
         */

        private int    id;
        private int    uid;
        private String title;
        private String content;
        private int    start_time;
        private int    end_time;
        private String meeting_place;
        private int    room_id;
        private String send_type;
        private int    created;
        private Object part_name;
        private Object username;
        private Object department;

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

        public Object getPart_name() {
            return part_name;
        }

        public void setPart_name(Object part_name) {
            this.part_name = part_name;
        }

        public Object getUsername() {
            return username;
        }

        public void setUsername(Object username) {
            this.username = username;
        }

        public Object getDepartment() {
            return department;
        }

        public void setDepartment(Object department) {
            this.department = department;
        }
    }
}
