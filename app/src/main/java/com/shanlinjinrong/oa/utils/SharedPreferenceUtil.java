package com.shanlinjinrong.oa.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lenovo on 2017/7/1.
 */

public class SharedPreferenceUtil {

    public static void setShouldAskPermission(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("value", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getShouldAskPermission(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("value", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

}
