package com.shanlin.oa.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlin.oa.R;
import com.shanlin.oa.model.Report;
import com.shanlin.oa.utils.Utils;

import java.util.List;

/**
 * ProjectName: GroupInformationPlatform
 * PackageName: com.itcrm.GroupInformationPlatform.ui.adapter
 * Author:Created by CXP on Date: 2016/9/8 11:43.
 * Description:消息界面条目适配器
 */
public class TabMsgRecyclerViewAdapter extends BaseQuickAdapter<Report> {

    private List<Report> data;

    public TabMsgRecyclerViewAdapter(List<Report> data) {
        super(R.layout.tab_message_recycler_item, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder holder, Report report) {

        String type = "";
        switch (Integer.parseInt(report.getType())) {
            case 1:
                type = "[日报]";
                break;
            case 2:
                type = "[周报]";
                break;
            case 3:
                type = "[月报]";
                break;

        }
        View noReadView = holder.getView(R.id.tab_msg_no_read);
        if (Integer.parseInt(report.getStatus()) == 1) {
            noReadView.setVisibility(View.VISIBLE);
        } else {
            noReadView.setVisibility(View.GONE);
        }
        holder.setText(R.id.tab_msg_mymsg_title, report.getSendUser() + "的工作汇报").
                setText(R.id.tab_msg_mymsg_content,report.getSendUser()+" "+report.getFirstTime()).
                setText(R.id.tab_msg_mymsg_type, type).
                setText(R.id.tab_msg_mymsg_date, report.getCreateTimeYM());

        View line = holder.getView(R.id.line);
        if (holder.getAdapterPosition() == data.size()) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 3);
            lp.setMargins(0, Utils.dip2px(14), 0, 0);
            lp.addRule(RelativeLayout.BELOW, R.id.tab_msg_mymsg_iv);
            line.setLayoutParams(lp);
        }

    }
}