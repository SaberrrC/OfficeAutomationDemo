package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

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
    }
}
