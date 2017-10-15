package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingPredetermineRecordActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;
import com.shanlinjinrong.oa.utils.GlideRoundTransformUtils;

import java.util.List;

import butterknife.Bind;

/**
 * 选择会议室
 */

public class MeetingDetailsAdapter extends BaseQuickAdapter<MeetingRoomsBean.DataBean> {


    private Context mContext;

    public MeetingDetailsAdapter(Context context, List<MeetingRoomsBean.DataBean> data) {
        super(R.layout.meeting_details_list_item, data);
        mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, MeetingRoomsBean.DataBean workContentBean) {

        baseViewHolder.setText(R.id.tv_meeting_name, workContentBean.getRoomnameX());
        baseViewHolder.setText(R.id.tv_meeting_location, workContentBean.getAddressX());
        baseViewHolder.setText(R.id.tv_meeting_people_number, workContentBean.getNopX() + "人");
        baseViewHolder.setText(R.id.tv_meeting_device, workContentBean.getDeviceX());

        final String roomnameX = workContentBean.getRoomnameX();
        final int nopX = workContentBean.getNopX();
        final String deviceX = workContentBean.getDeviceX();
        final String roomimgX = workContentBean.getRoomimgX();
        final int room_idX = workContentBean.getRoom_idX();

        try {
            Glide.with(AppManager.mContext)
                    .load(roomimgX)
                    .into((ImageView) baseViewHolder.getView(R.id.iv_meeting_details));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        baseViewHolder.setOnClickListener(R.id.btn_meeting_predetermine, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MeetingPredetermineRecordActivity.class);
                intent.putExtra("meetingName", roomnameX);
                intent.putExtra("meetingPeopleNumber", nopX + "人");
                intent.putExtra("meetingDevice", deviceX);
                intent.putExtra("roomId", room_idX);
                mContext.startActivity(intent);
            }
        });
    }

}
