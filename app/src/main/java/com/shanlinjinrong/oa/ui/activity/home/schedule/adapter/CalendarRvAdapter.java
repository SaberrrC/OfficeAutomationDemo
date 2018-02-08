package com.shanlinjinrong.oa.ui.activity.home.schedule.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.ScheduleRvItemData;
import com.shanlinjinrong.oa.utils.LogUtils;

import java.util.List;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.adapter
 * Author:Created by Tsui on Date:2016/12/14 15:19
 * Description:日历条目数据
 */
public class CalendarRvAdapter extends BaseQuickAdapter<ScheduleRvItemData> {
    String type = null;

    public CalendarRvAdapter(List<ScheduleRvItemData> data, String type) {
        super(R.layout.calendar_rv_item, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, ScheduleRvItemData schedule) {
        if (type.equals("3")) {
//            LogUtils.e("type.equals(\"3\")");
            holder.getView(R.id.tv_date).setVisibility(View.GONE);
            holder.getView(R.id.tv_room).setVisibility(View.GONE);
            holder.setText(R.id.tv_theme, schedule.getTheme());
        } else {
            if (schedule.getTime().equals("00:00~00:00")) {
                holder.setText(R.id.tv_date, schedule.getDate());
            } else {
                holder.setText(R.id.tv_date, schedule.getTime());

            }
            if (schedule.getRoomname().equals("无")) {
                holder.setText(R.id.tv_room, "不需要会议室");
            } else {
                holder.setText(R.id.tv_room, schedule.getRoomname());
            }

            holder.setText(R.id.tv_theme, schedule.getTheme());
            LogUtils.e(schedule.getTime() + "," + schedule.getRoomname() + ",");
        }

    }
}
