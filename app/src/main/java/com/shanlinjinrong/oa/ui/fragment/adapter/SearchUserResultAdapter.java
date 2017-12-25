package com.shanlinjinrong.oa.ui.fragment.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.easeui.utils.GlideRoundTransformUtils;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserResultAdapter extends BaseMultiItemQuickAdapter<User> {

    public SearchUserResultAdapter(List<User> data) {
        super(data);
        addItemType(100, R.layout.tab_contacts_employee);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, User user) {
        switch (baseViewHolder.getItemViewType()) {
            case 100:
                CircleImageView portrait = baseViewHolder.getView(R.id.portrait);
                String portraits = Constants.SLPicBaseUrl + user.getPortraits();
                Glide.with(AppManager.mContext)
                        .load(portraits)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ease_user_portraits)
                        .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                        .placeholder(R.drawable.ease_user_portraits).into(portrait);

                baseViewHolder.setText(R.id.user_name, user.getUsername())
                        .setText(R.id.user_post_name, user.getPostName());
                break;
        }
    }
}
