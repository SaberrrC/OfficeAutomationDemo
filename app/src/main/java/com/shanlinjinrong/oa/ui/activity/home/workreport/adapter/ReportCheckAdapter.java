package com.shanlinjinrong.oa.ui.activity.home.workreport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.CheckReportItem;

import java.util.List;

/**
 * Created by 丁 on 2017/9/21.
 * 审核汇报
 */
public class ReportCheckAdapter extends RecyclerView.Adapter<ReportCheckAdapter.ViewHolder> {

    private List<CheckReportItem> items;

    public ReportCheckAdapter(List<CheckReportItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.work_report_check_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView time;
        private TextView commitTime;
        private TextView account;

        public ViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.tv_report_type);
            time = (TextView) itemView.findViewById(R.id.tv_report_time);
            commitTime = (TextView) itemView.findViewById(R.id.tv_report_commit_time);
            account = (TextView) itemView.findViewById(R.id.tv_report_account);
        }

        public void setData(CheckReportItem reportItem) {
            type.setText(reportItem.getType());
            time.setText(reportItem.getReportTime());
            commitTime.setText(reportItem.getReportCommitTime());
            account.setText(reportItem.getReportAccount());
        }
    }

}
