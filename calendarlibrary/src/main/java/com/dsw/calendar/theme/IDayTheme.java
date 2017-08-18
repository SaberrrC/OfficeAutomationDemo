package com.dsw.calendar.theme;

/**
 * Created by Administrator on 2016/7/30.
 */
public interface IDayTheme {
    /**
     * 选中日期的背景色
     * @return 16进制颜色值 hex color
     */
      int colorSelectBG();

    /**
     * 选中日期的颜色
     * @return 16进制颜色值 hex color
     */
      int colorSelectDay();

    /**
     * 今天日期颜色
     * @return 16进制颜色值 hex color
     */
      int colorToday();

    /**
     * 日历的整个背景色
     * @return
     */
      int colorMonthView();

    /**
     * 工作日的颜色
     * @return
     */
      int colorWeekday();

    /**
     * 周末的颜色
     * @return
     */
      int colorWeekend();

    /**
     * 事务装饰颜色
     * @return  16进制颜色值 hex color
     */
      int colorDecor();

    /**
     * 假日颜色
     * @return 16进制颜色值 hex color
     */
      int colorRest();

    /**
     * 班颜色
     * @return  16进制颜色值 hex color
     */
      int colorWork();

    /**
     * 描述文字颜色
     * @return 16进制颜色值 hex color
     */
      int colorDesc();

    /**
     * 日期大小
     * @return
     */
      int sizeDay();

    /**
     * 描述文字大小
     * @return
     */
      int sizeDesc();

    /**
     * 装饰器大小
     * @return
     */
      int sizeDecor();
    /**
     * 日期高度
     * @return
     */
      int dateHeight();

    /**
     * 线条颜色
     * @return
     */
      int colorLine();

    /**
     * 滑动模式  0是渐变滑动方式，1是没有滑动方式
     * @return
     */
      int smoothMode();
}
