package com.shanlinjinrong.oa.ui.activity.message.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupContactBean;

import java.util.List;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class SelectedContactAdapter extends BaseMultiItemQuickAdapter<Contacts> {

    public SelectedContactAdapter(List<Contacts> data) {
        super(data);
        addItemType(Contacts.DEPARTMENT, R.layout.item_selected_group_contact);
        addItemType(Contacts.EMPLOYEE, R.layout.tab_contacts_employee);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Contacts contacts) {
        switch (baseViewHolder.getItemViewType()) {
            case GroupContactBean.DEPARTMENT:
                baseViewHolder.setText(R.id.tv_department_name, contacts.getDepartmentName()
                        + "    " + "(" + contacts.getDepartmentPersons() + ")");
                break;
            case GroupContactBean.EMPLOYEE:
                CheckBox selectedUser = baseViewHolder.getView(R.id.cb_selected_user);
                selectedUser.setChecked(contacts.isChecked());
                SimpleDraweeView portrait = baseViewHolder.getView(R.id.portrait);
                Glide.with(AppManager.mContext)
                        .load(contacts.getPortraits())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(com.hyphenate.easeui.R.drawable.ease_default_avatar)
                        .into(portrait);
                baseViewHolder.setText(R.id.user_name, contacts.getUsername())
                        .setText(R.id.user_post_name, contacts.getPostTitle());
                break;
        }
    }
}
