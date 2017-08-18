package com.shanlin.oa.utils;

import android.graphics.Color;

import com.dsw.calendar.theme.IWeekTheme;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.dsw.calendar.utils
 * Author:Created by Tsui on Date:2016/12/14 16:51
 * Description:
 */
public class MyIWeekTheme implements IWeekTheme {
    @Override
    public int colorTopLinen() {
        return 0;
    }

    @Override
    public int colorBottomLine() {
        return 0;
    }

    @Override
    public int colorWeekday() {
        return 0;
    }

    @Override
    public int colorWeekend() {
        return Color.parseColor("#9D9AA2");
    }

    @Override
    public int colorWeekView() {
        return Color.parseColor("#dec9ff");
    }

    @Override
    public int sizeLine() {
        return 0;
    }

    @Override
    public int sizeText() {
        return 0;
    }
}
