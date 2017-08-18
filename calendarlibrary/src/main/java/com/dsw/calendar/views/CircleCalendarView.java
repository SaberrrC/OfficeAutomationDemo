package com.dsw.calendar.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsw.calendar.R;
import com.dsw.calendar.component.CircleMonthView;
import com.dsw.calendar.component.MonthView;
import com.dsw.calendar.component.WeekView;
import com.dsw.calendar.entity.CalendarInfo;
import com.dsw.calendar.inter.ListenerMove;
import com.dsw.calendar.theme.IDayTheme;
import com.dsw.calendar.theme.IWeekTheme;

import java.util.List;

/**
 * Created by Administrator on 2016/8/7.
 */
public class CircleCalendarView extends LinearLayout implements View.OnClickListener, ListenerMove {
    private final View left;
    private final View right;
    private boolean isCanSet=true;//
    private WeekView weekView;
    private CircleMonthView circleMonthView;
    public TextView textViewYear, textViewMonth;
    ListenerMove listenerMove;
    private int currentYear;
    private int currentMonth;

    public CircleCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams llParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.display_grid_date, null);
        weekView = new WeekView(context, null);
        circleMonthView = new CircleMonthView(context, null);
        addView(view, llParams);
        addView(weekView, llParams);
        addView(circleMonthView, llParams);

        left = view.findViewById(R.id.left);
        left.setOnClickListener(this);
        right = view.findViewById(R.id.right);
        right.setOnClickListener(this);
        textViewYear = (TextView) view.findViewById(R.id.year);
        textViewMonth = (TextView) view.findViewById(R.id.month);

        currentYear=circleMonthView.getSelYear();
        currentMonth=circleMonthView.getSelMonth() + 1;

        textViewYear.setText(currentYear+ "年");
        textViewMonth.setText(currentMonth + "月");

        circleMonthView.setMonthLisener(new MonthView.IMonthLisener() {
            @Override
            public void setTextMonth() {

                textViewYear.setText(circleMonthView.getSelYear() + "年");
                textViewMonth.setText((circleMonthView.getSelMonth() + 1) + "月");

            }
        });
    }

    /**
     * 设置日历点击事件
     *
     * @param dateClick
     */
    public void setDateClick(MonthView.IDateClick dateClick) {
        circleMonthView.setDateClick(dateClick);
    }

    /**
     * 设置星期的形式
     *
     * @param weekString 默认值	"日","一","二","三","四","五","六"
     */
    public void setWeekString(String[] weekString) {
        weekView.setWeekString(weekString);
    }

    public void setCalendarInfos(List<CalendarInfo> calendarInfos) {
        circleMonthView.setCalendarInfos(calendarInfos);
    }

    public void setDayTheme(IDayTheme theme) {
        circleMonthView.setTheme(theme);
    }

    public void setWeekTheme(IWeekTheme weekTheme) {
        weekView.setWeekTheme(weekTheme);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.left) {
            circleMonthView.onLeftClick();
            if (listenerMove != null) {
                listenerMove.leftMove(String.valueOf(currentYear),String.valueOf(currentMonth));
            }
        } else {
            circleMonthView.onRightClick();
            if (listenerMove != null) {
                listenerMove.rightMove(String.valueOf(currentYear),String.valueOf(currentMonth));
            }

        }
    }

    public void setListenerMove(ListenerMove listenerMove) {
        this.listenerMove = listenerMove;
    }

    @Override
    public void leftMove(String currentYear,String currentMonth) {

    }

    @Override
    public void rightMove(String currentYear,String currentMonth) {

    }

    @Override
    public void currentDate(String year, String month) {

    }
}

