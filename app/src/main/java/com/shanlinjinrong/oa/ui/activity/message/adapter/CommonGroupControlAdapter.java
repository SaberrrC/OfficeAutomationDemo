package com.shanlinjinrong.oa.ui.activity.message.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.utils.GlideRoundTransformUtils;

import java.util.ArrayList;
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
            case "add":
                portraits.setImageResource(R.mipmap.add_chat_contacts);
                break;
            case "delete":
                portraits.setImageResource(R.mipmap.delete_chat_contacts);
                break;
            default:
                try {
                    String portaits = "http://" + bean.getImg();
                    portaits = portaits.replace("http:///", "http://");
                    name.setVisibility(View.VISIBLE);
                    name.setText(bean.getUsername());
                    Glide.with(AppManager.mContext)
                            .load(portaits)
                            .error(R.drawable.icon_homepage_work_report_me_launch)
                            .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                            .placeholder(R.drawable.icon_homepage_work_report_me_launch)
                            .into(portraits);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
