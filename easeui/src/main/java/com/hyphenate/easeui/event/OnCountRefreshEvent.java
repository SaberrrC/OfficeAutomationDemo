package com.hyphenate.easeui.event;

/**
 * Created by SaberrrC on 2017-12-21.
 */

public class OnCountRefreshEvent {

    private int unReadCount;

    public int getUnReadMsgCount(){
        return unReadCount;
    }

    public void setUnReadMsgCount(int count){
        unReadCount = count;
    }
}
