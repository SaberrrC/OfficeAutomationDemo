package com.shanlinjinrong.oa.ui.activity.message.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.message.bean.ChatMessageDetailsBean;
import com.shanlinjinrong.oa.utils.GlideRoundTransformUtils;

import java.util.List;

/**
 * @Description：消息详情展示Adapter
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class CommonPersonAddAdapter extends BaseQuickAdapter<ChatMessageDetailsBean> {

    private List<ChatMessageDetailsBean> mData;

    public CommonPersonAddAdapter(List<ChatMessageDetailsBean> data) {
        super(R.layout.item_common_person_add, data);
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ChatMessageDetailsBean bean) {
        baseViewHolder.setText(R.id.tv_person_name, bean.getName());
        ImageView portraits = baseViewHolder.getView(R.id.img_person_profile);
        TextView name = baseViewHolder.getView(R.id.tv_person_name);
        try {
            if (baseViewHolder.getPosition() == mData.size() - 1) {
                name.setVisibility(View.INVISIBLE);
                Glide.with(AppManager.mContext)
                        .load(bean.getPortraist())
                        .error(R.mipmap.icon_default_group_portraits)
                        .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                        .placeholder(R.mipmap.icon_default_group_portraits).into(portraits);
                return;
            }
            Glide.with(AppManager.mContext)
                    .load(bean.getPortraist())
                    .error(R.mipmap.icon_default_group_portraits)
                    .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                    .placeholder(R.mipmap.icon_default_group_portraits).into(portraits);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
