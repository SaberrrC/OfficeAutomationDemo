package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.WeekCalendarBean;
import com.shanlinjinrong.oa.utils.CalendarUtils;
import com.shanlinjinrong.oa.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestAdapter extends BaseQuickAdapter<WeekCalendarBean> {

    private DateTime mInitialDateTime;
    private int mCurrPage;
    private String mCurrentDay;
    private List<WeekCalendarBean> mData;

    public TestAdapter(List<WeekCalendarBean> data, String currentDay, DateTime initialDateTime, int currPage) {
        super(R.layout.item_week_calendar, data);
        mInitialDateTime = initialDateTime;
        mCurrPage = currPage;
        mCurrentDay = currentDay;
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WeekCalendarBean weekCalendarBean) {

        DateTime dateTime = mInitialDateTime.plusDays((baseViewHolder.getPosition() - mCurrPage) * 7);

        CalendarUtils.NCalendar weekCalendar2 = CalendarUtils.getWeekCalendar2(dateTime, 1);

        List<DateTime> dateTimeList = weekCalendar2.dateTimeList;


        List<TextView> textDays = new ArrayList<>();
        List<TextView> textWeeks = new ArrayList<>();
        List<ImageView> textIcon = new ArrayList<>();
        List<FrameLayout> frameLayouts = new ArrayList<>();

        textIcon.add(baseViewHolder.getView(R.id.iv_icon1));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon2));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon3));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon4));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon5));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon6));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon7));

        textDays.add(baseViewHolder.getView(R.id.tv_day1));
        textDays.add(baseViewHolder.getView(R.id.tv_day2));
        textDays.add(baseViewHolder.getView(R.id.tv_day3));
        textDays.add(baseViewHolder.getView(R.id.tv_day4));
        textDays.add(baseViewHolder.getView(R.id.tv_day5));
        textDays.add(baseViewHolder.getView(R.id.tv_day6));
        textDays.add(baseViewHolder.getView(R.id.tv_day7));

        textWeeks.add(baseViewHolder.getView(R.id.tv_week1));
        textWeeks.add(baseViewHolder.getView(R.id.tv_week2));
        textWeeks.add(baseViewHolder.getView(R.id.tv_week3));
        textWeeks.add(baseViewHolder.getView(R.id.tv_week4));
        textWeeks.add(baseViewHolder.getView(R.id.tv_week5));
        textWeeks.add(baseViewHolder.getView(R.id.tv_week6));
        textWeeks.add(baseViewHolder.getView(R.id.tv_week7));

        frameLayouts.add(baseViewHolder.getView(R.id.fl_container1));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container2));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container3));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container4));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container5));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container6));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container7));


        String[] weeks = {"一", "二", "三", "四", "五", "六", "日"};
        for (int i = 0; i < 7; i++) {
            textDays.get(i).setText(dateTimeList.get(i).toLocalDate().toString().substring(dateTimeList.get(i).toLocalDate().toString().length() - 2, dateTimeList.get(i).toLocalDate().toString().length()));
            textWeeks.get(i).setText(weeks[i]);

            if (dateTimeList.get(i).toLocalDate().toString().substring(dateTimeList.get(i).toLocalDate().toString().length() - 2, dateTimeList.get(i).toLocalDate().toString().length()).equals(mCurrentDay) && weekCalendarBean.isFirst()) {
                weekCalendarBean.getIsSelected().set(i, true);
                weekCalendarBean.setFirst(false);
            }


            if (weekCalendarBean.getIsSelected().get(i)) {
                textIcon.get(i).setVisibility(View.VISIBLE);
                textDays.get(i).setTextColor(mContext.getResources().getColor(R.color.white));

            } else {
                textIcon.get(i).setVisibility(View.INVISIBLE);
                textDays.get(i).setTextColor(mContext.getResources().getColor(R.color.gray_normal));

            }
            int finalI = i;
            frameLayouts.get(i).setOnClickListener(view -> {
                Observable.create(e -> {
                    for (int j = 0; j < mData.size(); j++) {
                        for (int k = 0; k < 7; k++) {
                            mData.get(j).getIsSelected().set(k, false);
                        }
                    }
                    e.onComplete();
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                        }, Throwable::printStackTrace, () -> {
                            weekCalendarBean.getIsSelected().set(finalI, true);
                            String date = DateUtils.getBiDisplayDateByTimestamp(DateUtils.getTimestampFromString(dateTimeList.get(finalI).toLocalDate().toString(), "yyyy-MM-dd"));
                            EventBus.getDefault().post(new SelectedWeekCalendarEvent(baseViewHolder.getPosition(), finalI, date, "TopDate"));
                        });
            });
        }
    }
}
