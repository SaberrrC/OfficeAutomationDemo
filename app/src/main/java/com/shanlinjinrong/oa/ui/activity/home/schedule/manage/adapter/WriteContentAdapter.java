package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

import android.content.Context;
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
    private int     mHeight;
    private Context mContext;

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
            llContent.setBackgroundColor(mContext.getResources().getColor(R.color.gray_99EFEFEF));
        } else {
            llContent.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        if (content.getData().size() == 0) {
            llContent.setOnClickListener(view -> EventBus.getDefault().post(new SelectedWeekCalendarEvent(baseViewHolder.getPosition(), "changeView")));
        }

        llContent.removeAllViews();
        if (content.getData().size() > 0) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            for (int i = 0; i < content.getData().size(); i++) {
                if (i > 1) {
                    continue;
                }
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setDescendantFocusability(LinearLayout.FOCUS_BLOCK_DESCENDANTS);

                TextView titleText = new TextView(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(15, 5, 15, 5);

                titleText.setLayoutParams(lp);
                titleText.setFocusable(false);
                titleText.setClickable(true);
                titleText.setPressed(false);
                titleText.setGravity(Gravity.CENTER);

                titleText.setText(content.getData().get(i).getTaskTheme());
                if (content.getData().get(i).getStatus() == 1){
                    titleText.setBackgroundColor(mContext.getResources().getColor(R.color.F5F5F5));
                }else {
                    titleText.setBackgroundColor(mContext.getResources().getColor(R.color.blue_69B0F2));
                }
                linearLayout.addView(titleText);
            }
            llContent.addView(linearLayout);
            TextView contentCount = new TextView(mContext);
            contentCount.setWidth(ScreenUtils.dp2px(mContext, 45));
            contentCount.setText("共" + content.getData().size() + "项");
            contentCount.setTextSize(14);
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
