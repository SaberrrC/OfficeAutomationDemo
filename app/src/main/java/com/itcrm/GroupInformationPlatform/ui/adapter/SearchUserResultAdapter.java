package com.itcrm.GroupInformationPlatform.ui.adapter;

import android.net.Uri;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.model.User;

import java.util.List;

/**
 * Created by lenovo on 2017/6/25.
 */

public class SearchUserResultAdapter extends BaseMultiItemQuickAdapter<User> {


    public SearchUserResultAdapter(List<User> data) {
        super(data);
        addItemType(100, R.layout.tab_contacts_employee);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        switch (baseViewHolder.getItemViewType()) {
            case 100:
                SimpleDraweeView portrait = baseViewHolder.getView(R.id.portrait);
                portrait.setImageURI(Uri.parse(user.getPortraits()));
//                if (user.getPortraits() == null) {
//                    portrait.setBackgroundResource(R.drawable.tab_me_head_default);
//                } else {
                portrait.setImageURI(Uri.parse(user.getPortraits()));
//                }
                baseViewHolder.setText(R.id.user_name, user.getUsername())
                        .setText(R.id.user_post_name, user.getPostName());
                break;
        }
    }
}
