package com.shanlinjinrong.oa.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.PushMsg;

import java.util.List;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.adapter
 * Author:Created by CXP on Date: 2016/9/18 18:17.
 * Description:
 */
public class TabMsgListAdapter extends BaseQuickAdapter<PushMsg> {
    List<PushMsg> data;

    //BaseQuickAdapter<PushMsg>
    public TabMsgListAdapter(List<PushMsg> data) {
        super(R.layout.tab_msg_list_recyclerview_item, data);

        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, PushMsg pushMsg) {

        int status = Integer.parseInt(pushMsg.getStatus());
        switch (status) {
            case 1://未读
                holder.getView(R.id.iv_no_red).setVisibility(View.VISIBLE);
                break;
            case 2://已读
                holder.getView(R.id.iv_no_red).setVisibility(View.GONE);
                break;
        }

        int type = Integer.parseInt(pushMsg.getType());
        String typeStr="";
        ImageView iconIv = holder.getView(R.id.iv_icon);
        switch (type) {
          //  类型 5,10,11工作汇报，6,审批申请，7审批回复,8会议,9视频会议(日程),2,3公告
            case 5:
            case 10:
            case 11://工作汇报
                typeStr="工作汇报";
                iconIv.setImageResource(R.drawable.icon_msg_list_work_report);
                break;
            case 6://审批申请
            case 7://审批回复
                typeStr="审批申请";
                iconIv.setImageResource(R.drawable.icon_msg_list_approval);
                break;
            case 8://会议
            case 9://视频会议
                typeStr="日程安排";
                iconIv.setImageResource(R.drawable.icon_msg_list_schedule);
                break;
            case 2:
            case 3://公告
                typeStr="公告通知";
                iconIv.setImageResource(R.drawable.icon_msg_list_notice);
                break;
        }

        //截取字符串,防止创建房间417问题
        int length = pushMsg.getContent().trim().length();
        if (length > 32) {
            length -= 32;
        }
        String roomName = pushMsg.getContent().trim().substring(0, length);

        holder.setText(R.id.tv_item_time, pushMsg.getCreated())
                .setText(R.id.tv_item_type, typeStr).
                setText(R.id.tv_title, roomName);
    }
}
