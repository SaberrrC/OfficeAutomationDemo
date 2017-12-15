package com.shanlinjinrong.oa.ui.activity.message.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Description：
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class SelectedContactAdapter extends BaseMultiItemQuickAdapter<Contacts> {

    public SelectedContactAdapter(List<Contacts> data) {
        super(data);
        addItemType(Contacts.DEPARTMENT, R.layout.item_selected_group_contact);
        addItemType(Contacts.EMPLOYEE, R.layout.item_contacts_employee);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Contacts contacts) {
        switch (baseViewHolder.getItemViewType()) {
            case GroupContactBean.DEPARTMENT:
                baseViewHolder.setText(R.id.tv_department_name, contacts.getDepartmentName()
                        + "    " + "(" + contacts.getDepartmentPersons() + ")");
                break;
            case GroupContactBean.EMPLOYEE:

                TextView userName = baseViewHolder.getView(R.id.user_name);
                TextView postName = baseViewHolder.getView(R.id.user_post_name);
                CheckBox selectedUser = baseViewHolder.getView(R.id.cb_selected_user);
                selectedUser.setChecked(contacts.isChecked());

                if (contacts.isModificationColor()) {
                    if (contacts.isGroupOwner()) {
                        baseViewHolder.setVisible(R.id.cb_selected_user, false);
                        userName.setTextColor(AppManager.mContext.getResources().getColor(R.color.text_gray));
                    } else {
                        baseViewHolder.setVisible(R.id.cb_selected_user, true);
                        userName.setTextColor(AppManager.mContext.getResources().getColor(R.color.black_333333));
                    }
                }

                CircleImageView portrait = baseViewHolder.getView(R.id.portrait);
                Glide.with(AppManager.mContext)
                        .load(contacts.getPortraits())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.icon_homepage_work_report_me_launch)
                        .into(portrait);
                userName.setText(contacts.getUsername());
                postName.setText(contacts.getPostTitle());
                break;
        }
    }
}
