package com.shanlinjinrong.oa.ui.activity.home.workreport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanlinjinrong.oa.R;

/**
 * Created by 丁 on 2017/9/20.
 */

public class WorkReportCheckAdapter extends RecyclerView.Adapter<WorkReportCheckAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.work_report_check_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
        }
    }
}
