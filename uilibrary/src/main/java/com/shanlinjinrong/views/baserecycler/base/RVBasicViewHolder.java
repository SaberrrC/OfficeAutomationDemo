package com.shanlinjinrong.views.baserecycler.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by k.huang on 2017/2/20.
 */
public class RVBasicViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mItemView;

    public RVBasicViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
        mItemView = itemView;
    }

    /**
     * View用SparseArray 保存进行了复用，避免每一次都find
     * @param viewId
     * @param <V>
     * @return
     */
    public <V extends View> V retrieveView(int viewId){
        View view = mViews.get(viewId);
        if (null == view){
            view = mItemView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (V)view;
    }

    public void clear(){
        mViews.clear();
    }
    /**
     * 获取ItemView
     * @return
     */
    public View getItemView() {
        return mItemView;
    }

}
