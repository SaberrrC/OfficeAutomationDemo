package com.shanlinjinrong.views.listview.cell;

import com.shanlinjinrong.bean.DataItemDetail;
import com.shanlinjinrong.views.listview.adapter.RefreshAdapter;

/**
 * Created by 丁 on 2017/9/14.
 */

public class EmptyCell extends DefaultCell {

    public EmptyCell(DataItemDetail itemDetail) {
        super(itemDetail);
    }

    @Override
    protected void bindData() {

    }

    @Override
    public int getItemType() {
        return RefreshAdapter.EMPTY_TYPE;
    }
}
