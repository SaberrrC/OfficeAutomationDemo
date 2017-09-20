package com.shanlinjinrong.views.listview.cell;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shanlinjinrong.bean.DataItemDetail;
import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.listview.holder.BaseViewHolder;

/**
 * Created by 丁 on 2017/9/14.
 * base cell 样式
 */
public abstract class BaseCell implements Cell {

    protected DataItemDetail itemDetail;

    public BaseCell(DataItemDetail itemDetail) {
        this.itemDetail = itemDetail;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (getCellLayout() == 0) {
            new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_empty_cell_layout, parent, false));
        }
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(getCellLayout(), parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        bindView(holder);
        bindData();
    }

    public DataItemDetail getData() {
        return itemDetail;
    }

    //设置布局样式
    protected abstract int getCellLayout();

    //设置数据
    protected abstract void bindView(BaseViewHolder holder);

    protected abstract void bindData();
}
