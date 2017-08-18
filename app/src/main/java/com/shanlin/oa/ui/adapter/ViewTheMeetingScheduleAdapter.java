package com.shanlin.oa.ui.adapter;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlin.oa.R;
import com.shanlin.oa.model.MeetDatils;

import java.util.List;


public class ViewTheMeetingScheduleAdapter extends BaseMultiItemQuickAdapter<MeetDatils> {


    public ViewTheMeetingScheduleAdapter(List<MeetDatils> data) {
        super(data);
        addItemType(20, R.layout.activity_view_the_meeting_schedule_item);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MeetDatils meetDatils) {
        switch (baseViewHolder.getItemViewType()) {
            case 20:
                baseViewHolder.setText(R.id.meeting_start_time, meetDatils.getBegintime())
                        .setText(R.id.meeting_theme, meetDatils.getTheme())
                        .setText(R.id.meeting_Initiated_people, meetDatils.getUsername())
                        .setText(R.id.next_meet_time, meetDatils.getBegintime()+"-"+meetDatils.getEndtime());
                break;
        }
    }
}
