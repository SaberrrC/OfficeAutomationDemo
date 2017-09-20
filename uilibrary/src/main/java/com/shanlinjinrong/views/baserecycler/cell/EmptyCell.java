package com.shanlinjinrong.views.baserecycler.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.baserecycler.base.RVBasicViewHolder;
import com.shanlinjinrong.views.baserecycler.base.SimpleRVBasicAdapter;


/**
 * Created by k.huang on 2017/2/20.
 */

public class EmptyCell extends RVAbsStateCell {
    public EmptyCell(Object o) {
        super(o);
    }

    @Override
    protected View getDefaultView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.rv_empty_cell_layout,null);
    }

    @Override
    public int getItemType() {
        return SimpleRVBasicAdapter.EMPTY_TYPE;
    }

    @Override
    public void onBindViewHolder(RVBasicViewHolder holder, int position) {

    }
}
