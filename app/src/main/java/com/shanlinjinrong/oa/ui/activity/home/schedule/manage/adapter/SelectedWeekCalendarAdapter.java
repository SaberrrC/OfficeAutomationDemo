package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.WeekCalendarBean;

import java.util.List;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/16
 * 功能描述：
 */

public class SelectedWeekCalendarAdapter extends BaseQuickAdapter<WeekCalendarBean> {

    private int mWidth;

    public SelectedWeekCalendarAdapter(List<WeekCalendarBean> data, int width) {
        super(R.layout.item_week_calendar, data);
        mWidth = width;

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WeekCalendarBean weekCalendarBean) {
        TextView day = baseViewHolder.getView(R.id.tv_day);
        TextView week = baseViewHolder.getView(R.id.tv_week);

        LinearLayout selectedContainer = baseViewHolder.getView(R.id.ll_selected_container);
        selectedContainer.setLayoutParams(new LinearLayout.LayoutParams(mWidth, ViewGroup.LayoutParams.MATCH_PARENT));

        day.setText(weekCalendarBean.getDay());
        week.setText(weekCalendarBean.getWeek());


    }
}
