package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.CalendarScheduleContentBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.LeftDateBean;

import java.util.List;


public class MonthContentAdapter extends BaseQuickAdapter<CalendarScheduleContentBean.DataBean> {

    public MonthContentAdapter(List<CalendarScheduleContentBean.DataBean> data) {
        super(R.layout.item_monthly_calendar_content, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CalendarScheduleContentBean.DataBean dataBean) {
        baseViewHolder.setText(R.id.tv_content, dataBean.getTaskTheme());

        if (dataBean.getStatus() == 1) { //完成
            baseViewHolder.setTextColor(R.id.tv_content, Color.parseColor("#999999"));
            baseViewHolder.setBackgroundColor(R.id.tv_content, Color.parseColor("#F5F5F5"));
        } else {
            baseViewHolder.setTextColor(R.id.tv_content, Color.parseColor("#333333"));
            baseViewHolder.setBackgroundColor(R.id.tv_content, Color.parseColor("#7F69B0F2"));
        }
    }
}
