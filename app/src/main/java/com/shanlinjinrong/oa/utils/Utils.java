package com.shanlinjinrong.oa.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shanlinjinrong.oa.manager.AppManager;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 概述：工具类
 * Created by KevinMeng on 2016/5/30.
 */
public class Utils {

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return true; false;
     */
    public static boolean isNetworkAvailabl(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 验证输入信息是否符合
     *
     * @param pattern 验证规则
     * @param input   需要验证的字符串
     * @return 当条件满足时，将返回true，否则返回false
     */
    public static boolean isRegex(String pattern, String input) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(input);
        return matcher.matches();
    }


    /**
     * dp转px
     */
    public static int dip2px(float dp) {
        float density = AppManager.mContext.getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5);
    }

    /**
     * http://blog.csdn.net/jia635/article/details/51899919
     * @return 获取mac地址
     */
    public static String getMacAddress() {
        try {
            String currentUsedMac = "";
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();

                byte[] addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }

                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                String mac = buf.toString();
//                LogUtils.e("---mac" + "interfaceName=" + iF.getName() + ", mac=" + mac);
                if (iF.getName().equals("wlan0")) {
                    currentUsedMac = mac;
                }
            }
            return currentUsedMac;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }


    /**
     * cxp添加
     * 格式化日期->yyyy-MM-dd
     *
     * @return 2016-01-01
     */
    public static String getDateByTimestamp(long currentTimeMillis) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(currentTimeMillis);
    }


    /**
     * @param input
     * @return 将半角字符转换为全角字符
     */
    public static String ToDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * @param context
     * @return 判断app是否在前台
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (currentPackageName != null && currentPackageName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }
}