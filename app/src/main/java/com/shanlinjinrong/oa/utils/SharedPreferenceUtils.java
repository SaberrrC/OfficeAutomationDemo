package com.shanlinjinrong.oa.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class SharedPreferenceUtils {

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

    /**
     * 存储数据(Int)
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putIntValue(Context context, String name, String key, int value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        sp.putInt(key, value);
        sp.commit();
    }

    /**
     * 存储数据(String)
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putStringValue(Context context, String name, String key, String value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        sp.putString(key, value);
        sp.commit();
    }

    /**
     * 取出数据（int)
     *
     * @param context
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static int getIntValue(Context context, String name, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }

    /**
     * 取出数据（String)
     *
     * @param context
     * @param key
     * @param defValue 默认值
     * @return
     */
    public static String getStringValue(Context context, String name, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }

    /**
     * 清空对应key数据
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String name, String key) {
        SharedPreferences.Editor sp = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        sp.remove(key);
        sp.commit();
    }

    /**
     * 清空所有数据
     *
     * @param context
     */
    public static void clear(Context context, String name) {
        SharedPreferences.Editor sp = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        sp.clear();
        sp.commit();
    }
}
