package com.shanlinjinrong.views.listview.cell;

import com.shanlinjinrong.bean.DataItemDetail;
import com.shanlinjinrong.views.listview.adapter.RefreshAdapter;

/**
 * Created by 丁 on 2017/9/14.
 * 加载更多
 */
public class LoadMoreCell extends DefaultCell {

    public LoadMoreCell(DataItemDetail itemDetail) {
        super(itemDetail);
    }

    @Override
    protected void bindData() {
        mMessage.setText("上拉加载更多");
    }

    @Override
    public int getItemType() {
        return RefreshAdapter.LOAD_MORE_TYPE;
    }
}
