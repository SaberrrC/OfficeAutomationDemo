package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;
import com.shanlinjinrong.utils.DeviceUtil;

import java.util.List;


/**
 * Created by 丁 on 2017/10/12.
 * 自定义日期选择框
 */
public class DatePopAdapter extends RecyclerView.Adapter<DatePopAdapter.ItemHolder> {
    private List<PopItem> mData;
    private Context mContext;

    private OnItemClick mOnItemClick;

    public DatePopAdapter(List<PopItem> mData) {
        this.mData = mData;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.date_pop_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        holder.item.setText(mData.get(position).getContent());
        if (mData.get(position).isSelect()) {
            holder.item.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));
            holder.image.setVisibility(View.VISIBLE);
            if (holder.item.getText().toString().length() > 2) {
                ViewGroup.LayoutParams lp = holder.image.getLayoutParams();
                lp.width = DeviceUtil.dip2px(mContext, 46);
                lp.height = DeviceUtil.dip2px(mContext, 46);
                holder.image.setLayoutParams(lp);
            }
        } else {
            holder.image.setVisibility(View.GONE);
//            holder.item.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.date_pop_text_selector, null));
        }

        if (mData.get(position).isEnable()) {
            if (!mData.get(position).isSelect())
                holder.item.setTextColor(0xFF4A4A4A);
        } else {
            holder.item.setTextColor(0xFF999999);
        }


        holder.item.setEnabled(mData.get(position).isEnable());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSelect(position);
                if (mOnItemClick != null) {
                    mOnItemClick.onItemClicked(position);
                }
            }
        });

    }

    private void setSelect(int select) {
        for (int i = 0; i < mData.size(); i++) {
            if (select == i) {
                mData.get(i).setSelect(true);
            } else {
                mData.get(i).setSelect(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView item;
        ImageView image;

        ItemHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.pop_item);
            image = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    /**
     * PopWindow点击事件回调接口
     */
    public interface OnItemClick {
        void onItemClicked(int position);
    }

    public DatePopAdapter setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
        return this;
    }
}
