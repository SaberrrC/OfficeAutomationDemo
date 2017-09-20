package com.shanlinjinrong.views.dialog.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.dialog.MultiItem;
import com.shanlinjinrong.views.dialog.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by 丁 on 2017/9/12.
 * single choice
 */
public class MultiChoiceAdapter extends RecyclerView.Adapter<MultiChoiceAdapter.MultiChoiceHolder> {
    private List<MultiItem> mItems;
    private List<MultiItem> mSelectItems;
    private OnItemClickListener itemClickListener;

    public MultiChoiceAdapter(List<MultiItem> mItems) {
        this.mItems = mItems;
    }

    @Override
    public MultiChoiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MultiChoiceHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_chlice_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MultiChoiceHolder holder, final int position) {
        holder.title.setText(mItems.get(position).getTitle());
        holder.icon.setEnabled(mItems.get(position).isCheck());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClicked(MultiChoiceAdapter.this, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size() == 0 ? 0 : mItems.size();
    }

    class MultiChoiceHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView icon;

        MultiChoiceHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.multi_choice_title);
            icon = (ImageView) itemView.findViewById(R.id.multi_choice_icon);
        }
    }

    public MultiChoiceAdapter setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    public List<MultiItem> getSelectItems() {
        return mSelectItems;
    }
}
