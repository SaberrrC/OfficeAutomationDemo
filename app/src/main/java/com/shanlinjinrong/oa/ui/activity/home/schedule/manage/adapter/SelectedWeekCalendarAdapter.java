package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.WeekCalendarBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/16
 * 功能描述：
 */

public class SelectedWeekCalendarAdapter extends BaseQuickAdapter<WeekCalendarBean> {

    private Context                mContext;
    private List<WeekCalendarBean> mData;

    public SelectedWeekCalendarAdapter(List<WeekCalendarBean> data, Context context) {
        super(R.layout.item_week_calendar, data);
        mContext = context;
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WeekCalendarBean weekCalendarBean) {
        List<TextView> textDays = new ArrayList<>();
        List<TextView> textWeeks = new ArrayList<>();
        List<ImageView> textIcon = new ArrayList<>();
        List<FrameLayout> frameLayouts = new ArrayList<>();

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

        textIcon.add(baseViewHolder.getView(R.id.iv_icon1));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon2));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon3));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon4));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon5));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon6));
        textIcon.add(baseViewHolder.getView(R.id.iv_icon7));

        frameLayouts.add(baseViewHolder.getView(R.id.fl_container1));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container2));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container3));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container4));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container5));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container6));
        frameLayouts.add(baseViewHolder.getView(R.id.fl_container7));

        for (int i = 0; i < 7; i++) {
            textDays.get(i).setText(weekCalendarBean.getDay().get(i));
            textWeeks.get(i).setText(weekCalendarBean.getWeek().get(i));
            if (weekCalendarBean.getIsSelected().get(i)) {
                textIcon.get(i).setVisibility(View.VISIBLE);
                textDays.get(i).setTextColor(mContext.getResources().getColor(R.color.white));
                textWeeks.get(i).setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                textIcon.get(i).setVisibility(View.INVISIBLE);
                textDays.get(i).setTextColor(mContext.getResources().getColor(R.color.gray_normal));
                textWeeks.get(i).setTextColor(mContext.getResources().getColor(R.color.gray_normal));
            }

            frameLayouts.get(i).requestFocus();
            frameLayouts.get(i).requestFocusFromTouch();

            int finalI = i;
            frameLayouts.get(i).setOnClickListener(view -> {
                Observable.create(e -> {
                    for (int j = 0; j < mData.size(); j++) {
                        for (int k = 0; k < mData.get(j).getIsSelected().size(); k++) {
                            mData.get(j).getIsSelected().set(k, false);
                        }
                    }
                    e.onComplete();
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                        }, Throwable::printStackTrace, () -> {
                            weekCalendarBean.getIsSelected().set(finalI, true);
                            EventBus.getDefault().post(new SelectedWeekCalendarEvent(baseViewHolder.getPosition(), "TopDate", finalI));
                        });
            });
        }
    }
}
