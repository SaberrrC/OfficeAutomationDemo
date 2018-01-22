package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofit.net.ApiConstant;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingPredetermineRecordActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MeetingRoomsBean;

import java.util.List;

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
        final String roomnameX = workContentBean.getRoomname();
        final int nopX = workContentBean.getNop();
        final String deviceX = workContentBean.getDevice();
        final String roomimgX = workContentBean.getRoomimg();
        final int room_idX = workContentBean.getRoom_id();

        baseViewHolder.setText(R.id.tv_meeting_name, roomnameX);
        baseViewHolder.setText(R.id.tv_meeting_location, workContentBean.getAddress());
        baseViewHolder.setText(R.id.tv_meeting_people_number, workContentBean.getNop() + "人");
        baseViewHolder.setText(R.id.tv_meeting_device, deviceX);
        try {
            Glide.with(AppManager.mContext)
                    .load(ApiConstant.BASE_PIC_URL + roomimgX)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.video_image_1)
                    .into((ImageView) baseViewHolder.getView(R.id.iv_meeting_details));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        baseViewHolder.setOnClickListener(R.id.meeting_select_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
