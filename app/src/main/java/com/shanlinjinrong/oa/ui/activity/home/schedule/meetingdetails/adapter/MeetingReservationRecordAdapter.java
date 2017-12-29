package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.ITEM_TYPE;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingInfoFillOutActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 会议室预订
 */

public class MeetingReservationRecordAdapter extends BaseMultiItemQuickAdapter<ReservationRecordBean.DataBeanX.DataBean> {

    private Context                                        mContext;
    private boolean                                        mIsUse;
    private List<ReservationRecordBean.DataBeanX.DataBean> mData;

    public MeetingReservationRecordAdapter(Context context, List<ReservationRecordBean.DataBeanX.DataBean> data) {
        super(data);
        addItemType(ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal(), R.layout.meeting_reservation_record_list_item);
        addItemType(ITEM_TYPE.ITEM_TYPE_FOOTER.ordinal(), R.layout.load_more_layout);
//        super(,R.layout.meeting_reservation_record_list_item, data);
        mContext = context;
        mData = data;
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    public String getWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(date);
        return week;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ReservationRecordBean.DataBeanX.DataBean dataBeanX) {
        if (baseViewHolder.getItemViewType() == ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            long currentTime = DateUtils.getTimestampFromString(str, "yyyy年MM月dd日   HH:mm");
            int start_time = dataBeanX.getStart_time();
            long startTime = start_time * 1000L;
            String week = getWeek(new Date(startTime));

            if (currentTime > startTime) {
                baseViewHolder.setText(R.id.tv_meeting_state, "已使用");
                baseViewHolder.setTextColor(R.id.tv_meeting_state, Color.rgb(102, 102, 102));
                baseViewHolder.setImageResource(R.id.tvDot, R.drawable.meeting_record_icon_success);
            } else {
                baseViewHolder.setText(R.id.tv_meeting_state, "待使用");
                baseViewHolder.setTextColor(R.id.tv_meeting_state, Color.rgb(85, 187, 255));
                baseViewHolder.setImageResource(R.id.tvDot, R.drawable.meeting_record_icon);
            }

            baseViewHolder.setText(R.id.tv_meeting_room_name, dataBeanX.getTitle());
            baseViewHolder.setText(R.id.tv_meeting_content, dataBeanX.getRoomname());
            baseViewHolder.setText(R.id.tv_accept_time, DateUtils.stringToDateTransform(dataBeanX.getStart_time() + "", "yyyy年MM月dd日  HH时mm分  ") + week);
        } else if (baseViewHolder.getItemViewType() == ITEM_TYPE.ITEM_TYPE_FOOTER.ordinal()) {
            return;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount() && position < mData.size()) {
            return ITEM_TYPE.ITEM_TYPE_FOOTER.ordinal();
        } else
            return ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal();
    }

}
