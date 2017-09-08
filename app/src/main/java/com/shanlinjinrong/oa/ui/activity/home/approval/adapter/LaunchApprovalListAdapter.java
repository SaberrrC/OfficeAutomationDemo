package com.shanlinjinrong.oa.ui.activity.home.approval.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.approval.bean.LaunchApprovalItem;

import java.util.List;

/**
 * Created by 丁 on 2017/9/1.
 * 发起审批页面list
 */
public class LaunchApprovalListAdapter extends RecyclerView.Adapter<LaunchApprovalListAdapter.ItemHolder> {
    private Context mContext;
    private List<LaunchApprovalItem> mValues;
    private OnItemClickListener itemClickListener;


    public LaunchApprovalListAdapter(List<LaunchApprovalItem> items) {
        mValues = items;
    }

    @Override
    public LaunchApprovalListAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_launch_approval, parent, false);
        mContext = view.getContext();
        return new ItemHolder(view);
    }


    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        LaunchApprovalItem approvalItem = mValues.get(position);
        holder.title.setText(approvalItem.getTitle());
        holder.image.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), approvalItem.getImage(), null));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClicked(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        View mView;

        ItemHolder(View view) {
            super(view);
            mView = view;
            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public LaunchApprovalListAdapter setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }
}
