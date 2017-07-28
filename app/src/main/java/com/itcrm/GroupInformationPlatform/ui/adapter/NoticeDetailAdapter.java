package com.itcrm.GroupInformationPlatform.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.model.Notice;

import java.util.List;

/**
 * ProjectName: dev-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.adapter
 * Author:Created by CXP on Date: 2016/9/18 18:17.
 * Description:
 */
public class NoticeDetailAdapter extends BaseQuickAdapter<Notice> {
    List<Notice> data;

    public NoticeDetailAdapter(List<Notice> data) {
        super(R.layout.notice_detail_recyclerview_item, data);

        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, Notice notice) {

        int status = Integer.parseInt(notice.getStatus());
        switch (status) {
            case 1://已读
                ((TextView) holder.getView(R.id.tv_notice_detail_rv_item_title))
                        .setTextColor(Color.parseColor("#bfbaba"));
                ((TextView) holder.getView(R.id.tv_notice_detail_rv_item_title_top))
                        .setTextColor(Color.parseColor("#bfbaba"));
                ((TextView) holder.getView(R.id.tv_notice_detail_rv_item_time))
                        .setTextColor(Color.parseColor("#bfbaba"));
                break;
            case 0://wei读
                ((TextView) holder.getView(R.id.tv_notice_detail_rv_item_title))
                        .setTextColor(Color.parseColor("#565656"));
                ((TextView) holder.getView(R.id.tv_notice_detail_rv_item_title_top))
                        .setTextColor(Color.parseColor("#548ee9"));
                ((TextView) holder.getView(R.id.tv_notice_detail_rv_item_time))
                        .setTextColor(Color.parseColor("#565656"));

                break;
        }
        holder.setText(R.id.tv_notice_detail_rv_item_time, notice.getCreatetime()).
                setText(R.id.tv_notice_detail_rv_item_title, notice.getTitle());
        String type = "";
        switch (Integer.parseInt(notice.getType())) {
            case 1:
                type = "集团通告:";
                break;
            case 2:
                type = "公司公告:";
                break;
            case 3:
                type = "部门通知:";
                break;
        }
        holder.setText(R.id.tv_notice_detail_rv_item_title_top, type);
        if (((holder.getAdapterPosition()) == (data.size() - 1))) {
            RelativeLayout itemView = holder.getView(R.id.tv_notice_detail_rv_item_root_view);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            itemView.setLayoutParams(params);
        }
    }
}
