package com.shanlin.oa.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlin.oa.R;
import com.shanlin.oa.model.JoinVideoMember;

import java.util.List;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.adapter
 * Author:Created by CXP on Date: 2017/3/23 13：14
 * Description:视频会议参会人适配器
 */
public class JoinVideoMeetingListAdapter extends BaseQuickAdapter<JoinVideoMember> {

    public JoinVideoMeetingListAdapter(List<JoinVideoMember> data) {
        super(R.layout.activity_join_video_member_item, data);

    }

    @Override
    protected void convert(BaseViewHolder holder, JoinVideoMember joinVideoMember) {

        holder.setText(R.id.tv_userName, joinVideoMember.getUserName())
                .setText(R.id.tv_post, joinVideoMember.getPost());

        //1：互动 2：没有互动
        if (joinVideoMember.getState().equals("1")) {
            holder.setText(R.id.tv_state, "互动中");
            holder.getView(R.id.iv_hand).setVisibility(View.INVISIBLE);
        } else {
            //是否举手
            if (joinVideoMember.isHand()) {
                holder.getView(R.id.iv_hand).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_state, "切换");
            } else {
                holder.getView(R.id.iv_hand).setVisibility(View.INVISIBLE);
                holder.setText(R.id.tv_state, "");
            }
        }


    }
}
