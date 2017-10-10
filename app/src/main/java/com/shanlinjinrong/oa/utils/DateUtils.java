package com.shanlinjinrong.oa.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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


    /**
     * 获取当前日期之前的所有周一时间
     */
    public static List<String> getMondayData(String pattern) {
        return getMondayData("", pattern);
    }

    /**
     * 获取某个日期到当前日期之间的所有周一时间
     */
    public static List<String> getMondayData(String date, String pattern) {
        List<String> mondays = new ArrayList<>();
        String monday = getCurrentMonday(pattern);
        for (; ; ) {
            monday = getIntervalDate(monday, -7, pattern);
            long l = dateToLong(monday, pattern);
            if (dateToLong(monday, pattern) > dateToLong(date, pattern)) {
                String currentYear = monday.replace("年", "-");
                String currentMonth = currentYear.replace("月", "-");
                String currentDay = currentMonth.replace("日", "");
                mondays.add(currentDay);
            } else {
                break;
            }
        }
        return mondays;
    }

    /**
     * 获取某个日期到当前日期之后的所有周一时间
     */
    public static List<String> getMondayData1(String date, String pattern) {
        List<String> mondays = new ArrayList<>();
        String monday = getCurrentMonday(pattern);
        String currentYear = monday.replace("年", "-");
        String currentMonth = currentYear.replace("月", "-");
        String currentDay = currentMonth.replace("日", "");
        mondays.add(currentDay);
        for (; ; ) {
            monday = getIntervalDate(monday, +7, pattern);
            if (dateToLong(monday, pattern) < dateToLong(date, pattern)) {
                String currentYear1 = monday.replace("年", "-");
                String currentMonth1 = currentYear1.replace("月", "-");
                String currentDay1 = currentMonth1.replace("日", "");
                mondays.add(currentDay1);
            } else {
                break;
            }
        }
        return mondays;
    }

    /**
     * 获取某个日期到当前日期之间的所有周一时间
     */
    public static List<String> getMondayData1(String pattern) {
        List<String> mondays = new ArrayList<>();
        String monday = getCurrentMonday(pattern);
        mondays.add(monday);
        for (int i = 0; i < 52; i++) {
            monday = getIntervalDate(monday, -7, pattern);
            mondays.add(monday);
        }
        return mondays;
    }

    /**
     * 获取某个日期到当前日期之后的所有周一时间
     */
    public static List<String> getMondayData2(String pattern) {
        List<String> mondays = new ArrayList<>();
        String monday = getCurrentMonday(pattern);
        mondays.add(monday);
        for (int i = 0; i < 52; i++) {
            monday = getIntervalDate(monday, +7, pattern);
            mondays.add(monday);
        }
        return mondays;
    }


    /**
     * 获取与某个日期间隔几天的日期
     */
    public static String getIntervalDate(String date, int day, String pattern) {
        if (isDate(date, pattern)) {
            long otherTime = dateToLong(date, pattern) + day * 24 * 60 * 60 * 1000;
            return longToDateString(otherTime, pattern);
        }
        return "";
    }

    /**
     * 获取与某个日期间隔几天的日期
     */
    public static String getIntervalDate1(String date, int day, String pattern) {
        long otherTime = dateToLong(date, pattern) + day * 24 * 60 * 60 * 1000;
        return longToDateString(otherTime, pattern);
    }

    /**
     * 日期转换为时间戳
     */
    public static long dateToLong(String date, String pattern) {
        long time = 0;
        try {
            time = getDateFormat(pattern).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 将时间戳转化为日期字符串
     */
    public static String longToDateString(long time, String pattern) {
        String date = "";
        if (time != 0) {
            try {
                date = getDateFormat(pattern).format(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 判断字符串是否为日期字符串
     */
    public static boolean isDate(String date, String pattern) {
        boolean isDate = false;
        if (date != null) {
            if (StringToDate(date, pattern) != null) {
                isDate = true;
            }
        }
        return isDate;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     */
    public static Date StringToDate(String date, String pattern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(pattern).parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myDate;
    }

    /**
     * 获取当前日期所在的周一
     */
    public static String getCurrentMonday(String pattern) {
        SimpleDateFormat dateFormat = getDateFormat(pattern);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //周一
        Date time = cal.getTime();
        return dateFormat.format(time);
    }

    public static String getDateWeek(String dateMonday, String symbol, String pattern) {
        return dateMonday + symbol + getIntervalDate(dateMonday, 6, pattern);
    }

    @NonNull
    public static SimpleDateFormat getDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA);
    }

    /**
     * 获取当前日期所在的周
     * symbol : 连接的符号
     */
    public static String getCurrentWeek(String symbol, String pattern) {
        SimpleDateFormat dateFormat = getDateFormat(pattern);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //周一
        String monday = dateFormat.format(cal.getTime());
        //周日
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 6);
        String sunday = dateFormat.format(cal.getTime());
        return monday + symbol + sunday;
    }
}
