package com.shanlinjinrong.oa.ui.fragment.adapter;

import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.utils.GlideRoundTransformUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
                Glide.with(AppManager.mContext)
                        .load(user.getPortraits())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ease_default_avatar)
                        .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                        .placeholder(R.drawable.ease_default_avatar).into(portrait);
                baseViewHolder.setText(R.id.user_name, user.getUsername())
                        .setText(R.id.user_post_name, user.getPostName());
                break;
        }
    }
}
