package com.shanlinjinrong.views.listview.holder;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by 丁 on 2017/9/11.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;
    private View mItemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        mItemView = itemView;
    }

    /**
     * 保存View，避免每一次都find
     */
    public <V extends View> V findViewById(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (null == view) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (V) view;
    }

    public void clear() {
        mViews.clear();
    }

}
