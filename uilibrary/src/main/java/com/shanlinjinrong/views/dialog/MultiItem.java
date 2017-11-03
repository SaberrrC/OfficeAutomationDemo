package com.shanlinjinrong.views.dialog;

/**
 * Created by 丁 on 2017/9/12.
 */

public class MultiItem {
    private String title;
    private boolean isCheck;

    public MultiItem(String title, boolean isCheck) {
        this.title = title;
        this.isCheck = isCheck;
    }


    public String getTitle() {
        return title;
    }

    public MultiItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public MultiItem setCheck(boolean check) {
        isCheck = check;
        return this;
    }
}
