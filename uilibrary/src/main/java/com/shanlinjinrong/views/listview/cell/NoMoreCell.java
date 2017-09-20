package com.shanlinjinrong.views.listview.cell;

import com.shanlinjinrong.bean.DataItemDetail;
import com.shanlinjinrong.views.listview.adapter.RefreshAdapter;

/**
 * Created by 丁 on 2017/9/14.
 */
public class NoMoreCell extends DefaultCell {

    public NoMoreCell(DataItemDetail itemDetail) {
        super(itemDetail);
    }

    @Override
    protected void bindData() {
        mMessage.setText("没有更多数据");
    }

    @Override
    public int getItemType() {
        return RefreshAdapter.NO_MORE_TYPE;
    }
}
