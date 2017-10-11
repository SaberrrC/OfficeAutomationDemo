package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
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

public class MeetingReservationRecordAdapter extends BaseQuickAdapter<ReservationRecordBean> {

    private Context mContext;
    private List<ReservationRecordBean> mData;

    public MeetingReservationRecordAdapter(Context context, List<ReservationRecordBean> data) {
        super(R.layout.meeting_reservation_record_list_item, data);
        mContext = context;
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ReservationRecordBean recordBean) {

        if (recordBean.getState().equals("0")) {
//            baseViewHolder.setBackgroundColor(R.id.tvBottomLine, mContext.getResources().getColor(R.color.gray_9b9b9b));
//            baseViewHolder.getView(R.id.btn_meeting_info_complete).setEnabled(false);
            baseViewHolder.getView(R.id.tvDot).setEnabled(false);
        } else {
//            baseViewHolder.setBackgroundColor(R.id.tvBottomLine, mContext.getResources().getColor(R.color.blue_69B0F2));
//            baseViewHolder.getView(R.id.btn_meeting_info_complete).setEnabled(true);
            baseViewHolder.getView(R.id.tvDot).setEnabled(true);
        }

//        baseViewHolder.setOnClickListener(R.id.btn_meeting_info_complete, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext,MeetingInfoFillOutActivity.class);
//                intent.putExtra("isWriteMeetingInfo",false);
//                mContext.startActivity(intent);
//            }
//        });

        //布局样式
        if (baseViewHolder.getPosition() == 0) {
            baseViewHolder.setVisible(R.id.tvTopLine, true);
        }
//        else if (baseViewHolder.getPosition() == mData.size() - 1) {
//            baseViewHolder.setVisible(R.id.tvBottomLine, false);
//        } else {
//            baseViewHolder.setVisible(R.id.tvTopLine, false);
//            baseViewHolder.setVisible(R.id.tvBottomLine, true);
//        }

    }
}
