package com.shanlin.oa.ui.activity.home.workreport.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.shanlin.oa.ui.activity.home.workreport.bean.ItemBean;

import java.util.List;

/**
 * Created by 丁 on 2017/8/22.
 */

public class DecorationLine extends RecyclerView.ItemDecoration {


    private List<ItemBean> mData;

    private Paint mPaint;
    private Rect mBounds;//用于存放测量文字Rect

    private int mTitleHeight;//title的高
    private int COLOR_TITLE_BG = Color.parseColor("#f2f2f2");
    private int COLOR_TITLE_FONT = Color.parseColor("#9B9B9B");
    private int mMarginLeft;//距离左侧距离


    public DecorationLine(Context context, List<ItemBean> mData) {
        this.mData = mData;
        mPaint = new Paint();
        mBounds = new Rect();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, context.getResources().getDisplayMetrics());
        int mTitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics());
        mMarginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mTitleFontSize);
        mPaint.setAntiAlias(true);
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
            if (mData.get(position).getGroup()) {
                outRect.set(0, mTitleHeight, 0, 0);//设置title
            } else {
                outRect.set(0, 2, 0, 0);
            }
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (position > 0) {
                if (mData.get(position).getGroup()) {
                    drawTitleArea(c, left, right, child, params, position);
                } else {
//                    mDriver.draw(c);
                }
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
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(left, child.getTop() - params.topMargin - mTitleHeight, right, child.getTop() - params.topMargin, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(mData.get(position).getGroupTitle(), 0, mData.get(position).getGroupTitle().length(), mBounds);
        c.drawText(mData.get(position).getGroupTitle(), child.getPaddingLeft() + mMarginLeft, child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mPaint);
    }
}
