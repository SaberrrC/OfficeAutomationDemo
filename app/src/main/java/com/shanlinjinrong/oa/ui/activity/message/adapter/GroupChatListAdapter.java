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

    private String mGroupName;

    public GroupChatListAdapter(List<EMGroup> data) {
        super(R.layout.item_group_list_show, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, EMGroup bean) {
        if (bean.getGroupName() != null) {
            if (bean.getGroupName().length() > 10) {
                mGroupName = bean.getGroupName().substring(0, 10) + "...";
            } else {
                mGroupName = bean.getGroupName();
            }
        }
        baseViewHolder.setText(R.id.tv_group_name, mGroupName);
    }
}
