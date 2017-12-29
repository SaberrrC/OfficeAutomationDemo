package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean;

import java.util.List;

/**
 * Created by tonny on 2017/9/30.
 */

public class ReservationRecordBean {

    /**
     * code : 000000
     * message : success
     * data : {"hasPrevious":true,"hasNext":false,"pageCount":8,"pageNum":8,"pageSize":1,"total":8,"data":[{"id":429,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453412,"start_time":1514595600,"end_time":1514599200,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":430,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453436,"start_time":1514437200,"end_time":1514440800,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":426,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453358,"start_time":1514433600,"end_time":1514437200,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":428,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453395,"start_time":1514430000,"end_time":1514433600,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":427,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453386,"start_time":1514426400,"end_time":1514430000,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":423,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453016,"start_time":1514422800,"end_time":1514426400,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":424,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453018,"start_time":1514422800,"end_time":1514426400,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":425,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453066,"start_time":1514422800,"end_time":1514426400,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"}]}
     */

    private String code;
    private String    message;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * hasPrevious : true
         * hasNext : false
         * pageCount : 8
         * pageNum : 8
         * pageSize : 1
         * total : 8
         * data : [{"id":429,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453412,"start_time":1514595600,"end_time":1514599200,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":430,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453436,"start_time":1514437200,"end_time":1514440800,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":426,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453358,"start_time":1514433600,"end_time":1514437200,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":428,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453395,"start_time":1514430000,"end_time":1514433600,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":427,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453386,"start_time":1514426400,"end_time":1514430000,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":423,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453016,"start_time":1514422800,"end_time":1514426400,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":424,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453018,"start_time":1514422800,"end_time":1514426400,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"},{"id":425,"uid":50363,"title":"","content":"￦ﾚﾂ￦ﾗﾠ","created":1514453066,"start_time":1514422800,"end_time":1514426400,"room_id":2,"meetingPlace":"星创5F","send_type":"null","part_name":[],"roomname":"火星"}]
         */

        private boolean hasPrevious;
        private boolean        hasNext;
        private int            pageCount;
        private int            pageNum;
        private int            pageSize;
        private int            total;
        private List<DataBean> data;

        public boolean isHasPrevious() {
            return hasPrevious;
        }

        public void setHasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 429
             * uid : 50363
             * title :
             * content : ￦ﾚﾂ￦ﾗﾠ
             * created : 1514453412
             * start_time : 1514595600
             * end_time : 1514599200
             * room_id : 2
             * meetingPlace : 星创5F
             * send_type : null
             * part_name : []
             * roomname : 火星
             */

            private int id;
            private int     uid;
            private String  title;
            private String  content;
            private int     created;
            private int     start_time;
            private int     end_time;
            private int     room_id;
            private String  meetingPlace;
            private String  send_type;
            private String  roomname;
            private List<?> part_name;

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

            public String getMeetingPlace() {
                return meetingPlace;
            }

            public void setMeetingPlace(String meetingPlace) {
                this.meetingPlace = meetingPlace;
            }

            public String getSend_type() {
                return send_type;
            }

            public void setSend_type(String send_type) {
                this.send_type = send_type;
            }

            public String getRoomname() {
                return roomname;
            }

            public void setRoomname(String roomname) {
                this.roomname = roomname;
            }

            public List<?> getPart_name() {
                return part_name;
            }

            public void setPart_name(List<?> part_name) {
                this.part_name = part_name;
            }
        }
    }
}
