package com.shanlinjinrong.oa.ui.activity.home.workreport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.MyLaunchReportItem;

import java.util.List;

/**
 * Created by 丁 on 2017/9/21.
 * 我发送的全部汇报
 */
public class AllReportAdapter extends RecyclerView.Adapter<AllReportAdapter.ViewHolder> {

    private List<MyLaunchReportItem> items;

    public AllReportAdapter(List<MyLaunchReportItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_launch_report_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView time;
        private TextView commitTime;
        private TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.tv_report_type);
            time = (TextView) itemView.findViewById(R.id.tv_report_time);
            commitTime = (TextView) itemView.findViewById(R.id.tv_report_commit_time);
            status = (TextView) itemView.findViewById(R.id.tv_report_status);
        }

        public void setData(MyLaunchReportItem reportItem) {
            type.setText(reportItem.getType());
            time.setText(reportItem.getReportTime());
            commitTime.setText(reportItem.getReportCommitTime());
            status.setText(reportItem.getStatus());
        }
    }

}
