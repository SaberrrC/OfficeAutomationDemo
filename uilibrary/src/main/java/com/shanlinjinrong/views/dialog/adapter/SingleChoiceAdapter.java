package com.shanlinjinrong.views.dialog.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.dialog.listener.OnItemClickListener;

/**
 * Created by 丁 on 2017/9/12.
 * single choice
 */
public class SingleChoiceAdapter extends RecyclerView.Adapter<SingleChoiceAdapter.SingleChoiceHolder> {
    private CharSequence[] mItems;
    private int selectPosition;
    private OnItemClickListener itemClickListener;

    public SingleChoiceAdapter(CharSequence[] mItems, int selectPosition) {
        this.mItems = mItems;
        this.selectPosition = selectPosition;
    }

    @Override
    public SingleChoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SingleChoiceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_chlice_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SingleChoiceHolder holder, final int position) {
        holder.title.setText(mItems[position]);
        if (selectPosition == position) {
            holder.icon.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClicked(SingleChoiceAdapter.this, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mItems.length == 0 ? 0 : mItems.length;
    }

    public CharSequence[] getItems() {
        return mItems;
    }

    class SingleChoiceHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView icon;

        SingleChoiceHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.single_choice_title);
            icon = (ImageView) itemView.findViewById(R.id.single_choice_icon);
        }
    }

    public SingleChoiceAdapter setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }


}
