package com.shanlinjinrong.oa.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MonthlyCalenderPopItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
     * 获取到当前日期格式
     *
     * @param pattern
     * @return
     */
    public static String getCurrentDate(String pattern) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }


    public static String getOldDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        String format = simpleDateFormat.format(calendar.getTime());
        return format;
    }

    /**
     * 格式化日期显示 <br/>
     *
     * @param
     * @return 时间戳
     */
    public static long getTimestampFromString(String time, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
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
    public static String getDisplayMonthDay(long timestamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
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


    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }


    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getWeek(int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day - 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int calculateDaysInMonth(int year, int month) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] bigMonths = {"1", "3", "5", "7", "8", "10", "12"};
        String[] littleMonths = {"4", "6", "9", "11"};
        List<String> bigList = Arrays.asList(bigMonths);
        List<String> littleList = Arrays.asList(littleMonths);
        // 判断大小月及是否闰年,用来确定"日"的数据
        for (int i = 0; i < bigList.size(); i++) {
            if (bigList.get(i).equals(String.valueOf(month))) {
                return 31;
            }
        }

        for (int i = 0; i < littleList.size(); i++) {
            if (littleList.get(i).equals(String.valueOf(month))) {
                return 30;
            }
        }

        if (year <= 0) {
            return 29;
        }
        // 是否闰年
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return 29;
        } else {
            return 28;
        }
    }


    public static int getCurrentDaysInMonth(int month) {
        int year = getCurrentYear();
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] bigMonths = {"1", "3", "5", "7", "8", "10", "12"};
        String[] littleMonths = {"4", "6", "9", "11"};
        List<String> bigList = Arrays.asList(bigMonths);
        List<String> littleList = Arrays.asList(littleMonths);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (bigList.contains(String.valueOf(month))) {
            return 31;
        } else if (littleList.contains(String.valueOf(month))) {
            return 30;
        } else {
            if (year <= 0) {
                return 29;
            }
            // 是否闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                return 29;
            } else {
                return 28;
            }
        }
    }

    public static List<Integer> getDate(int month) {
        List<Integer> data = new ArrayList<>();
        if (month < 1 || month > 12) {
            return data;
        }
        Calendar cal = Calendar.getInstance();
        int monthDays = calculateDaysInMonth(Calendar.YEAR, month); //获取当月天数
        int lastMonthDays;//上个月的天数
        if (month == 1) {
            lastMonthDays = calculateDaysInMonth(Calendar.YEAR - 1, 11);
        } else {
            lastMonthDays = calculateDaysInMonth(Calendar.YEAR, month - 1);
        }
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        int col = cal.get(Calendar.DAY_OF_WEEK);   //获取该天在本星期的第几天 ，也就是第几列
        for (int i = col - 2; i >= 0; i--) {
            data.add(lastMonthDays - i);
        }

        for (int i = 1; i <= monthDays; i++) {
            data.add(i);
        }

        if (data.size() % 7 != 0) {
            int size = 7 - data.size() % 7;
            for (int i = 1; i <= size; i++) {
                data.add(i);
            }
        }


        return data;
    }

    public static List<PopItem> getDate(int month, int select) {
        List<PopItem> data = new ArrayList<>();
        if (month < 1 || month > 12) {
            return null;
        }
        PopItem item;
        Calendar cal = Calendar.getInstance();
        int curDay = cal.get(Calendar.DAY_OF_MONTH);
        int curMonth = cal.get(Calendar.MONTH);
        int monthDays = calculateDaysInMonth(Calendar.YEAR, month); //获取当月天数
        int lastMonthDays;//上个月的天数
        if (month == 1) {
            lastMonthDays = calculateDaysInMonth(Calendar.YEAR - 1, 11);
        } else {
            lastMonthDays = calculateDaysInMonth(Calendar.YEAR, month - 1);
        }

        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        int col = cal.get(Calendar.DAY_OF_WEEK);   //获取该天在本星期的第几天 ，也就是第几列
        for (int i = col - 2; i >= 0; i--) {
            item = new PopItem("" + (lastMonthDays - i), false, false);
            data.add(item);
        }

        for (int i = 1; i <= monthDays; i++) {
            if (i < curDay && month == curMonth + 1) {
                item = new PopItem("" + i, false, false);
            } else {
                item = new PopItem("" + i, true, false);
            }
            if (select == i) {
                item.setSelect(true);
            }
            data.add(item);
        }


        if (data.size() % 7 != 0) {
            int size = 7 - data.size() % 7;
            for (int i = 1; i <= size; i++) {
                item = new PopItem("" + i, false, false);
                data.add(item);
            }
        }
        return data;
    }

    public static List<PopItem> getAttandenceDate(int year, int month, int select) {
        List<PopItem> data = new ArrayList<>();
        if (month < 1 || month > 12) {
            return null;
        }
        PopItem item;
        Calendar cal = Calendar.getInstance();
        int curDay = cal.get(Calendar.DAY_OF_MONTH);
        int curMonth = cal.get(Calendar.MONTH);
        int monthDays = calculateDaysInMonth(Calendar.YEAR, month); //获取当月天数
        int lastMonthDays;//上个月的天数

        if (month == 1) {
            lastMonthDays = calculateDaysInMonth(Calendar.YEAR - 1, 11);
        } else {
            lastMonthDays = calculateDaysInMonth(Calendar.YEAR, month - 1);
        }

        if (month == 1) {
            month = 12;
            year = year - 1;
        } else {
            month = month - 1;
        }

        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //获取该天在本星期的第几天 ，也就是第几列
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(cal.getTime());

        int col = cal.get(Calendar.DAY_OF_WEEK) - 1;

        if (cal.getFirstDayOfWeek() == Calendar.SUNDAY) {
            if (col == 0) {
                col = 7;
            }
        }


        for (int i = col - 2; i >= 0; i--) {
            item = new PopItem("" + (lastMonthDays - i), false, false);
            data.add(item);
        }

        for (int i = 1; i <= monthDays; i++) {
            if (i < curDay && month == curMonth + 1) {
                item = new PopItem("" + i, true, false);
            } else {
                item = new PopItem("" + i, true, false);
            }
            if (select == i) {
                item.setSelect(true);
            }
            data.add(item);
        }


        if (data.size() % 7 != 0) {
            int size = 7 - data.size() % 7;
            for (int i = 1; i <= size; i++) {
                item = new PopItem("" + i, false, false);
                data.add(item);
            }
        }
        return data;
    }

    public static List<MonthlyCalenderPopItem> getMonthlyScheduleCalendarDate(int year, int month, int select) {
        List<MonthlyCalenderPopItem> data = new ArrayList<>();
        if (month < 1 || month > 12) {
            return null;
        }
        MonthlyCalenderPopItem item;
        Calendar cal = Calendar.getInstance();
        int curDay = cal.get(Calendar.DAY_OF_MONTH);
        int curMonth = cal.get(Calendar.MONTH);
        int monthDays = calculateDaysInMonth(Calendar.YEAR, month); //获取当月天数
        int lastMonthDays;//上个月的天数
        if (month == 1) {
            lastMonthDays = calculateDaysInMonth(Calendar.YEAR - 1, 11);
        } else {
            lastMonthDays = calculateDaysInMonth(Calendar.YEAR, month - 1);
        }
        if (month == 1) {
            month = 12;
            year = year - 1;
        } else {
            month = month - 1;
        }
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //获取该天在本星期的第几天 ，也就是第几列
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(cal.getTime());

        int col = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (cal.getFirstDayOfWeek() == Calendar.SUNDAY) {
            if (col == 0) {
                col = 7;
            }
        }


        for (int i = col - 2; i >= 0; i--) {
            item = new MonthlyCalenderPopItem("" + (lastMonthDays - i), false, false);
            data.add(item);
        }

        for (int i = 1; i <= monthDays; i++) {
            if (i < curDay && month == curMonth + 1) {
                item = new MonthlyCalenderPopItem("" + i, true, false);
            } else {
                item = new MonthlyCalenderPopItem("" + i, true, false);
            }
            if (select == i) {
                item.setSelect(true);
            }
            data.add(item);
        }


        if (data.size() % 7 != 0) {
            int size = 7 - data.size() % 7;
            for (int i = 1; i <= size; i++) {
                item = new MonthlyCalenderPopItem("" + i, false, false);
                data.add(item);
            }
        }
        return data;
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
            if (stringToDate(date, pattern) != null) {
                isDate = true;
            }
        }
        return isDate;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     */
    public static Date stringToDate(String date, String pattern) {
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


    public static String stringToDateTransform(String time, String pattern) {
        SimpleDateFormat sdr = new SimpleDateFormat(pattern);
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    public static String stringToDateTransform(int time, String pattern) {
        SimpleDateFormat sdr = new SimpleDateFormat(pattern);
        @SuppressWarnings("unused")
        long lcc = time;
        int i = time;
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    /**
     * 判断给定字符串时间是否为今日
     */
    public static boolean isToday(long date) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(date);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH) + 1;
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        return year1 == year2 && month1 == month2 && day1 == day2;
    }


    public static boolean isSameDay(long date, Date sameDate) {

        if (null == sameDate) {

            return false;

        }

        Calendar nowCalendar = Calendar.getInstance();

        nowCalendar.setTime(sameDate);

        Calendar dateCalendar = Calendar.getInstance();

        dateCalendar.setTimeInMillis(date);

        if (nowCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)

                && nowCalendar.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)

                && nowCalendar.get(Calendar.DATE) == dateCalendar.get(Calendar.DATE)) {

            return true;

        }

        // if (date.getYear() == sameDate.getYear() && date.getMonth() == sameDate.getMonth()

        // && date.getDate() == sameDate.getDate()) {

        // return true;

        // }

        return false;

    }


    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


    /**
     * 根据 年、月 获取对应的月份 的 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
