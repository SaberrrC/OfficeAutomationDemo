package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iflytek.cloud.resource.Resource;
import com.iflytek.cloud.thirdparty.S;
import com.j256.ormlite.stmt.query.In;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingInfoFillOutActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.utils.DateUtils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by tonny on 2017/9/30.
 */

public class MeetingReservationRecordAdapter extends BaseQuickAdapter<ReservationRecordBean.DataBean> {

    private Context mContext;

    public MeetingReservationRecordAdapter(Context context, List<ReservationRecordBean.DataBean> data) {
        super(R.layout.meeting_reservation_record_list_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ReservationRecordBean.DataBean recordBean) {

        final int id = recordBean.getId();
        String todayDate = DateUtils.getTodayDate();

        baseViewHolder.setText(R.id.tv_meeting_room_name, recordBean.getTitle());
        baseViewHolder.setText(R.id.tv_meeting_content, recordBean.getContent());
        baseViewHolder.setText(R.id.tv_accept_time, DateUtils.stringToDate(recordBean.getStart_time()));

        baseViewHolder.setOnClickListener(R.id.rlContent, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MeetingInfoFillOutActivity.class);
                intent.putExtra("isWriteMeetingInfo", false);
                intent.putExtra("id", id);
                mContext.startActivity(intent);
            }
        });
    }




}
