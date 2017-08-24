package com.shanlinjinrong.oa.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.utils
 * Author:Created by Tsui on Date:2016/12/9 17:22
 * Description:
 */
public class DateUtils {
    /**
     * Tsui添加
     * 把时间向后推迟 ? 时间
     * 格式化日期->yyyy-MM-dd
     *
     * @return 2016-01-01
     */
    public static String getDateByTimestampRetardedTime(String delay) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            String mdate = "";
            Date d = new Date();
            long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化日期显示 <br/>
     *
     * @param
     * @return 时间戳
     */
    public static long getTimestampFromString(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 格式化日期显示 <br/>
     *
     * @param timestamp 时间戳
     * @return 2016-01-01
     */
    public static String getBiDisplayDateByTimestamp(long timestamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new Date(timestamp));
    }

    /**
     * 格式化日期显示 <br/>
     *
     * @param timestamp 时间戳
     * @return 2016-01-01
     */
    public static String getDisplayDateByTimestamp(long timestamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        return sdf.format(new Date(timestamp * 1000));
        return sdf.format(timestamp);
    }


    /**
     * 格式化日期显示 <br/>
     *
     * @param timestamp 时间戳
     * @return 2016/01/01
     */
    public static String getDefDisplayDateByTimestamp(long timestamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        return sdf.format(new Date(timestamp * 1000));
        return sdf.format(timestamp);
    }

    /**
     * 格式化时间显示 <br/>
     * H:mm:ss 24小时制，hh:mm:ss 12小时 <br/><br/>
     *
     * @param timestamp 时间戳
     * @return 12:00:00
     */
    public static String getDisplayTimeByTimestamp(long timestamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    /**
     * @return 获得当天的日期
     */
    public static String getTodayDate() {
        return getTodayDate(true);
    }

    public static String getTodayDate(boolean isDef) {
        if (isDef)
            return getDefDisplayDateByTimestamp(System.currentTimeMillis());
        else
            return getDisplayDateByTimestamp(System.currentTimeMillis());
    }


    /**
     * 根据传入的日期获取 yyyy-MM-dd HH:mm格式的时间
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return format.format(date);
    }

    /**
     * 得到天数为单位的时间差
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getDayD_Value(String beginDate, String endDate) {

        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy/MM/dd");
        long from = 0;
        try {
            from = simpleFormat.parse(beginDate).getTime();
            long to = simpleFormat.parse(endDate).getTime();
            int days = (int) ((to - from) / (1000 * 60 * 60 * 24));
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到小时为单位的时间差
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getHourD_Value(String beginDate, String endDate) {
        //hh小写是12小时制！HH是24小时制,换成小写会出bug
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        long from = 0;
        try {
            from = simpleFormat.parse(beginDate).getTime();
            long to = simpleFormat.parse(endDate).getTime();
            int hours = (int) ((to - from) / (1000 * 60 * 60));
            return hours;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 得到分钟为单位的时间差
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getMinutesD_Value(String beginDate, String endDate) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String fromDate = simpleFormat.format(beginDate);
        String toDate = simpleFormat.format(endDate);
        long from = 0;
        try {
            from = simpleFormat.parse(fromDate).getTime();
            long to = simpleFormat.parse(toDate).getTime();
            int minutes = (int) ((to - from) / (1000 * 60));
            return minutes;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @return 判断时间先后顺序 True,是正确的先后时间
     */
    public static boolean judeDateOrder(String beginTime, String EndTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Date date1 = sdf.parse(beginTime);
            Date date2 = sdf.parse(EndTime);
            LogUtils.e("date1：" + date1);
            LogUtils.e("date2：" + date2);
            LogUtils.e("时间差：" + String.valueOf(date2.getTime() - date1.getTime()));
            if (date2.getTime() - date1.getTime() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * @return 精确到天，判断时间先后顺序 True,是正确的先后时间,
     */
    public static boolean judeDateOrderByDay(String beginTime, String EndTime) {
        LogUtils.e("judeDateOrderByDay->beginTime:" + beginTime + "endTime:" + EndTime);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(beginTime);
            Date date2 = sdf.parse(EndTime);
            LogUtils.e("date1：" + date1);
            LogUtils.e("date2：" + date2);
            LogUtils.e("时间差：" + String.valueOf(date2.getTime() - date1.getTime()));
            if (date2.getTime() - date1.getTime() >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
