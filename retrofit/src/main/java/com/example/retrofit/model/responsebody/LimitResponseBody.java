package com.example.retrofit.model.responsebody;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SaberrrC on 2018-1-25.
 {
 "code": "000000",
 "message": "查询用户权限成功",
 "data": [
 {
 "id": "10",
 "name": "日程管理",
 "level": "1",
 "parentId": "1",
 "url": "meeting_admin",
 "sort": 4
 }
 ]
 }
 */

public class LimitResponseBody implements Serializable{

    private String code;
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

    public static class DataBean implements Serializable{
        /**
         * id : 10
         * name : 日程管理
         * level : 1
         * parentId : 1
         * url : meeting_admin
         * sort : 4
         */

        private String id;
        private String name;
        private String level;
        private String parentId;
        private String url;
        private int    sort;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }
}
