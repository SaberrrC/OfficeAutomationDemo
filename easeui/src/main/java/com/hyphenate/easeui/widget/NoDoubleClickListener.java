package com.hyphenate.easeui.widget;

import android.view.View;

import java.util.Calendar;

public abstract class NoDoubleClickListener implements View.OnClickListener {
    public  long MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime        = 0;

    public NoDoubleClickListener() {
    }

    public NoDoubleClickListener(long minClickDelayTime) {
        this.MIN_CLICK_DELAY_TIME = minClickDelayTime;
    }

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
        lastClickTime = currentTime;
    }

    public abstract void onNoDoubleClick(View v);
}
