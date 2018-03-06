package com.shanlinjinrong.oa.ui.activity.home.approval.adapter;

import android.support.v4.content.res.ResourcesCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.approval.bean.LaunchApprovalItem;

import java.util.List;

/**
 * 作者：王凤旭
 * 创建时间：2018/3/2
 * 功能描述：
 */

public class LaunchApprovalAdapter extends BaseQuickAdapter<LaunchApprovalItem> {
    public LaunchApprovalAdapter(List<LaunchApprovalItem> data) {
        super(R.layout.item_launch_approval, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LaunchApprovalItem launchApprovalItem) {
        baseViewHolder.setText(R.id.title, launchApprovalItem.getTitle());
        baseViewHolder.setImageDrawable(R.id.image, ResourcesCompat.getDrawable(mContext.getResources(), launchApprovalItem.getImage(), null));
    }
}
