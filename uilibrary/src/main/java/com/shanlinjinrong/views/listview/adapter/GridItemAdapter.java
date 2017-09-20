package com.shanlinjinrong.views.listview.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.listview.GridItem;
import com.shanlinjinrong.views.listview.holder.BaseViewHolder;
import com.shanlinjinrong.views.listview.holder.GridViewHolder;

import java.util.List;

/**
 * Created by 丁 on 2017/9/11.
 * grid adapter
 */
public class GridItemAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<GridItem> mItems;
    private Context mContext;
    private OnItemClickListener itemClickListener;
    private boolean isCardView;

    public GridItemAdapter(Context mContext, List<GridItem> mItems) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    public GridItemAdapter(Context mContext, List<GridItem> mItems, boolean isCardView) {
        this.mContext = mContext;
        this.mItems = mItems;
        this.isCardView = isCardView;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (isCardView) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_card_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        }
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (holder instanceof GridViewHolder) {
            GridViewHolder gridHolder = (GridViewHolder) holder;
            if (mItems.get(position).getImageDrawableId() != 0)
                gridHolder.image.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), mItems.get(position).getImageDrawableId(), null));
            else if (!TextUtils.isEmpty(mItems.get(position).getImageUrl())) {
                Glide.with(mContext).load(mItems.get(position).getImageUrl()).into(gridHolder.image);
            }
            gridHolder.title.setText(mItems.get(position).getTitle());

            gridHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClicked(GridItemAdapter.this, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnItemClickListener {
        void onItemClicked(GridItemAdapter adapter, int position);
    }

    public GridItemAdapter setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }
}
