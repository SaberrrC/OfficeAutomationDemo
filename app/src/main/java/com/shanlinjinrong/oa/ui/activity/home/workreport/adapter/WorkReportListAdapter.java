package com.shanlinjinrong.oa.ui.activity.home.workreport.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.ReportList;

import java.util.List;

/**
 * <h3>Description: 工作汇报列表适配器 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/6.<br />
 */
public class WorkReportListAdapter extends BaseQuickAdapter<ReportList> {


    /**
     * @param data items
     * @param
     */
    public WorkReportListAdapter(List<ReportList> data) {
        super(R.layout.work_report_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ReportList report) {
        String titleTag = null, date;
        switch (Integer.parseInt(report.getType())) {
            case 1:
                titleTag = "周报";
                break;
            case 2:
                titleTag = "月报";
                break;
        }

            date = report.getCreateTimeYM() + "  " + report.getCreateTimeHI();
        holder.setText(R.id.title, "[ " +titleTag + " ] " +report.getLastTime() )
                .setText(R.id.date, date).setText(R.id.name, report.getSendUser());

        TextView title = holder.getView(R.id.title);
        TextView tvTag = holder.getView(R.id.date);
        TextView name = holder.getView(R.id.name);

        String state=report.getStatus();
        //状态 1未读，2已读
        if ("1".equals(state)) {
            title.getPaint().setFakeBoldText(true);
            title.setTextColor(Color.parseColor("#333333"));
            tvTag.setTextColor(Color.parseColor("#333333"));
            name.setTextColor(Color.parseColor("#333333"));
        } else if ("2".equals(state)){
            title.getPaint().setFakeBoldText(true);
            title.setTextColor(Color.parseColor("#999999"));
            tvTag.setTextColor(Color.parseColor("#999999"));
            name.setTextColor(Color.parseColor("#999999"));
        }
    }
}