package com.hyphenate.easeui.event;

/**
 * Created by SaberrrC on 2017-12-12.
 */

public class OnConversationFinishEvent {

    private String event;

    public OnConversationFinishEvent(String event) {
        this.event = event;
    }

    public OnConversationFinishEvent() {

    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
