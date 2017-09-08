package com.shanlinjinrong.oa.ui.activity.home.approval.bean;

/**
 * Created by 丁 on 2017/9/1.
 * 发起审批页面中的item实体
 */
public class LaunchApprovalItem {
    private String title;//标题

    private int image;

    public LaunchApprovalItem(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


}
