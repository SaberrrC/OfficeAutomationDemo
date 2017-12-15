package com.shanlinjinrong.oa.ui.activity.message.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
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
                name.setVisibility(View.INVISIBLE);
                break;
            case "delete":
                portraits.setImageResource(R.mipmap.delete_chat_contacts);
                name.setVisibility(View.INVISIBLE);
                break;
            default:
                try {
                    String portaits = bean.getImg();
                    //   portaits = Constants.SLPicBaseUrl + portaits;
                    name.setVisibility(View.VISIBLE);
                    name.setText(bean.getUsername());


                    ImageLoader.getInstance().displayImage(portaits, portraits, new DisplayImageOptions.Builder()
                            .showImageForEmptyUri(R.drawable.icon_homepage_work_report_me_launch)
                            .showImageOnFail(R.drawable.icon_homepage_work_report_me_launch)
                            .resetViewBeforeLoading(true)
                            .cacheOnDisk(true)
                            .imageScaleType(ImageScaleType.EXACTLY)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .considerExifParams(true)
                            .displayer(new FadeInBitmapDisplayer(300))
                            .build()); // Incoming options will be used

                 /*
                    Glide.with(AppManager.mContext)
                            .load(portaits)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.icon_homepage_work_report_me_launch)
                            .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                            .placeholder(R.drawable.icon_homepage_work_report_me_launch)
                            .into(portraits);
                    */

                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
