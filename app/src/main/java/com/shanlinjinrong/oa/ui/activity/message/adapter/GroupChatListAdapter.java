package com.shanlinjinrong.oa.ui.activity.message.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.message.bean.ChatMessageDetailsBean;

import java.util.List;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class GroupChatListAdapter extends BaseQuickAdapter<ChatMessageDetailsBean> {
    public GroupChatListAdapter(List<ChatMessageDetailsBean> data) {
        super(R.layout.item_group_list_show, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ChatMessageDetailsBean bean) {

    }
}
