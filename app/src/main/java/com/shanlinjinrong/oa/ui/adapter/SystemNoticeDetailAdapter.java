package com.shanlinjinrong.oa.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.SystemNotice;

import java.util.List;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.adapter
 * Author:Created by CXP on Date: 2016/9/18 18:17.
 * Description:系统消息RecyclerView适配器
 */
public class SystemNoticeDetailAdapter extends BaseQuickAdapter<SystemNotice> {
    List<SystemNotice> data;

    public SystemNoticeDetailAdapter(List<SystemNotice> data) {
        super(R.layout.system_notice_detail_recyclerview_item, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, SystemNotice notice) {
        holder.setText(R.id.tv_sys_notice_detail_rv_item_content, notice.getContent());
        if (((holder.getAdapterPosition()) == (data.size() - 1))) {
            RelativeLayout itemView = holder.getView(R.id.system_notice_detail_item_root_view);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            itemView.setLayoutParams(params);
        }

    }
}
