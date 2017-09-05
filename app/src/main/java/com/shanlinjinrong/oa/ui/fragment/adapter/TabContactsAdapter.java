package com.shanlinjinrong.oa.ui.fragment.adapter;

import android.net.Uri;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.Contacts;

import java.util.List;

/**
 * <h3>Description: 通讯录适配器 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/1.<br />
 */
public class TabContactsAdapter extends BaseMultiItemQuickAdapter<Contacts> {


    public TabContactsAdapter(List<Contacts> data) {
        super(data);
        addItemType(Contacts.DEPARTMENT, R.layout.tab_contacts_department);
        addItemType(Contacts.EMPLOYEE, R.layout.tab_contacts_employee);
    }

    @Override
    protected void convert(BaseViewHolder helper, Contacts contacts) {
        switch (helper.getItemViewType()) {
            case Contacts.DEPARTMENT:
                if (contacts.getItemType() == Contacts.DEPARTMENT) {
                    helper.setText(R.id.name, contacts.getDepartmentName())
                            .setText(R.id.qty, "（" + contacts.getDepartmentPersons() + "）");

                }
                break;
            case Contacts.EMPLOYEE:
                SimpleDraweeView portrait = helper.getView(R.id.portrait);
                portrait.setImageURI(Uri.parse(contacts.getPortraits()));
                helper.setText(R.id.user_name, contacts.getUsername())
                        .setText(R.id.user_post_name, contacts.getPostTitle());
                break;
        }
    }




}