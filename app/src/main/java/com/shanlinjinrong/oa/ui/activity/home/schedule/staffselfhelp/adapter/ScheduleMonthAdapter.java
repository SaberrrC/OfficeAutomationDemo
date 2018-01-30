package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
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
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MonthlyCalenderPopItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksContract;
import com.shanlinjinrong.oa.utils.ScreenUtils;
import com.shanlinjinrong.utils.DeviceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Random;

public class ScheduleMonthAdapter extends RecyclerView.Adapter<ScheduleMonthAdapter.ItemHolder> {
    private List<MonthlyCalenderPopItem> mData;
    private Context mContext;
    private int mViewHeight;
    private int mViewWidth;
    private OnItemClick mOnItemClick;

    public ScheduleMonthAdapter(List<MonthlyCalenderPopItem> mData, int mViewHeight, int mViewWidth) {
        this.mData = mData;
        this.mViewHeight = mViewHeight;
        this.mViewWidth = mViewWidth;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.date_schedulemonth_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        ViewGroup.LayoutParams mLayoutParams = holder.view.getLayoutParams();
        mLayoutParams.height = mViewHeight;
        holder.view.setLayoutParams(mLayoutParams);

        holder.item.setText(mData.get(position).getContent());
        if (mData.get(position).isEnable()) {
            if (!mData.get(position).isSelect()) {
                holder.item.setTextColor(0xFF333333);
                holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                holder.view.setBackground(null);

            } else {
                holder.item.setTextColor(0xFF333333);
//                holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.bg_B269B0F2));
                holder.view.setBackground(mContext.getResources().getDrawable(R.drawable.bg_calendar_selected));
            }
        } else {
            holder.item.setTextColor(0xFFEFEFEF);
        }

        holder.view.setEnabled(mData.get(position).isEnable());
        holder.view.setOnClickListener(v -> {
            if (mOnItemClick != null) {
                mOnItemClick.onItemClicked(v, position);
            }
        });
        holder.llcontent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.llcontent.removeAllViews();
        if (mData.get(position).isEnable()) {
            if (mData.get(position).getData() != null && mData.get(position).getData().size() > 0) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setDescendantFocusability(LinearLayout.FOCUS_BLOCK_DESCENDANTS);
                for (int i = 0; i < mData.get(position).getData().size(); i++) {
                    TextView titleText = new TextView(mContext);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(5, 5, 5, 5);
                    titleText.setLayoutParams(lp);
                    titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    titleText.setMaxLines(1);
                    titleText.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                    titleText.setGravity(Gravity.CENTER);
                    titleText.setText(mData.get(position).getData().get(i).getTaskTheme());
                    if (i < 2) {
                        if (mData.get(position).getData().get(i).getStatus() == 1) { //完成
                            titleText.setBackgroundColor(mContext.getResources().getColor(R.color.bg_F5F5F5));
                            titleText.setTextColor(mContext.getResources().getColor(R.color.tab_bar_text_gray));
                        } else {
                            titleText.setBackgroundColor(mContext.getResources().getColor(R.color.bg_69B0F2));
                            titleText.setTextColor(mContext.getResources().getColor(R.color.text_333333));
                        }

                        titleText.setText(mData.get(position).getData().get(i).getTaskTheme());
                        linearLayout.addView(titleText);
                    } else {
                        titleText.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                        titleText.setTextColor(mContext.getResources().getColor(R.color.text_333333));
                        titleText.setText("共" + mData.get(position).getData().size());
                        linearLayout.addView(titleText);
                        break;
                    }
                }
                holder.llcontent.addView(linearLayout);
            } else {
                holder.llcontent.removeAllViews();
            }
        }

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
