package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;
import com.shanlinjinrong.oa.utils.ScreenUtils;
import com.shanlinjinrong.utils.DeviceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Random;


/**
 * Created by 丁 on 2017/10/12.
 * 自定义日期选择框
 */
public class ScheduleMonthAdapter extends RecyclerView.Adapter<ScheduleMonthAdapter.ItemHolder> {
    private List<PopItem> mData;
    private Context mContext;

    private OnItemClick mOnItemClick;

    public ScheduleMonthAdapter(List<PopItem> mData) {
        this.mData = mData;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.date_schedulemonth_item, parent, false));
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
            if (!mData.get(position).isSelect())
                holder.item.setTextColor(0xFF4A4A4A);
        } else {
            holder.item.setTextColor(0xFF999999);
        }
        holder.item.setEnabled(mData.get(position).isEnable());
        holder.view.setOnClickListener(v -> {
            if (mOnItemClick != null) {
                mOnItemClick.onItemClicked(v, position);
            }
        });


        holder.llcontent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.llcontent.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setDescendantFocusability(LinearLayout.FOCUS_BLOCK_DESCENDANTS);

        for (int i = 0; i < new Random().nextInt(4); i++) {
            TextView titleText = new TextView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(1, 1, 1, 1);
            titleText.setLayoutParams(lp);
            titleText.setGravity(Gravity.CENTER);
            titleText.setText("OA");
            linearLayout.addView(titleText);
        }
        holder.llcontent.addView(linearLayout);

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
        LinearLayout llcontent;
        View view;

        ItemHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.pop_item);
            image = (ImageView) itemView.findViewById(R.id.iv_icon);
            llcontent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            view = itemView.findViewById(R.id.ll_rootView);

        }
    }

    /**
     * PopWindow点击事件回调接口
     */
    public interface OnItemClick {
        void onItemClicked(View v, int position);
    }

    public ScheduleMonthAdapter setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
        return this;
    }
}
