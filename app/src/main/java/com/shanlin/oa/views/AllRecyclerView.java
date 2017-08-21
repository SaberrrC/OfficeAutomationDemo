package com.shanlin.oa.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by 丁 on 2017/8/21.
 * 显示list的全部高度
 */
public class AllRecyclerView extends RecyclerView {
    public AllRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AllRecyclerView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}
