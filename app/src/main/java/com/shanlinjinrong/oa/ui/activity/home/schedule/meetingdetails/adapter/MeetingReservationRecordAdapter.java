package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingInfoFillOutActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 会议室预订
 */

public class MeetingReservationRecordAdapter extends BaseQuickAdapter<ReservationRecordBean.DataBean> {

    private Context mContext;
    private boolean mIsUse;
    private List<ReservationRecordBean.DataBean> mData;

    public MeetingReservationRecordAdapter(Context context, List<ReservationRecordBean.DataBean> data) {
        super(R.layout.meeting_reservation_record_list_item, data);
        mContext = context;
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ReservationRecordBean.DataBean recordBean) {
        try {
            final int id = recordBean.getId();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            long currentTime = DateUtils.getTimestampFromString(str, "yyyy年MM月dd日   HH:mm");
            String start_time = recordBean.getStart_time();
            final int room_id = recordBean.getRoom_id();
            long startTime = Long.parseLong(start_time) * 1000L;
            final boolean isMeetingPast = currentTime < startTime;
            if (currentTime > startTime) {
                baseViewHolder.setText(R.id.tv_meeting_state, "已使用");
                baseViewHolder.setTextColor(R.id.tv_meeting_state, Color.rgb(102, 102, 102));
                baseViewHolder.setImageResource(R.id.tvDot, R.drawable.meeting_record_icon_success);
            } else {
                baseViewHolder.setText(R.id.tv_meeting_state, "待使用");
                baseViewHolder.setTextColor(R.id.tv_meeting_state, Color.rgb(85, 187, 255));
                baseViewHolder.setImageResource(R.id.tvDot, R.drawable.meeting_record_icon);
            }

            baseViewHolder.setText(R.id.tv_meeting_room_name, recordBean.getMeeting_place());
            baseViewHolder.setText(R.id.tv_meeting_content, recordBean.getContent());
            baseViewHolder.setText(R.id.tv_accept_time, DateUtils.stringToDate(recordBean.getStart_time()));

            baseViewHolder.setOnClickListener(R.id.rlContent, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MeetingInfoFillOutActivity.class);
                    intent.putExtra("isWriteMeetingInfo", false);
                    intent.putExtra("isMeetingPast", isMeetingPast);
                    intent.putExtra("isMeetingRecord", true);
                    intent.putExtra("roomId", room_id);
                    intent.putExtra("id", id);
                    mContext.startActivity(intent);
//                MeetingReservationRecordActivity.mRecordActivity.finish();
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();

        }
    }
}
