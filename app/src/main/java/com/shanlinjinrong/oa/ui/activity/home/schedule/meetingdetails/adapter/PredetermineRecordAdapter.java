package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;

import java.util.List;

/**
 * Created by tonny on 2017/9/30.
 */

public class PredetermineRecordAdapter extends BaseQuickAdapter<String> {


    public PredetermineRecordAdapter(List<String> data) {
        super(R.layout.meeting_predetermine_record_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_predetermine_record_time,s);
    }
}
