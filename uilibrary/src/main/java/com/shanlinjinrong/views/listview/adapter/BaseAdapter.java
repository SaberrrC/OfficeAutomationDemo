package com.shanlinjinrong.views.listview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shanlinjinrong.bean.DataItemDetail;
import com.shanlinjinrong.views.listview.cell.BaseCell;
import com.shanlinjinrong.views.listview.holder.BaseViewHolder;

import java.util.List;

/**
 * Created by 丁 on 2017/9/14.
 * BaseAdapter 可以绑定不同的样式
 */
public class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<BaseCell> mItems;

    private OnItemClickListener mOnItemClickListener;

    public BaseAdapter(List<BaseCell> mItems) {
        this.mItems = mItems;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < getItemCount(); i++) {
            if (viewType == mItems.get(i).getItemType()) {
                return mItems.get(i).onCreateViewHolder(parent, viewType);
            }
        }
        throw new RuntimeException("Wrong viewType");
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        mItems.get(position).onBindViewHolder(holder, position);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClicked(BaseAdapter.this, mItems.get(holder.getAdapterPosition()).getData(), holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getItemType();
    }

    public interface OnItemClickListener {
        void onItemClicked(BaseAdapter adapter, DataItemDetail data, int position);
    }

    public BaseAdapter setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
        return this;
    }

    public List<BaseCell> getItems() {
        return mItems;
    }

    public void add(BaseCell cell, int index) {
        mItems.add(index, cell);
        notifyItemChanged(index);
    }

    public void add(BaseCell cell) {
        mItems.add(cell);
        int index = mItems.indexOf(cell);
        notifyItemChanged(index);
    }

}
