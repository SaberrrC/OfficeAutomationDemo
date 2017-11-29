package com.shanlinjinrong.oa.ui.activity.message.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMGroup;
import com.shanlinjinrong.oa.R;

import java.util.List;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class GroupChatListAdapter extends BaseQuickAdapter<EMGroup> {
    public GroupChatListAdapter(List<EMGroup> data) {
        super(R.layout.item_group_list_show, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, EMGroup bean) {
        baseViewHolder.setText(R.id.tv_group_name, bean.getGroupName());
    }
}
