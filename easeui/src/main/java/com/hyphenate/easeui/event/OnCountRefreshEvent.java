package com.hyphenate.easeui.event;

/**
 * Created by SaberrrC on 2017-12-21.
 */

public class OnCountRefreshEvent {

    private int event;
    private int unReadCount;
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int getUnReadMsgCount() {
        return unReadCount;
    }

    public void setUnReadMsgCount(int count) {
        unReadCount = count;
    }
}
