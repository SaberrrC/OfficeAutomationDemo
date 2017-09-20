package com.shanlinjinrong.views.listview.cell;

import com.shanlinjinrong.bean.DataItemDetail;
import com.shanlinjinrong.views.listview.adapter.RefreshAdapter;

/**
 * Created by 丁 on 2017/9/14.
 */

public class ErrorCell extends DefaultCell {

    public ErrorCell(DataItemDetail itemDetail) {
        super(itemDetail);
    }

    @Override
    protected void bindData() {
        mMessage.setText("加载出错");
    }

    @Override
    public int getItemType() {
        return RefreshAdapter.ERROR_TYPE;
    }
}
