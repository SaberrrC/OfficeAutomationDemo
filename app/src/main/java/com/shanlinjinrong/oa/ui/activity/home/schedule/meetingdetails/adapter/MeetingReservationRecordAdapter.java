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

import java.util.List;

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

        baseViewHolder.setText(R.id.tv_accept_time,recordBean.getStart_time() + recordBean.getEnd_time());
        baseViewHolder.setText(R.id.tv_meeting_room_name,recordBean.getTitle());
        baseViewHolder.setText(R.id.tv_meeting_content,recordBean.getContent());


        baseViewHolder.setOnClickListener(R.id.rlContent, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MeetingInfoFillOutActivity.class);
                intent.putExtra("isWriteMeetingInfo", false);
                mContext.startActivity(intent);
            }
        });

    }
}
