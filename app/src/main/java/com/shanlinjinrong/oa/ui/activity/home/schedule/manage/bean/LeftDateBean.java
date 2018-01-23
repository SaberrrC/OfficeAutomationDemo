package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean;

import android.util.SparseArray;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/16
 * 功能描述：
 */

public class LeftDateBean implements MultiItemEntity {
    private String date;

    private int itemType;

    private int position;

    private boolean isSelected;

    private SparseArray<LeftDateBean.DataBean> data;

    public SparseArray<DataBean> getData() {
        return data;
    }

    public void setData(SparseArray<DataBean> data) {
        this.data = data;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getItemType() {
        return itemType;
    }


    public static class DataBean {

        private String title;

        private String contentCount;

        private String content;

        private String date;

        private int startTime;

        private int endTime;

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getStartTime() {
            return startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContentCount() {
            return contentCount;
        }

        public void setContentCount(String contentCount) {
            this.contentCount = contentCount;
        }
    }
}
