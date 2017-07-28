package com.hyphenate.easeui.utils;

import android.content.Context;

/**
 * 概述：工具类
 * Created by KevinMeng on 2016/5/30.
 */
public class UiUtils {


    /**
     * dp转px
     */
    public static int dip2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5);
    }


}