package com.shanlinjinrong.views.listview.cell;

import android.view.ViewGroup;

import com.shanlinjinrong.views.listview.holder.BaseViewHolder;

/**
 * Created by 丁 on 2017/9/14.
 * cell 内容接口
 */
public interface Cell {
    /**
     * 获得view的类型
     */
    int getItemType();

    /**
     * 创建ViewHolder
     */
    BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 数据绑定
     */
    void onBindViewHolder(BaseViewHolder holder, int position);
}
