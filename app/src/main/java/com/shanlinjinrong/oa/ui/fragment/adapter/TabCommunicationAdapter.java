package com.shanlinjinrong.oa.ui.fragment.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.chat.EMConversation;
import com.shanlinjinrong.oa.R;

import java.util.List;

/**
 * <h3>Description: 通讯页面适配器 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/1.<br />
 */
public class TabCommunicationAdapter extends BaseQuickAdapter<EMConversation> {


    public TabCommunicationAdapter(List<EMConversation> data) {
        super(R.layout.tab_communication_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, EMConversation emConversation) {
        //TODO you 了好友列表后还得继续修改
        holder.setText(R.id.name, emConversation.conversationId())
                .setText(R.id.tv_last_content, emConversation.getLastMessage().toString())
                .setText(R.id.tv_last_time, emConversation.getLastMessage().toString());
        SimpleDraweeView portrait = holder.getView(R.id.opposite_portrait);
//        portrait.setImageURI(emConversation.get);
    }
}
