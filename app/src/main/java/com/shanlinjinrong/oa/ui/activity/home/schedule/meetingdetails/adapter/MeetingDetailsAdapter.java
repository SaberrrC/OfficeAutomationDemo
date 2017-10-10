package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.MeetingPredetermineRecordActivity;

import java.util.List;

import butterknife.Bind;

/**
 * Created by tonny on 2017/9/29.
 */

public class MeetingDetailsAdapter extends BaseQuickAdapter<String> {


    private Context mContext;

    public MeetingDetailsAdapter(Context context, List<String> data) {
        super(R.layout.meeting_details_list_item, data);
        mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, String workContentBean) {

//        SpannableString location = new SpannableString("星峰企业园1号楼");
//        ImageSpan locationSpan = new ImageSpan(mContext, R.mipmap.location);
//        location.setSpan(locationSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//
//
//        SpannableString device = new SpannableString("星峰企业园1号楼");
//        ImageSpan deviceSpan = new ImageSpan(mContext, R.mipmap.devices);
//        device.setSpan(deviceSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        baseViewHolder.setText(R.id.tv_meeting_name, "七楼大会议室");
        baseViewHolder.setText(R.id.tv_meeting_location,"星峰企业园1号楼");
        baseViewHolder.setText(R.id.tv_meeting_people_number, "20人");
        baseViewHolder.setText(R.id.tv_meeting_device, "投影");



        baseViewHolder.setOnClickListener(R.id.btn_meeting_predetermine, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MeetingPredetermineRecordActivity.class);
                intent.putExtra("meetingName", "七楼大会议室12");
                intent.putExtra("meetingPeopleNumber", "50人");
                intent.putExtra("meetingDevice", "投影12");
                mContext.startActivity(intent);
            }
        });
    }

}
