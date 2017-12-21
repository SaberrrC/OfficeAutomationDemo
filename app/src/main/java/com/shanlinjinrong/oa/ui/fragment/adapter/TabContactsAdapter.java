package com.shanlinjinrong.oa.ui.fragment.adapter;

import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.easeui.utils.GlideRoundTransformUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
                    String departmentPersons = contacts.getDepartmentPersons();
                    if (departmentPersons.equals("-")) {
                        departmentPersons = "";
                    }
                    helper.setText(R.id.name, contacts.getDepartmentName())
                            .setText(R.id.qty, "（" + departmentPersons + "）");

                }
                break;
            case Contacts.EMPLOYEE:
                CircleImageView portrait = helper.getView(R.id.portrait);
                String portraits = Constants.SLPicBaseUrl + contacts.getPortraits();

//                ImageLoader.getInstance().displayImage(portraits, portrait, new DisplayImageOptions.Builder()
//                        .showImageForEmptyUri(R.drawable.icon_homepage_work_report_me_launch)
//                        .showImageOnFail(R.drawable.icon_homepage_work_report_me_launch)
//                        .resetViewBeforeLoading(true)
//                        .cacheOnDisk(true)
//                        .imageScaleType(ImageScaleType.EXACTLY)
//                        .bitmapConfig(Bitmap.Config.RGB_565)
//                        .considerExifParams(true)
//                        .displayer(new FadeInBitmapDisplayer(300))
//                        .build());

                Glide.with(AppManager.mContext)
                        .load(portraits)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ease_user_portraits)
                        .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                        .placeholder(R.drawable.ease_user_portraits).into(portrait);

                helper.setText(R.id.user_name, contacts.getUsername())
                        .setText(R.id.user_post_name, contacts.getPostTitle());
                break;
        }
    }


}