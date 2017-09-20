package com.shanlinjinrong.views.baserecycler.cell;

import android.view.ViewGroup;

import com.shanlinjinrong.views.baserecycler.base.RVBasicViewHolder;


/**
 * Created by k.huang on 2017/2/20.
 */

public interface Cell {
    /**
     * 回收资源处理，例如bitmap的回收
     */
    public void releaseResource();

    /**
     * 获得view的类型
     * @return
     */
    public int getItemType();

    /**
     * 创建ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    public RVBasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 数据绑定
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RVBasicViewHolder holder, int position);
}
