package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by tonny on 2017/9/30.
 */

public class ReservationRecordBean {


    /**
     * code : 200
     * info : success
     * data : [{"id":13,"uid":618,"title":"1212","content":"萨达","start_time":1507852800,"end_time":1507903200,"meeting_place":"32","room_id":3,"send_type":"邮件","created":1507779610,"part_name":"傅梦婷,邱晓嬿"},{"id":12,"uid":618,"title":"1212","content":"萨达","start_time":1508630400,"end_time":1507903200,"meeting_place":"32","room_id":3,"send_type":"邮件","created":1507779218,"part_name":"顾震平,齐娜,姚嵩庆"}]
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

    public static class DataBean implements MultiItemEntity {
        /**
         * id : 13
         * uid : 618
         * title : 1212
         * content : 萨达
         * start_time : 1507852800
         * end_time : 1507903200
         * meeting_place : 32
         * room_id : 3
         * send_type : 邮件
         * created : 1507779610
         * part_name : 傅梦婷,邱晓嬿
         */

        private int id;
        private int uid;
        private String title;
        private String content;
        private String start_time;
        private String end_time;
        private String meeting_place;
        private int room_id;
        private String send_type;
        private String created;
        private String part_name;
        private String roomname;
        private int itemType;

        public String getRoomname() {
            return roomname;
        }

        public void setRoomname(String roomname) {
            this.roomname = roomname;
        }

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

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getPart_name() {
            return part_name;
        }

        public void setPart_name(String part_name) {
            this.part_name = part_name;
        }

        public void setItemType(int type) {
            this.itemType = type;
        }


        @Override
        public int getItemType() {
            return itemType;
        }
    }
}
