package com.dsw.calendar.theme;

import android.graphics.Color;

/**
 * Created by Administrator on 2016/7/30.
 */
public class DefaultDayTheme implements IDayTheme {
    @Override
    public int colorSelectBG() {
        return Color.parseColor("#FE0000");
    }

    @Override
    public int colorSelectDay() {
        return Color.parseColor("#FFFFFF");
    }

    @Override
    public int colorToday() {
        return Color.parseColor("#0EA7ED");
    }

    @Override
    public int colorMonthView() {
        return Color.parseColor("#FFFFFF");
    }

    @Override
    public int colorWeekday() {
        return Color.parseColor("#333333");
    }

    @Override
    public int colorWeekend() {
        return Color.parseColor("#FE0000");
    }


     @Override
    public int colorDecor() {
        return Color.parseColor("#FF0000");
    }

    @Override
    public int colorRest() {
        return Color.parseColor("#68CB00");
    }

    @Override
    public int colorWork() {
        return Color.parseColor("#FF9B12");
    }

    @Override
    public int colorDesc() {
        return Color.parseColor("#FF9B12");
    }

    @Override
    public int sizeDay() {
        return 40;
    }

    @Override
    public int sizeDecor() {
        return 8;
    }

    @Override
    public int sizeDesc() {

        return 0;
    }

    @Override
    public int dateHeight() {
        return 120;
    }

    @Override
    public int colorLine() {
        return Color.parseColor("#FFFFFF");
    }

    @Override
    public int smoothMode() {
        return 0;
    }
}
