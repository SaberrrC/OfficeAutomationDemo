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

public class LoadMoreCell extends RVAbsStateCell {
    public static final int mDefaultHeight = 120;

    public LoadMoreCell(Object o) {
        super(o);
    }

    @Override
    protected View getDefaultView(Context context) {

        return LayoutInflater.from(context).inflate(R.layout.list_load_more_cell, null);
    }

    @Override
    public int getItemType() {
        return SimpleRVBasicAdapter.LOAD_MORE_TYPE;
    }

    @Override
    public void onBindViewHolder(RVBasicViewHolder holder, int position) {

    }
}
