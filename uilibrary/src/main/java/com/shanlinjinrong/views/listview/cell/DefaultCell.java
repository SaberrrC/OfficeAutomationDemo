package com.shanlinjinrong.views.listview.cell;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.bean.DataItemDetail;
import com.shanlinjinrong.uilibrary.R;
import com.shanlinjinrong.views.listview.holder.BaseViewHolder;

/**
 * Created by 丁 on 2017/9/14.
 * 默认cell
 */
public abstract class DefaultCell extends BaseCell {
    TextView mMessage;
    private View mView;
    private String mTextMessage;

    DefaultCell(DataItemDetail itemDetail) {
        super(itemDetail);
    }

    @Override
    protected int getCellLayout() {
        return R.layout.default_list_cell;
    }

    @Override
    protected void bindView(BaseViewHolder holder) {
        mMessage = holder.findViewById(R.id.tv_message);
        LinearLayout mContentView = holder.findViewById(R.id.content_view);
        if (mView != null) {
            mContentView.removeAllViews();
            mContentView.addView(mView);
            mContentView.setVisibility(View.VISIBLE);
            mMessage.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mTextMessage)) {
            mMessage.setText(mTextMessage);
        }
    }

    public void setCustomView(View view) {
        mView = view;
    }

    //设置显示文字
    public DefaultCell setMessage(String message) {
        mTextMessage = message;
        if (mMessage != null && !TextUtils.isEmpty(message)){
            mMessage.setText(message);
        }
        return this;
    }
}
