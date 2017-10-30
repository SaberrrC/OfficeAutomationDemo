package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.Dialog_Common_bean;

import java.util.List;

public class ApproveDecorationLine extends RecyclerView.ItemDecoration {
    private List<Dialog_Common_bean> mData;

    private Paint mPaint;
    private Rect mBounds;//用于存放测量文字Rect

    private int mTitleHeight;//title的高
    private int mLineHeight = 2;//line的高
    private int mMarginLeft;//距离左侧距离

    private Context mContext;


    public ApproveDecorationLine(Context context, List<Dialog_Common_bean> mData) {
        mContext = context;
        this.mData = mData;
        mPaint = new Paint();
        mBounds = new Rect();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, context.getResources().getDisplayMetrics());
        int mTitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics());
        mMarginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mTitleFontSize);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawVertical(c, parent);
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        if (position > 0) {
            outRect.set(0, mLineHeight, 0, 0);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = 0;
        final int right = parent.getWidth();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (position > 0) {
                //绘制分割线
                mPaint.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.gray_d5d5d5, null));
                c.drawLine(left + mMarginLeft, child.getY(), right - mMarginLeft, child.getY(), mPaint);
            }
        }
    }

    /**
     * 绘制Title区域背景和文字的方法
     *
     * @param c
     * @param left
     * @param right
     * @param child
     * @param params
     * @param position
     */
    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {//最先调用，绘制在最下层
        mPaint.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.page_bg, null));
        //标题栏背景色
        c.drawRect(left, child.getTop() - mTitleHeight, right, child.getTop(), mPaint);
        mPaint.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.gray_9b9b9b, null));
    }
}

