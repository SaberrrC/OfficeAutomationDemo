package com.shanlinjinrong.oa.ui.activity.message.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.hyphenate.easeui.utils.GlideRoundTransformUtils;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppManager;

import java.util.List;

/**
 * @Description：消息详情展示Adapter
 * @Auther：王凤旭
 * @Email：Tonnywfx@Gmail.com
 */

public class CommonGroupControlAdapter extends BaseQuickAdapter<GroupUserInfoResponse> {

    public CommonGroupControlAdapter(int layoutResId, List<GroupUserInfoResponse> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, GroupUserInfoResponse bean) {
        ImageView portraits = baseViewHolder.getView(R.id.img_person_profile);
        TextView name = baseViewHolder.getView(R.id.tv_person_name);
        switch (bean.getUsername()) {
            case Constants.MEMBERADD:
                portraits.setImageResource(R.mipmap.add_chat_contacts);
                name.setVisibility(View.INVISIBLE);
                break;
            case Constants.MEMBERDELETE:
                portraits.setImageResource(R.mipmap.delete_chat_contacts);
                name.setVisibility(View.INVISIBLE);
                break;
            default:
                try {
                    String portaits = bean.getImg();
                    name.setVisibility(View.VISIBLE);
                    name.setText(bean.getUsername());

                    if (!portaits.equals(com.example.retrofit.constants.Constants.SLPicBaseUrl)) {
                        Glide.with(AppManager.mContext)
                                .load(portaits)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.drawable.icon_homepage_work_report_me_launch)
                                .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                                .placeholder(R.drawable.icon_homepage_work_report_me_launch)
                                .into(portraits);
                    } else {
                        Glide.with(AppManager.mContext).load(R.drawable.icon_homepage_work_report_me_launch).asBitmap().into(portraits);
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
