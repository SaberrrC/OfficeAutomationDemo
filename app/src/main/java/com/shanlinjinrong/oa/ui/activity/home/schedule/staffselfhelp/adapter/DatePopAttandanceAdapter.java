package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter;

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
public class DatePopAttandanceAdapter extends RecyclerView.Adapter<DatePopAttandanceAdapter.ItemHolder> {
    private List<PopItem> mData;
    private Context mContext;

    private OnItemClick mOnItemClick;

    public DatePopAttandanceAdapter(List<PopItem> mData) {
        this.mData = mData;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.date_attandance_item, parent, false));
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
        }

        if (mData.get(position).isEnable()) {
            int dateType = 1;
            holder.imageState.setVisibility(View.VISIBLE);
            dateType = mData.get(position).getDateType();
            switch (dateType) {
                case 4:
                    holder.imageState.setImageResource(R.drawable.circle_red);
                    break;
                case 3:
                    holder.imageState.setImageResource(R.drawable.circle_blue);
                    break;
                case 2:
                    holder.imageState.setImageResource(R.drawable.circle_yellow);
                    break;
                case 1:
                    holder.imageState.setImageResource(R.drawable.circle_green);
                    break;
            }


            if (!mData.get(position).isSelect())
                holder.item.setTextColor(0xFF4A4A4A);
        } else {
            holder.item.setTextColor(0xFF999999);
            holder.imageState.setVisibility(View.GONE);
        }


        holder.item.setEnabled(mData.get(position).isEnable());
        holder.item.setOnClickListener(v -> {
//                setSelect(position);
            if (mOnItemClick != null) {
                mOnItemClick.onItemClicked(v, position);
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
        ImageView imageState;

        ItemHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.pop_item);
            image = (ImageView) itemView.findViewById(R.id.iv_icon);
            imageState = (ImageView) itemView.findViewById(R.id.iv_state);
        }
    }

    /**
     * PopWindow点击事件回调接口
     */
    public interface OnItemClick {
        void onItemClicked(View v, int position);
    }

    public DatePopAttandanceAdapter setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
        return this;
    }
}
