package com.example.retrofit.model;

import java.io.Serializable;

/**
 * Created by dell on 2017/12/28.
 */

public class UpLoadPortraitsBean implements Serializable {
    private String portrait;

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
