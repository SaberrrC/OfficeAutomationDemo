package com.shanlinjinrong.oa.ui.activity.message.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupUsers;

import java.util.List;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class SelectedUserAdapter extends BaseQuickAdapter<GroupUsers> {

    public SelectedUserAdapter(List<GroupUsers> data) {
        super(R.layout.item_selected_uers, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GroupUsers groupUsers) {
        if (groupUsers.isChecked()) {
            baseViewHolder.setText(R.id.tv_name, groupUsers.getUsername())
                    .setText(R.id.tv_department, groupUsers.getDepartmentName())
                    .setText(R.id.tv_post, groupUsers.getPostTitle());
        }
    }
}
