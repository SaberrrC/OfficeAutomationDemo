package com.shanlinjinrong.oa.ui.activity.home.schedule.adapter;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.MeetRoom;

import java.util.List;


public class SelectOrdinaryMeetingRoomAdapter extends BaseMultiItemQuickAdapter<MeetRoom> {


    public SelectOrdinaryMeetingRoomAdapter(List<MeetRoom> data) {
        super(data);
        addItemType(10, R.layout.select_ordinary_meeting_room_item);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MeetRoom meetRoom) {
        switch (baseViewHolder.getItemViewType()) {
            case 10:



                //人数
                if (meetRoom.getNop()==null||meetRoom.getNop().equals("null")){
                    baseViewHolder.setText(R.id.people_num, "-人");

                }else{
                    baseViewHolder.setText(R.id.people_num, meetRoom.getNop());
                }
                //设备
                if (meetRoom.getDevice()==null||meetRoom.getDevice().equals("")){
                    baseViewHolder.setText(R.id.equipment_name, "-");
                }else{
                    baseViewHolder.setText(R.id.equipment_name, meetRoom.getDevice());
                }
                //开始时间
                if (meetRoom.getBegintime()==null||meetRoom.getBegintime().equals("null")){
                    baseViewHolder.setText(R.id.next_meet_time, "-");
                }else{
                    baseViewHolder.setText(R.id.next_meet_time, meetRoom.getBegintime());
                }

                baseViewHolder.setText(R.id.meet_room_name, meetRoom.getRoomname())
                        .setText(R.id.meet_room_location, meetRoom.getAddress()+meetRoom.getFloor());
                break;
        }
    }
}
