package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.LeftDateBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/16
 * 功能描述：
 */

public class WriteContentAdapter extends BaseQuickAdapter<LeftDateBean> {
    private int                       mHeight;
    private Context                   mContext;
    private LinearLayout.LayoutParams mLp;

    public WriteContentAdapter(List<LeftDateBean> data, Context context, int height) {
        super(R.layout.item_week_calendaer_content, data);
        mHeight = height;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LeftDateBean content) {
        LinearLayout llContent = baseViewHolder.getView(R.id.ll_week_calendar_content);
        llContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight));
        if (content.isSelected()) {
            llContent.setBackground(mContext.getResources().getDrawable(R.drawable.bg_calendar_selected));
        } else {
            llContent.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            llContent.setBackground(null);
        }

        if (content.getData().size() == 0) {
            llContent.setOnClickListener(view -> EventBus.getDefault().post(new SelectedWeekCalendarEvent(baseViewHolder.getPosition(), "changeView")));
        }

        llContent.removeAllViews();
        if (content.getData().size() > 0) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            for (int i = 0; i < content.getData().size(); i++) {
                if (i > 1) {
                    break;
                }
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setDescendantFocusability(LinearLayout.FOCUS_BLOCK_DESCENDANTS);

                TextView titleText = new TextView(mContext);
                if (mHeight > 100) {
                    mLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mLp.setMargins(15, 5, 15, 5);
                    titleText.setTextSize(12);
                } else if (mHeight > 80 && mHeight < 100) {
                    mLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((mHeight / 2) - 15));
                    mLp.setMargins(15, 5, 15, 5);
                    titleText.setTextSize(12);
                } else {
                    mLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((mHeight / 2) - 10));
                    mLp.setMargins(15, 3, 15, 3);
                    titleText.setTextSize(10);
                }

                titleText.setLayoutParams(mLp);
                titleText.setFocusable(false);
                titleText.setClickable(true);
                titleText.setPressed(false);
                titleText.setSingleLine();
                titleText.setEllipsize(TextUtils.TruncateAt.END);
                titleText.setMaxLines(1);
                titleText.setGravity(Gravity.CENTER_VERTICAL);


                titleText.setText(content.getData().get(i).getTaskTheme());
                if (content.getData().get(i).getStatus() == 1) {
                    titleText.setBackgroundColor(mContext.getResources().getColor(R.color.F5F5F5));
                } else {
                    titleText.setBackgroundColor(mContext.getResources().getColor(R.color.blue_69B0F2));
                }
                linearLayout.addView(titleText);
            }
            llContent.addView(linearLayout);
            TextView contentCount = new TextView(mContext);
            contentCount.setWidth(ScreenUtils.dp2px(mContext, 50));
            contentCount.setText("共" + content.getData().size() + "项");
            contentCount.setTextSize(14);
            contentCount.setGravity(Gravity.CENTER);
            contentCount.setSingleLine();
            llContent.setDescendantFocusability(LinearLayout.FOCUS_BLOCK_DESCENDANTS);
            llContent.addView(contentCount);
        }
        if (llContent.getChildCount() > 0) {
            llContent.setOnClickListener(view -> EventBus.getDefault().post(new SelectedWeekCalendarEvent(baseViewHolder.getPosition(), "popupWindow")));
        }
        for (int i = 0; i < content.getData().size(); i++) {
            LinearLayout childAt = (LinearLayout) llContent.getChildAt(0);
            TextView childAt1 = (TextView) childAt.getChildAt(i);
            if (childAt1 != null) {
                childAt1.setOnClickListener(view -> EventBus.getDefault().post(new SelectedWeekCalendarEvent(baseViewHolder.getPosition(), "popupWindow")));
            }
        }
    }
}
