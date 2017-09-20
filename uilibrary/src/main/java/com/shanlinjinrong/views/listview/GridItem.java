package com.shanlinjinrong.views.listview;

import android.support.annotation.DrawableRes;

/**
 * Created by 丁 on 2017/9/11.
 * 网格item bean
 */

public class GridItem {
    private String imageUrl;
    private int imageDrawableId;
    private String title;//标题

    public GridItem(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public GridItem(@DrawableRes int imageDrawableId, String title) {
        this.imageDrawableId = imageDrawableId;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public GridItem setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getImageDrawableId() {
        return imageDrawableId;
    }

    public GridItem setImageDrawableId(int imageDrawableId) {
        this.imageDrawableId = imageDrawableId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public GridItem setTitle(String title) {
        this.title = title;
        return this;
    }
}
